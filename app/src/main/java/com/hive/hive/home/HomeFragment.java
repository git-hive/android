package com.hive.hive.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.association.votes.current.CurrentAdapter;
import com.hive.hive.association.votes.current.CurrentAgendaFirebaseHandle;
import com.hive.hive.home.db_files.CurrentAgendasForHomeFirebaseHandle;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Session;
import com.hive.hive.model.forum.Forum;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.user.User;
import com.hive.hive.profiles.UserProfileActivity;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {
    private boolean first = true;//used to verify if there is need to create a adapter

    private RecyclerView mRecyclerViewHome;
    private RecyclerViewHomeAdapter mRecyclerViewHomeAdapter;
    private String TAG = HomeFragment.class.getSimpleName();
    // Views
    ImageView mUserAvatar;
    TextView mGreetingsTV;

    // Settings
    View mView;
    Context mContext;

    // Data
    ArrayList<Object> DUMMYARRAY;


    //Questions
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mQuestionsEL;
    private ListenerRegistration mQuestionsLR;
    private Pair<ArrayList<String>, HashMap<String, Question>> mQuestions; //FROM CURRENT AGENDA

    // Agenda
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas;  //first its agenda ids, second its a map <agendaid, agenda>
    private HashMap<String, Integer> mAgendasScores;
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mAgendasEL;
    private ListenerRegistration mAgendasLR;


    // Session

    private Pair<String, Session> mCurrentSession;
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mSessionEL;
    private ListenerRegistration mSessionLR;



    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Set settings
        mView = v;
        mContext = getContext();

        setCurrentUserInfo();

        initStructures();

        initDummy();

        initRecycler();

        initOnClicks();

        initEventListeners();


        mSessionLR = VotesHelper.getCurrentSession(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s").addSnapshotListener(mSessionEL);


        return v;
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

    public void setCurrentUserInfo(){
        mUserAvatar = mView.findViewById(R.id.userAvatar);
        mGreetingsTV = mView.findViewById(R.id.textViewGreetings);
        try {
            DocumentReference userRef = DocReferences.getUserRef();
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    //mUserScore.setText(user.getScore());
                    String oldGreetings = mGreetingsTV.getText().toString().substring(0, 11);
                    mGreetingsTV.setText(oldGreetings + user.getName());
                    ProfilePhotoHelper.loadImage(mContext, mUserAvatar, user.getPhotoUrl());

                }
            });
        }catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }

    }

    public void addAgenda(String agendaId, Agenda agenda){
        mAgendas.first.add(agendaId);
        mAgendas.second.put(agendaId, agenda);

        getAgendaScore(agendaId);

    }

    public void updateAgenda(String agendaId, Agenda agenda){

        mAgendas.second.put(agendaId, agenda);

        mRecyclerViewHomeAdapter.notifyDataSetChanged();

    }

    public void removeAgenda(String agendaId){
        mAgendas.first.remove(agendaId);
        mAgendas.second.remove(agendaId);
        mAgendasScores.remove(agendaId);

        mRecyclerViewHomeAdapter.notifyDataSetChanged();
    }

    public void addSession(String sessionId, Session session){
        //sets current Session
        mCurrentSession = new Pair<>(sessionId, session);

        //call to get Agendas
        mAgendasLR =
                VotesHelper.getAgendas(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mCurrentSession.first)
                        .addSnapshotListener(mAgendasEL);


        mRecyclerViewHomeAdapter.setmCurrentSession(mCurrentSession);
        mRecyclerViewHomeAdapter.notifyDataSetChanged();

    }

    public void updateSession(String sessionId, Session session){
        mCurrentSession = new Pair<>(sessionId, session);
        //TODO SHOULD UPDATE SOMETHING ELSE???

    }

    public void removeSession(){
        mCurrentSession = null;
        mAgendasLR.remove();
        mAgendas.first.clear();
        mAgendas.second.clear();
        mRecyclerViewHomeAdapter.notifyDataSetChanged();
        mCurrentSession = null;
    }

    public void addQuestions(String questionId, Question question){
        mQuestions.first.add(questionId);

        mQuestions.second.put(questionId, question);

        //setGridQuestionsItems(CurrentAdapter.mCurrentAgendaId);

    }

    public void updateQuestions(String questionId, Question question){
        mQuestions.second.put(questionId, question);

        //mExpandableQuestionsAdapter.notifyDataSetChanged();

    }

    public void removeQuestions(String questionId){
        mQuestions.first.remove(questionId);
        mQuestions.second.remove(questionId);

        //setGridQuestionsItems(CurrentAdapter.mCurrentAgendaId);

    }



    private void initEventListeners(){
        mSessionEL = CurrentAgendasForHomeFirebaseHandle.sessionHandler(this);

        mAgendasEL = CurrentAgendasForHomeFirebaseHandle.agendasHandler(this);

        mQuestionsEL = CurrentAgendasForHomeFirebaseHandle.questionsHanderl(this);

    }

    private void initStructures() {
        mAgendas = new Pair<>(new ArrayList<>(), new HashMap<>());
        mAgendasScores = new HashMap<>();
        mQuestions = new Pair<>(new ArrayList<>(), new HashMap<>());


    }

    public void initRecycler(){
        mRecyclerViewHomeAdapter = new RecyclerViewHomeAdapter(mAgendas, mAgendasScores, DUMMYARRAY);
        mRecyclerViewHome = mView.findViewById(R.id.recyclerViewFeed);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRecyclerViewHome.setAdapter(mRecyclerViewHomeAdapter);
    }

    public  void initDummy(){
        DUMMYARRAY = new ArrayList<>();

        ForumPost post = new ForumPost();
        post.setTitle("Evento Beneficiente para o Inverno");
        post.setContent("Estava pensando em fazermo um evento beneficiente interno...");
        post.setNumComments(2);
        post.setSupportScore(5);
        DUMMYARRAY.add(post);
        DUMMYARRAY.add(new Request());
    }

    public void initOnClicks(){
        mUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, UserProfileActivity.class));
            }
        });
    }

    private void getAgendaScore(String agendaId){
        mAgendas.second.get(agendaId).getRequestRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Request request = documentSnapshot.toObject(Request.class);

                    mAgendasScores.put(agendaId, request.getScore());

                    //hideProgressBar();//got some Agendas to show, no need to show progress anymore

                    mRecyclerViewHomeAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
