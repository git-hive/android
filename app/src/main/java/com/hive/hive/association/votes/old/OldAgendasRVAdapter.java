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
import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.model.association.Agenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OldAgendasRVAdapter extends RecyclerView.Adapter<AgendasViewHolder>{
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas;

    private Context mContext;

    // Local for now
    private HashMap<String, String> mIconsDrawablePaths;
    private HashMap<String, Integer> mIconsDrawable;

    //-- Views
    private UnfoldableView mUnfoldableView;
    private FrameLayout mDetailsLayout;
    private View mView;

    public OldAgendasRVAdapter(Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas,
                               Context mContext, UnfoldableView mUnfoldableView, FrameLayout mDetailsLayout, View mView) {
        this.mAgendas = mAgendas;
        this.mContext = mContext;
        this.mUnfoldableView = mUnfoldableView;
        this.mDetailsLayout = mDetailsLayout;
        this.mView = mView;
    }

    @Override
    public AgendasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_cell, parent, false);

        // Init locally
        initPossibleCategoryIcons();


        return new AgendasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AgendasViewHolder holder, int position) {
        final String[] agendaID = {mAgendas.first.get(position)};

        final Agenda agenda = mAgendas.second.get(agendaID[0]);

        //populate views
        holder.getmTitle().setText(agenda.getTitle());
        //TODO:Change this line to get from server
        holder.getmCategoryIcon().setImageResource(getDrawable("services"));

        holder.getmTime().setVisibility(View.GONE);
        holder.getmTimeIV().setVisibility(View.GONE);

        holder.getmVote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clearQuestions();
                agendaID[0] = mAgendas.first.get(position);
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
        //requestScoreTV.setText(mAgendaScore.get(agendaId).toString());
        //fillUser(agenda.getSuggestedByRef());


        //sets the current agenda, ExpandableListAdapter depends on it
        //mCurrentAgendaId = agendaId;

//        //TODO REMOVE STATIC ASSOCIATION REFERENCE
//        if(CurrentFragment.mCurrentSessionId != null)// should'nt happen, but just to be sure
//            mQuestionsLR = VotesHelper.getQuestions(FirebaseFirestore.getInstance(),"gVw7dUkuw3SSZSYRXe8s",
//                    CurrentFragment.mCurrentSessionId, agendaId).addSnapshotListener(mQuestionsEL);


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
