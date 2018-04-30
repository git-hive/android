package com.hive.hive.association.votes.old;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.utils.VotingUtils;

import java.util.ArrayList;

import java.util.HashMap;


public class OldAgendasRVAdapter extends RecyclerView.Adapter<AgendasViewHolder>{
    private Pair<ArrayList<DocumentSnapshot>, HashMap<String, Agenda>> mAgendas;
    private HashMap<String, String> mAgendaAndSessionIds;
    private Context mContext;

    // Local for now
    private HashMap<String, String> mIconsDrawablePaths;
    private HashMap<String, Integer> mIconsDrawable;

    //-- Views
    private UnfoldableView mUnfoldableView;
    private FrameLayout mDetailsLayout;
    private View mView;

    public OldAgendasRVAdapter(Pair<ArrayList<DocumentSnapshot>, HashMap<String, Agenda>> mAgendas, HashMap<String, String> agendaAndSessionIds,
                               Context mContext, UnfoldableView mUnfoldableView, FrameLayout mDetailsLayout, View mView) {
        this.mAgendas = mAgendas;
        this.mAgendaAndSessionIds = agendaAndSessionIds;
        this.mContext = mContext;
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
        final String[] agendaID = {mAgendas.first.get(position).getId()};

        final Agenda agenda = mAgendas.second.get(agendaID[0]);

        //populate views
        holder.getmTitle().setText(agenda.getTitle());
        //TODO:Change this line to get from server
        holder.getmCategoryIcon().setImageResource(VotingUtils.getDrawable("services", mIconsDrawable));

        holder.getmTime().setVisibility(View.GONE);
        holder.getmTimeIV().setVisibility(View.GONE);

        holder.getmVote().setOnClickListener(new View.OnClickListener() {
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

        //load suggested by info
        VotingUtils.fillUnfoldableUser(agenda.getSuggestedByRef(), mView);

        //TODO REMOVE STATIC ASSOCIATION REFERENCE

        OldAgendasFirebaseHandle.getPastQuestions("gVw7dUkuw3SSZSYRXe8s", mAgendaAndSessionIds.get(agendaId), agendaId);

    }


}
