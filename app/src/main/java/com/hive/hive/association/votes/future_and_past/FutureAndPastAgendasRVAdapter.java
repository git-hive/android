package com.hive.hive.association.votes.future_and_past;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.association.votes.future_and_past.future.FutureAgendasFirebaseHandle;
import com.hive.hive.association.votes.future_and_past.future.FutureFragment;
import com.hive.hive.association.votes.future_and_past.old.OldAgendasFirebaseHandle;
import com.hive.hive.association.votes.future_and_past.old.OldFragment;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.utils.VotingUtils;
import java.util.ArrayList;
import java.util.HashMap;


public class FutureAndPastAgendasRVAdapter extends RecyclerView.Adapter<AgendasViewHolder>{
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas;
    private HashMap<String, String> mScores;
    //context and fragment
    private Context mContext;
    private OldFragment mOldFragment;
    private FutureFragment mFutureFragment;
    // Local for now
    private HashMap<String, String> mIconsDrawablePaths;
    private HashMap<String, Integer> mIconsDrawable;

    //-- Views
    private UnfoldableView mUnfoldableView;
    private FrameLayout mDetailsLayout;
    private View mView;

    public FutureAndPastAgendasRVAdapter(Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas, HashMap<String, String> scores, Context mContext, OldFragment fragment,
                                         UnfoldableView mUnfoldableView, FrameLayout mDetailsLayout, View mView) {
        this.mAgendas = mAgendas;
        this.mScores = scores;
        this.mContext = mContext;
        this.mOldFragment = fragment;
        this.mUnfoldableView = mUnfoldableView;
        this.mDetailsLayout = mDetailsLayout;
        this.mView = mView;
        mIconsDrawable= new HashMap<>();
        mIconsDrawablePaths = new HashMap<>();
    }

    public FutureAndPastAgendasRVAdapter(Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas, HashMap<String, String> mScores, Context mContext, FutureFragment mFutureFragment,
                                         UnfoldableView mUnfoldableView, FrameLayout mDetailsLayout, View mView) {
        this.mAgendas = mAgendas;
        this.mScores = mScores;
        this.mContext = mContext;
        this.mFutureFragment = mFutureFragment;
        this.mUnfoldableView = mUnfoldableView;
        this.mDetailsLayout = mDetailsLayout;
        this.mView = mView;
        mIconsDrawable= new HashMap<>();
        mIconsDrawablePaths = new HashMap<>();
    }

    @Override
    public AgendasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_cell, parent, false);

        // Init locally
        VotingUtils.initPossibleCategoryIcons(mContext, mIconsDrawablePaths, mIconsDrawable);


        return new AgendasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AgendasViewHolder holder, int position) {
        final String[] agendaID = {mAgendas.first.get(position)};

        final Agenda agenda = mAgendas.second.get(agendaID[0]);

        //populate views
        holder.getmTitle().setText(agenda.getTitle());
        holder.getmRequestScore().setText(mScores.get(agendaID[0]));
        //TODO:Change this line to get from server
        holder.getmCategoryIcon().setImageResource(VotingUtils.getDrawable("services", mIconsDrawable));

        //remove unnused views
        holder.getmTime().setVisibility(View.GONE);
        holder.getmTimeIV().setVisibility(View.GONE);

        //set onclicks
        holder.getmAgendaCV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clearQuestions();
                changeUnfoldableContent(agenda, agendaID[0]);
                mUnfoldableView.unfold(view, mDetailsLayout);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mAgendas.first.size();
    }

    private void changeUnfoldableContent(Agenda agenda, String agendaId){
        //load views
        TextView titleTV = mView.findViewById(R.id.expandable_titleContentTV);
        TextView descriptionTV = mView.findViewById(R.id.expandable_contentTV);

        TextView timeTV = mView.findViewById(R.id.expandable_timerTV);
        TextView requestScoreTV = mView.findViewById(R.id.expandable_supportTV);

        ImageView timeIV = mView.findViewById(R.id.expandable_timerIV);

        timeTV.setVisibility(View.GONE);
        timeIV.setVisibility(View.GONE);
        //set agenda texts
        titleTV.setText(agenda.getTitle());
        descriptionTV.setText(agenda.getContent());
        requestScoreTV.setText(mScores.get(agendaId));
        //load suggested by info
        VotingUtils.fillUnfoldableUser(agenda.getSuggestedByRef(), mView);

        //TODO REMOVE STATIC ASSOCIATION REFERENCE
        if(mOldFragment != null)
            OldAgendasFirebaseHandle.getPastQuestions(agenda.getSessionRef(), agendaId, mOldFragment);
        else
            FutureAgendasFirebaseHandle.getFutureQuestions(agenda.getSessionRef(), agendaId, mFutureFragment);
    }


}
