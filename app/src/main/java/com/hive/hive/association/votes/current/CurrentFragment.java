package com.hive.hive.association.votes.current;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.association.votes.question_answering.QuestionFormActivity;
import com.hive.hive.association.votes.questions.adapters.ExpandableListAdapter;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Session;
import com.hive.hive.utils.unfoldable.UnfoldableSharedMethods;

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
    private HashMap<String, Integer> mAgendasScores;
    public static ListenerRegistration mHasVotedLR;
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
    public static ExpandableListView expandableListView;
    public static ExpandableListAdapter mExpandableQuestionsAdapter;

    // Unfoldable Views references
    private static ImageButton choseVoteBT;
    private static TextView mHasVotedTV;

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

        choseVoteBT = view.findViewById(R.id.expandable_choseVoteBT);
        mHasVotedTV = view.findViewById(R.id.expandable_voteStatusTV);
        // Temporary solution to unfold card, TODO: Check with the @guys
        mTopClickableCardIV = view.findViewById(R.id.expandable_topCardIV);
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
                if (e != null) {
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
//                            Log.d(TAG, "ADDED current sesh " + dc.getDocument().toObject(Session.class).getStatus());
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
        mAgendasScores = new HashMap<>();
        mAgendasEL = new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Agenda agenda = dc.getDocument().toObject(Agenda.class);
//                            VotesHelper.setAgendas(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s"
//                                    , mCurrentSessionId, agenda);
                            String addedId = dc.getDocument().getId();
                            mAgendas.put(addedId, agenda);
                            mAgendasIds.add(addedId);
                            mAgendas.get(addedId).getRequestRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        Request request = documentSnapshot.toObject(Request.class);
                                        mAgendasScores.put(addedId, request.getScore());
//                                    Log.d(TAG, mAgendas.toString());
                                        mView.findViewById(R.id.agendasPB).setVisibility(View.GONE);
                                        mRVAdapter.notifyDataSetChanged();

                                    }
                                }
                            });
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mAgendas.remove(modifiedId);
                            mAgendas.put(modifiedId, dc.getDocument().toObject(Agenda.class));
                            //  Log.d(TAG, mAgendas.toString());
                            mRVAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();
                            mAgendas.remove(removedId);
                            mAgendasIds.remove(removedId);
                            mAgendasScores.remove(removedId);
//                            Log.d(TAG, mAgendas.toString());
                            mRVAdapter.notifyDataSetChanged();
                            break;
                    }
                }

            }
        };

        mRVAdapter = new CurrentAdapter(this.getContext(), mCurrentSession, mAgendas, mAgendasIds, mAgendasScores, mUnfoldableView, mDetailsLayout, view);
        mRV = view.findViewById(R.id.cellRV);
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setAdapter(mRVAdapter);

        // TODO: Check this expandable element Height, since it has some workarounds, either than set it fixed;

        expandableListView = view.findViewById(R.id.expandable_questionExpandableLV);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);


        // Start Questions activity stuff
        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), QuestionFormActivity.class));

            }
        });

        // Get scroll refence
        detailsScrollView = view.findViewById(R.id.expandable_cardScroll);

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
    public void onResume() {
        super.onResume();
        first = true;
        Glide.with(getContext().getApplicationContext()).resumeRequestsRecursive();
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.with(getContext().getApplicationContext()).pauseRequestsRecursive();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSessionEL != null)
            mSessionLR.remove();
        if (mAgendasLR != null)
            mAgendasLR.remove();
    }

    // Setting headers and childs to expandable listview
    static boolean first = true;

    public static void setGridQuestionsItems(final Context context, final HashMap<String, Question> questions, final ArrayList<String> questionsIds, final String agendaID) {
        if (first) {
            mExpandableQuestionsAdapter = new ExpandableListAdapter(context, questions, questionsIds);

            // Setting adpater over expandablelistview
            expandableListView.setAdapter(mExpandableQuestionsAdapter);
            first = false;
        } else mExpandableQuestionsAdapter.notifyDataSetChanged();
        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, QuestionFormActivity.class);
//                it.putExtra("questions", questions);
//                it.putExtra("questionsIds", questionsIds);
                //TODO STATIC ASSOCIATION ID
                it.putExtra("associationID", "gVw7dUkuw3SSZSYRXe8s");
                it.putExtra("sessionID", mCurrentSessionId);
                it.putExtra("agendaID", agendaID);

                context.startActivity(it);
            }
        });

        //Verifing if user has already voted
        try {
            mHasVotedLR = VotesHelper.getVote(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mCurrentSessionId,
                    agendaID, questionsIds.get(0), FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (e != null)
                        Log.e(TAG, e.getMessage());
                    if (documentSnapshot.exists()) {
                        mHasVotedTV.setText(context.getString(R.string.has_vote));
                        mHasVotedTV.setTextColor(context.getResources().getColor(R.color.green_text));
                    } else {
                        mHasVotedTV.setText(context.getString(R.string.hasn_vote));
                        mHasVotedTV.setTextColor(context.getResources().getColor(R.color.red_text));
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

        UnfoldableSharedMethods.unfoldableMagic(expandableListView, mExpandableQuestionsAdapter);

    }


    // Updating headers and childs to expandable listview
    public static void updateItems() {
        mExpandableQuestionsAdapter.notifyDataSetChanged();
    }


}