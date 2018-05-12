package com.hive.hive.association.votes.current;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    public static final String ARG_PAGE = "Atual";
    private static final String TAG = CurrentFragment.class.getSimpleName();

    //Session

    private Session mCurrentSession;
    public static String mCurrentSessionId;
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mSessionEL;
    private ListenerRegistration mSessionLR;

    //Agendas
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas;  //first its agenda ids, second its a map <agendaid, agenda>
    private HashMap<String, Integer> mAgendasScores;
    public static ListenerRegistration mHasVotedLR;
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mAgendasEL;
    private ListenerRegistration mAgendasLR;


    //Questions
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mQuestionsEL;
    private ListenerRegistration mQuestionsLR;
    private HashMap<String, Question> mQuestions; //FROM CURRENT AGENDA
    private ArrayList<String> mQuestionsIds; // FROM CURRENT AGENDA

    //Recycler Things
    RecyclerView mRV;
    CurrentAdapter mRVAdapter;

    //Views
    View mListTouchInterceptor;
    FrameLayout mDetailsLayout;
    UnfoldableView mUnfoldableView;
    ScrollView detailsScrollView;
    ProgressBar mAgendaPB;
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

        initViews(view);

        initExpandableViews(view);

        initUnfoldable(view);

        initStructures();

        initRecycler(view);

        initOnClicks(view);

        initEventListeners();

        //GET CURRENT SESSION
        //TODO REMOVE STATIC ASSOCIATION REFERENCE

        mSessionLR = VotesHelper.getCurrentSession(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s").addSnapshotListener(mSessionEL);
        //GET AGENDAS

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
        //todo review this
        if (mSessionEL != null)
            mSessionLR.remove();
        if (mAgendasLR != null)
            mAgendasLR.remove();
    }

    private void initViews(View view){
        mAgendaPB = view.findViewById(R.id.agendasPB);

    }
    private void initExpandableViews(View view){

        choseVoteBT = view.findViewById(R.id.expandable_choseVoteBT);
        mHasVotedTV = view.findViewById(R.id.expandable_voteStatusTV);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUnfoldable(View view) {

        //setting to right text
        ((TextView) view.findViewById(R.id.expandable_partialResultsTV)).setText(getText(R.string.final_result));

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

        //used to fold and unfold
        mDetailsLayout = view.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);


        mUnfoldableView = view.findViewById(R.id.unfoldable_view);

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

        expandableListView = view.findViewById(R.id.expandable_questionExpandableLV);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUnfoldableView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        UnfoldableSharedMethods.unfoldableListener(mUnfoldableView, mListTouchInterceptor, mDetailsLayout);

    }

    private void initStructures(){
        mAgendas = new Pair<>(new ArrayList<>(), new HashMap<>());
        mAgendasScores = new HashMap<>();

        mQuestions = new HashMap<>();
        mQuestionsIds = new ArrayList<>();
    }

    private void initRecycler(View view){
        mRVAdapter = new CurrentAdapter(this.getContext(), this,mCurrentSession,
                mAgendas, mAgendasScores, mUnfoldableView, mDetailsLayout, view);
        mRV = view.findViewById(R.id.cellRV);
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setAdapter(mRVAdapter);

    }
    private void initOnClicks(View view){
        // Start Questions activity stuff
        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), QuestionFormActivity.class));

            }
        });
    }
    private void initEventListeners(){
        mSessionEL = CurrentAgendaFirebaseHandle.sessionHandler(this);

        mAgendasEL = CurrentAgendaFirebaseHandle.agendasHandler(this);

        mQuestionsEL = CurrentAgendaFirebaseHandle.questionsHanderl(this);

    }

    public void addSession(String sessionId, Session session){
        //sets current Session
        mCurrentSession = session;
        mCurrentSessionId = sessionId;

        //call to get Agendas
        mAgendasLR =
                VotesHelper.getAgendas(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mCurrentSessionId)
                        .addSnapshotListener(mAgendasEL);


        //todo verify this shit
        mRVAdapter.setmCurrentSession(mCurrentSession);
        mRVAdapter.notifyDataSetChanged();

    }

    public void updateSession(String sessionId, Session session){
        mCurrentSession = session;
        mCurrentSessionId = sessionId;
        //TODO SHOULD UPDATE SOMETHING ELSE???

    }

    public void removeSession(){
        mCurrentSessionId = null;
        mAgendasLR.remove();
        mAgendas.first.clear();
        mAgendas.second.clear();
        mRVAdapter.notifyDataSetChanged();
        mCurrentSession = null;
    }

    public void addAgenda(String agendaId, Agenda agenda){
        mAgendas.first.add(agendaId);
        mAgendas.second.put(agendaId, agenda);

        getAgendaScore(agendaId);

    }

    public void updateAgenda(String agendaId, Agenda agenda){

        mAgendas.second.put(agendaId, agenda);

        mRVAdapter.notifyDataSetChanged();

    }

    public void removeAgenda(String agendaId){
        mAgendas.first.remove(agendaId);
        mAgendas.second.remove(agendaId);
        mAgendasScores.remove(agendaId);

        mRVAdapter.notifyDataSetChanged();
    }

    public void addQuestions(String questionId, Question question){
        mQuestions.put(questionId, question);
        mQuestionsIds.add(questionId);
        setGridQuestionsItems(mQuestions, mQuestionsIds, CurrentAdapter.mCurrentAgendaId);

    }

    public void updateQuestions(String questionId, Question question){
        mQuestions.put(questionId, question);

        mExpandableQuestionsAdapter.notifyDataSetChanged();

    }

    public void removeQuestions(String questionId){
        mQuestions.remove(questionId);
        mQuestionsIds.remove(questionId);

        setGridQuestionsItems(mQuestions, mQuestionsIds, CurrentAdapter.mCurrentAgendaId);

    }

    public void clearQuestions(){
        //solves questions mExpandableQuestionsAdapter bug
        try {
            if (mExpandableQuestionsAdapter != null)
                for (int i = 0; i < mExpandableQuestionsAdapter.getGroupCount(); i++)
                    CurrentFragment.expandableListView.collapseGroup(i);
            mQuestions.clear();
            mQuestionsIds.clear();
        }catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }
    }
    public void changeUnfoldableQuestionsContent(String agendaId){
        //todo check this
        //check Listeners  Registration, removes if necessary
        if(mHasVotedLR != null) mHasVotedLR.remove();
        //TODO CHECK LAST ITEM CLICKED BEFORE RELOADING DATA
        //IF CLICK IS DIFF
        if(mQuestionsLR != null) //catches the first run
            mQuestionsLR.remove();
        //TODO REMOVE STATIC ASSOCIATION REFERENCE
        if(mCurrentSessionId != null)// should'nt happen, but just to be sure
            mQuestionsLR = VotesHelper.getQuestions(FirebaseFirestore.getInstance(),"gVw7dUkuw3SSZSYRXe8s",
                    CurrentFragment.mCurrentSessionId, agendaId).addSnapshotListener(mQuestionsEL);

    }
    private void getAgendaScore(String agendaId){
        mAgendas.second.get(agendaId).getRequestRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Request request = documentSnapshot.toObject(Request.class);

                    mAgendasScores.put(agendaId, request.getScore());

                    hideProgressBar();//got some Agendas to show, no need to show progress anymore

                    mRVAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void hideProgressBar(){
       mAgendaPB.setVisibility(View.GONE);
    }

    private boolean first = true;//used to verify if there is need to create a adapter

    // Setting headers and childs to expandable listview
    public void setGridQuestionsItems(final HashMap<String, Question> questions, final ArrayList<String> questionsIds, final String agendaID) {
        if (first) {
            mExpandableQuestionsAdapter = new ExpandableListAdapter(CurrentFragment.this.getContext(), questions, questionsIds);
            // Setting adpater over expandablelistview
            expandableListView.setAdapter(mExpandableQuestionsAdapter);
            first = false;
        } else mExpandableQuestionsAdapter.notifyDataSetChanged();


        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CurrentFragment.this.getContext(), QuestionFormActivity.class);

             //TODO STATIC ASSOCIATION ID

                it.putExtra("associationID", "gVw7dUkuw3SSZSYRXe8s");
                it.putExtra("sessionID", mCurrentSessionId);
                it.putExtra("agendaID", agendaID);

                CurrentFragment.this.getContext().startActivity(it);
            }
        });

        //Verifing if user has already voted
        verifyIfUserVoted(agendaID, questionsIds.get(0)); //uses firts if, because if user voted in one question, all questions in this agenda where answered

        UnfoldableSharedMethods.unfoldableMagic(expandableListView, mExpandableQuestionsAdapter);

    }



    private void verifyIfUserVoted(String agendaId, String firstQuestionId){
        try {
            mHasVotedLR = VotesHelper.getVote(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mCurrentSessionId,
                    agendaId, firstQuestionId, FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (e != null)
                        Log.e(TAG, e.getMessage());
                    if (documentSnapshot.exists()) {
                        mHasVotedTV.setText(CurrentFragment.this.getContext().getString(R.string.has_vote));
                        mHasVotedTV.setTextColor(CurrentFragment.this.getContext().getResources().getColor(R.color.green_text));
                    } else {
                        mHasVotedTV.setText(CurrentFragment.this.getContext().getString(R.string.hasn_vote));
                        mHasVotedTV.setTextColor(CurrentFragment.this.getContext().getResources().getColor(R.color.red_text));
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

    }


}