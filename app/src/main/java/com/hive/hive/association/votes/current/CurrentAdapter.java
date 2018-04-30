package com.hive.hive.association.votes.current;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CurrentAdapter extends RecyclerView.Adapter<AgendasViewHolder> {
    private String TAG = CurrentAdapter.class.getSimpleName();
    //-- Data
    private HashMap<String, Agenda> mAgendas;
    private ArrayList<String> mAgendaIds;
    private HashMap<String, Integer> mAgendaScore;
    public  static String mCurrentAgendaId;
    private Session mCurrentSession;
    public static boolean  mHasVoted = false;
    //-- Timer
    ArrayList<CountDownTimer> mTimers;
    CountDownTimer mUnfoldableTimer;
    // Local for now
    private HashMap<String, String> mIconsDrawablePaths;
    private HashMap<String, Integer> mIconsDrawable;

    //-- Views
    private  UnfoldableView mUnfoldableView;
    private  FrameLayout mDetailsLayout;
    private View mView;
    //-- Current Agenda Questions
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mQuestionsEL;
    private ListenerRegistration mQuestionsLR;
    private HashMap<String, Question> mQuestions; //FROM CURRENT AGENDA
    private ArrayList<String> mQuestionsIds; // FROM CURRENT AGENDA
    private Context mContext;

    //-- IDS TO PASS TO VOTE
    String agendaID;
    public CurrentAdapter(Context context, Session session, HashMap<String, Agenda> agendas, ArrayList<String> agendasIds, HashMap<String, Integer> agendaScore,
                          UnfoldableView unfoldableView, FrameLayout detailsLayout, View view){
        this.mContext = context;
        this.mCurrentSession = session;
        this.mAgendas = agendas;
        this.mAgendaIds = agendasIds;
        this.mAgendaScore = agendaScore;
        this.mUnfoldableView = unfoldableView;
        this.mDetailsLayout = detailsLayout;
        this.mView = view;
        this.mTimers = new ArrayList<>();

    }

    public CountDownTimer getmUnfoldableTimer() {
        return mUnfoldableTimer;
    }

    public void setmCurrentSession(Session mCurrentSession) {
        this.mCurrentSession = mCurrentSession;
    }

    @Override
    public AgendasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_cell, parent, false);

        // Init locally
        initPossibleCategoryIcons();

        mQuestionsIds = new ArrayList<>();

        mQuestions = new HashMap<>();

        mQuestionsEL = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for(DocumentChange dc : documentSnapshots.getDocumentChanges()){
                    switch (dc.getType()){
                        case ADDED:
                            Log.d(TAG, "added questions");
                            String questionId = dc.getDocument().getId();
                            Question question = dc.getDocument().toObject(Question.class);
                            Log.d(TAG, "question "+  question.getInfo());
                            mQuestions.put(questionId, question);
                            mQuestionsIds.add(questionId);
                            CurrentFragment.setGridQuestionsItems(mContext, mQuestions, mQuestionsIds, agendaID);
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mQuestions.remove(modifiedId);
                            mQuestions.put(modifiedId, dc.getDocument().toObject(Question.class));
                            CurrentFragment.updateItems();
                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();
                            mQuestions.remove(removedId);
                            mQuestionsIds.remove(removedId);
                            CurrentFragment.setGridQuestionsItems(mContext, mQuestions, mQuestionsIds, agendaID);
                            break;
                    }
                }
            }
        };
        return new AgendasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AgendasViewHolder holder, int position) {
        //get current agenda
        agendaID = mAgendaIds.get(position);

        final Agenda agenda = mAgendas.get(agendaID);

        //populate views
        holder.getmTitle().setText(agenda.getTitle());
        //TODO:Change this line to get from server
        holder.getmCategoryIcon().setImageResource(getDrawable("services"));

        //sets Agenda Score
        if(mAgendaScore != null && mAgendaIds.get(position) != null)
            holder.getmRequestScore().setText(mAgendaScore.get(mAgendaIds.get(position)).toString());

        //TODO USE RETURN FROM CLOCK TO STOP SHIT
        //loads agenda remaining time
        mTimers.add(TimeUtils.clock(holder.getmTime(), mCurrentSession, mContext));


        holder.getmVote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearQuestions();
                agendaID = mAgendaIds.get(position);
                changeUnfoldableContent(agenda, agendaID);
                mUnfoldableView.unfold(view, mDetailsLayout);

            }
        });


    }
    @Override
    public int getItemCount() {
        return mAgendaIds.size();
    }

    private void clearQuestions(){
        //solves questions mExpandableQuestionsAdapter bug
        try {
            if (CurrentFragment.mExpandableQuestionsAdapter != null)
                for (int i = 0; i < CurrentFragment.mExpandableQuestionsAdapter.getGroupCount(); i++)
                    CurrentFragment.expandableListView.collapseGroup(i);
            mQuestions.clear();
            mQuestionsIds.clear();
        }catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }
    }

    private void changeUnfoldableContent(Agenda agenda, String agendaId){
        //load views
        TextView titleTV = mView.findViewById(R.id.expandable_titleContentTV);
        TextView descriptionTV = mView.findViewById(R.id.expandable_contentTV);
        TextView timeTV = mView.findViewById(R.id.expandable_timerTV);
        TextView requestScoreTV = mView.findViewById(R.id.expandable_supportTV);

        //set agenda texts
        titleTV.setText(agenda.getTitle());
        descriptionTV.setText(agenda.getContent());
        requestScoreTV.setText(mAgendaScore.get(agendaId).toString());
        fillUser(agenda.getSuggestedByRef());

        //sets time
        mUnfoldableTimer = TimeUtils.clock(timeTV, mCurrentSession, mContext);

        //sets the current agenda, ExpandableListAdapter depends on it
        mCurrentAgendaId = agendaId;

        //check Listeners  Registration, removes if necessary
        if(CurrentFragment.mHasVotedLR != null) CurrentFragment.mHasVotedLR.remove();
        //TODO CHECK LAST ITEM CLICKED BEFORE RELOADING DATA
        //IF CLICK IS DIFF
        if(mQuestionsLR != null) //catches the first run
            mQuestionsLR.remove();
        //TODO REMOVE STATIC ASSOCIATION REFERENCE
        if(CurrentFragment.mCurrentSessionId != null)// should'nt happen, but just to be sure
            mQuestionsLR = VotesHelper.getQuestions(FirebaseFirestore.getInstance(),"gVw7dUkuw3SSZSYRXe8s",
                    CurrentFragment.mCurrentSessionId, agendaId).addSnapshotListener(mQuestionsEL);


    }

    //loads user data, and fill some views
    private void fillUser(DocumentReference userRef){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    TextView suggestedByTV = mView.findViewById(R.id.expandable_suggestedByTV);
                    ImageView suggestedByIV = mView.findViewById(R.id.expandable_suggestedByIV);
                    User user = documentSnapshot.toObject(User.class);
                    suggestedByTV.setText(user.getName());
                    ProfilePhotoHelper.loadImage(mView.getContext().getApplicationContext(), suggestedByIV, user.getPhotoUrl());
                    //Log.d(RequestAdapter.class.getSimpleName(), user.getPhotoUrl());
                }
            }
        });
    }

    // Use to get the drawables programmatically
    public void initPossibleCategoryIcons(){
        List<String> iconsList = Arrays.asList("services", "cleaning", "gardening", "security");
        mIconsDrawable = new HashMap<>();
        mIconsDrawablePaths = new HashMap<>();

        for (String icon: iconsList) {
            mIconsDrawablePaths.put(icon, "ic_icones_"+icon+"_white");
            int imageResource = mContext.getResources()
                                        .getIdentifier(mIconsDrawablePaths.get(icon), "drawable", mContext.getPackageName());
            mIconsDrawable.put(icon, imageResource);
        }
    }

    public int getDrawable(String icon){
        return mIconsDrawable.get(icon);
    }


}
