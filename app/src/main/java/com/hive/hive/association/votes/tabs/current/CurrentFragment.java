package com.hive.hive.association.votes.tabs.current;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.association.votes.tabs.questions.QuestionForm;
import com.hive.hive.association.votes.tabs.questions.ExpandableListAdapter;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;
import com.hive.hive.utils.hexagonsPercentBar.HexagonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class CurrentFragment extends Fragment {
    private static final int NUM_LIST_ITEMS= 6;
    public static final String ARG_PAGE = "Passadas";
    private static final String TAG = CurrentFragment.class.getSimpleName();
    //Session
    private Session mCurrentSession;
    public static String mCurrentSessionId;
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mSessionEL;
    private ListenerRegistration mSessionLR;
    //Agendas
    private HashMap<String, Agenda> mAgendas;
    private ArrayList<String> mAgendasIds;
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mAgendasEL;
    private ListenerRegistration mAgendasLR;

    //Recycler Things
    RecyclerView mRV;
    CurrentAdapter mRVAdapter;
    //Views
    View mListTouchInterceptor;
    FrameLayout mDetailsLayout;
    UnfoldableView mUnfoldableView;
    HexagonView mPercentageBar;

    // Temporary solution to unfold card, TODO: Check with the @guys
    ImageView mTopClickableCardIV;

    // Expandable List View
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;

    // Views references
    ImageButton choseVoteBT;


    public static CurrentFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CurrentFragment fragment = new CurrentFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_current, container, false);

        choseVoteBT = view.findViewById(R.id.choseVoteBT);

        // Temporary solution to unfold card, TODO: Check with the @guys
        mTopClickableCardIV = view.findViewById(R.id.topCardIV);
        mTopClickableCardIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
                    mUnfoldableView.foldBack();
                }

            }
        });

        mListTouchInterceptor = view.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        mDetailsLayout = view.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);

        // TODO: Take care you should call autoInit always
        mPercentageBar =  view.findViewById(R.id.percentageBar);
        mPercentageBar.autoInit(4);

        // Call set config to set percentage
        // mPercentageBar.setConfig();

        mUnfoldableView = (UnfoldableView) view.findViewById(R.id.unfoldable_view);

        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                mDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);

                // Check this out to unfold when grab down TODO: @MarcoBirck
                unfoldableView.setGesturesEnabled(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);
            }
        });
        //GET CURRENT SESSION
        //TODO REMOVE STATIC ASSOCIATION REFERENCE
        mSessionEL = new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case REMOVED:
                            Log.e(TAG, "No current Session");
                            mCurrentSessionId = null;
                            mAgendasLR.remove();
                            mAgendas.clear();
                            mAgendasIds.clear();
                            mRVAdapter.notifyDataSetChanged();
                            mCurrentSession = null;
                            break;
                            //TODO MAY show message... there is no Session
                        case ADDED:
                            mCurrentSession = dc.getDocument().toObject(Session.class);
                            mCurrentSessionId = dc.getDocument().getId();
                            Log.d(TAG, "ADDED current sesh " + dc.getDocument().toObject(Session.class).getStatus());
                            mAgendasLR =
                                    VotesHelper.getAgendas(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mCurrentSessionId)
                                            .addSnapshotListener(mAgendasEL);
                            break;
                        case MODIFIED:
                            mCurrentSession = dc.getDocument().toObject(Session.class);
                            //TODO SHOULD UPDATE SOMETHING ELSE???
                    }
                }

            }
        };
        mSessionLR = VotesHelper.getCurrentSession(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s").addSnapshotListener(mSessionEL);
        //GET AGENDAS
        mAgendas = new HashMap<>();
        mAgendasIds = new ArrayList<>();

        mAgendasEL = new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Agenda agenda = dc.getDocument().toObject(Agenda.class);
                            mAgendas.put(dc.getDocument().getId(), agenda);
                            mAgendasIds.add(dc.getDocument().getId());
                            Log.d(TAG, mAgendas.toString());
                            mRVAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mAgendas.remove(modifiedId);
                            mAgendas.put(modifiedId, dc.getDocument().toObject(Agenda.class));
                            Log.d(TAG, mAgendas.toString());
                            mRVAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();
                            mAgendas.remove(removedId);
                            mAgendasIds.remove(removedId);
                            Log.d(TAG, mAgendas.toString());
                            mRVAdapter.notifyDataSetChanged();
                            break;
                    }
                }

            }
        };

        mRVAdapter = new CurrentAdapter(this.getContext(),  mAgendas, mAgendasIds , mUnfoldableView, mDetailsLayout, view);
        mRV = view.findViewById(R.id.cellRV);
        mRV.setAdapter(mRVAdapter);

        // TODO: Check this expandable element Height, since it has some workarounds, either than set it fixed;

        expandableListView = view.findViewById(R.id.questionExpandableLV);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        // Start Questions activity stuff
        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), QuestionForm.class));

            }
        });

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        Glide.with(getContext().getApplicationContext()).resumeRequestsRecursive();
    }
    @Override
    public void onStop(){
        super.onStop();
        Glide.with(getContext().getApplicationContext()).pauseRequestsRecursive();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mSessionLR.remove();
        mAgendasLR.remove();
    }
    // Setting headers and childs to expandable listview
    public static void setItems(Context context, HashMap<String, Question> questions, ArrayList<String> questionsIds){

        // Array list for header
        ArrayList<String> header = new ArrayList<String>();

        // Array list for child items
        List<String> child1 = new ArrayList<String>();
        List<String> child2 = new ArrayList<String>();
        List<String> child3 = new ArrayList<String>();
        List<String> child4 = new ArrayList<String>();

        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        // Adding headers to list
        for (int i = 1; i < 5; i++) {
            header.add("Question " + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child1.add("Question 1  " + " : Child" + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child2.add("Question 2  " + " : Child" + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child3.add("Question 3  " + " : Child" + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child4.add("Question 4  " + " : Child" + i);
        }

        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);

        adapter = new ExpandableListAdapter(context, questions, questionsIds, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);

    }

}