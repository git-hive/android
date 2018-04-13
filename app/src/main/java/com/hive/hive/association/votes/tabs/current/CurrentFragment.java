package com.hive.hive.association.votes.tabs.current;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;

// In this case, the fragment displays simple text based on the page
public class CurrentFragment extends Fragment {

    public static final String ARG_PAGE = "Passadas";
    private static final String TAG = CurrentFragment.class.getSimpleName();
    View mView;

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
    ScrollView detailsScrollView;

    // Temporary solution to unfold card, TODO: Check with the @guys
    ImageView mTopClickableCardIV;

    // Expandable List View
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;

    // Views references
    static ImageButton choseVoteBT;
    ImageView supportsIV;
    TextView supportsTV;


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

        mView = view;

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
                unfoldableView.setGesturesEnabled(true);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);
                mRVAdapter.getmUnfoldableTimer().cancel();
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
                            mRVAdapter.setmCurrentSession(mCurrentSession);
                            mRVAdapter.notifyDataSetChanged();
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

        mRVAdapter = new CurrentAdapter(this.getContext(), mCurrentSession, mAgendas, mAgendasIds , mUnfoldableView, mDetailsLayout, view);
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

        // Get scroll refence
        detailsScrollView = view.findViewById(R.id.cardScroll);

        // Solution by: https://github.com/alexvasilkov/FoldableLayout/issues/38#issuecomment-192814520
        // Allows scroll
        detailsScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUnfoldableView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUnfoldableView.requestDisallowInterceptTouchEvent(true);
                return false;
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
    public static void setItems(final Context context, final HashMap<String, Question> questions, final ArrayList<String> questionsIds, final String agendaID ){

        adapter = new ExpandableListAdapter(context, questions, questionsIds);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, QuestionForm.class);
                it.putExtra("questions", questions);
                it.putExtra("questionsIds", questionsIds);
                //TODO STATIC ASSOCIATION ID
                it.putExtra("associationID", "gVw7dUkuw3SSZSYRXe8s");
                it.putExtra("sessionID", mCurrentSessionId);
                it.putExtra("agendaID", agendaID );

                context.startActivity(it);
            }
        });


        // THIS MAGIC PEACE OF CODE MAKE THE VIEW WORK AS IT SHOULD
        expandableListView.setDividerHeight(0);

        for (int i = 0; i < adapter.getGroupCount(); i++)
            expandableListView.expandGroup(i);
        setListViewHeight(expandableListView);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        for (int i = 0; i < adapter.getGroupCount(); i++)
            expandableListView.collapseGroup(i);
        setListViewHeight(expandableListView);

    }


    // Updating headers and childs to expandable listview
    public static void updateItems(){
        adapter.notifyDataSetChanged();
    }


    // Workaround found in: https://thedeveloperworldisyours.com/android/expandable-listview-inside-scrollview/ to ExpandableListView
    // https://stackoverflow.com/questions/17696039/expandablelistview-inside-a-scrollview

    private static void setListViewHeight(ExpandableListView listView) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupView = listAdapter.getGroupView(i, true, null, listView);
            groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupView.getMeasuredHeight();

            if (listView.isGroupExpanded(i)){
                for(int j = 0; j < listAdapter.getChildrenCount(i); j++){
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private static void setListViewHeight(ExpandableListView listView,
                                          int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}