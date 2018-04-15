package com.hive.hive.association.votes.tabs.current;


import android.content.Context;
import android.content.Intent;
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
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.association.votes.voters.VotersListActivity;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.RequestViewHolder> {
    private String TAG = CurrentAdapter.class.getSimpleName();
    //-- Data
    private HashMap<String, Agenda> mAgendas;
    private ArrayList<String> mAgendaIds;
    private HashMap<String, Integer> mAgendaScore;
    public  static String mCurrentAgendaId;
    private Session mCurrentSession;
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
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_cell, parent, false);

        // Init locally
        initStuff();

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
                            String questionId = dc.getDocument().getId();
                            Question question = dc.getDocument().toObject(Question.class);
                            mQuestions.put(questionId, question);
                            mQuestionsIds.add(questionId);
                            CurrentFragment.setItems(mContext, mQuestions, mQuestionsIds, agendaID);
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
                            CurrentFragment.setItems(mContext, mQuestions, mQuestionsIds, agendaID);
                            break;
                    }
                }
                if(mQuestionsIds != null){
                    TextView votersTV = mView.findViewById(R.id.expandable_supportTV);
                    ImageView votersIV = mView.findViewById(R.id.expandable_supportIV);

                    View.OnClickListener votersOnClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent votersIntent = new Intent(mContext, VotersListActivity.class);
                            Log.d(TAG, DocReferences.getVotersRef("gVw7dUkuw3SSZSYRXe8s", CurrentFragment.mCurrentSessionId,
                                    mCurrentAgendaId, mQuestionsIds.get(0)).getPath());
                            votersIntent.putExtra(VotersListActivity.VOTERS_REF_STRING,
                                    DocReferences.getVotersRef("gVw7dUkuw3SSZSYRXe8s", CurrentFragment.mCurrentSessionId,
                                            mCurrentAgendaId, mQuestionsIds.get(0)).getPath());
                            mContext.startActivity(votersIntent);
                        }
                    };

                    votersIV.setOnClickListener(votersOnClickListener);
                    votersTV.setOnClickListener(votersOnClickListener);

                }
            }
        };
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, int position) {
        agendaID = mAgendaIds.get(position);
        final Agenda agenda = mAgendas.get(agendaID);
        holder.mTitle.setText(agenda.getTitle());
        //TODO:Change this line to get from server
        holder.mCategoryIcon.setImageResource(getDrawable("services"));
        holder.mRequestScore.setText(mAgendaScore.get(mAgendaIds.get(position)).toString());
        //TODO USE RETURN FROM CLOCK TO STOP SHIT
        mTimers.add(TimeUtils.clock(holder.mTime, mCurrentSession, mContext));
        holder.mVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUnfoldableContent(agenda, agendaID);
                mUnfoldableView.unfold(view, mDetailsLayout);

            }
        });


    }
    @Override
    public int getItemCount() {
        return mAgendaIds.size();
    }

    private void changeUnfoldableContent(Agenda agenda, String agendaId){
        TextView titleTV = mView.findViewById(R.id.expandable_titleContentTV);
        TextView descriptionTV = mView.findViewById(R.id.expandable_contentTV);
        TextView timeTV = mView.findViewById(R.id.expandable_timerTV);
        TextView requestScoreTV = mView.findViewById(R.id.expandable_supportTV);

        titleTV.setText(agenda.getTitle());
        descriptionTV.setText(agenda.getContent());
        requestScoreTV.setText(mAgendaScore.get(agendaId).toString());
        fillUser(agenda.getSuggestedByRef());

        mUnfoldableTimer = TimeUtils.clock(timeTV, mCurrentSession, mContext);


        mCurrentAgendaId = agendaId;
        //TODO CHECK LAST ITEM CLICKED BEFORE RELOADING DATA
        //IF CLICK IS DIFF
        if(mQuestionsLR != null) //catches the first run
            mQuestionsLR.remove();
        //TODO REMOVE STATIC ASSOCIATION REFERENCE
        if(CurrentFragment.mCurrentSessionId != null)// should'nt happen, but just to be sure
            mQuestionsLR = VotesHelper.getQuestions(FirebaseFirestore.getInstance(),"gVw7dUkuw3SSZSYRXe8s",
                    CurrentFragment.mCurrentSessionId, agendaId).addSnapshotListener(mQuestionsEL);


    }
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
    public void initStuff(){
        List<String> iconsList = Arrays.asList("services", "cleaning", "gardening", "security");
        mIconsDrawable = new HashMap<>();
        mIconsDrawablePaths = new HashMap<>();

        for (String icon:
             iconsList) {
            mIconsDrawablePaths.put(icon, "ic_icones_"+icon+"_white");
            int imageResource = mContext.getResources()
                                        .getIdentifier(mIconsDrawablePaths.get(icon), "drawable", mContext.getPackageName());
            mIconsDrawable.put(icon, imageResource);
        }
    }

    public int getDrawable(String icon){
        return mIconsDrawable.get(icon);
    }

    /**
     * Class to serve as ViewHolder for a Request model in this adapter
     */
    class RequestViewHolder extends RecyclerView.ViewHolder{
        CardView mVote;
        TextView mTitle;
        TextView mTime;
        TextView mRequestScore;
        ImageView mCategoryIcon;


        RequestViewHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.titleTV);
            mTime = view.findViewById(R.id.timeTV);
            mVote =  view.findViewById(R.id.cardVote);
            mRequestScore = view.findViewById(R.id.budgetTotalAppliedTV);
            mCategoryIcon = view.findViewById(R.id.category_IV);
        }

        @Override
        public String toString(){
            return "";
        }
    }

}
