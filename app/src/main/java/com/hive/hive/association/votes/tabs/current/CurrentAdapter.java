package com.hive.hive.association.votes.tabs.current;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.home.AssociationViewHolder;
import com.hive.hive.home.RequestViewHolderOld;
import com.hive.hive.model.association.Vote;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;


public class CurrentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =  CurrentAdapter.class.getSimpleName();

    private ArrayList<Vote> mCurrentVotes;
    private int mNumberItems;
    private final int REQUEST = 0, ASSOCIATIONPOST = 1, CURRENT_VOTE = 2;

    // Folding motion controllers
    private View.OnClickListener defaultRequestBtnClickListener;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    ViewGroup mViewGroup;

    public CurrentAdapter(ArrayList<Vote> currentVotes, int numListItems) {
        mCurrentVotes = currentVotes;
        mNumberItems = numListItems;
    }


    //-- Data


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mViewGroup  = viewGroup;
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder =  null;
        switch (viewType) {
            case REQUEST:
                View viewRequisition = inflater.inflate(R.layout.item_request, viewGroup, false);
                viewHolder = new RequestViewHolderOld(viewRequisition);
                break;
            case ASSOCIATIONPOST:
                View viewAPost = inflater.inflate(R.layout.item_association, viewGroup, false);
                viewHolder = new AssociationViewHolder(viewAPost);
                break;
            case CURRENT_VOTE:
                FoldingCell viewAVote;
                viewAVote = (FoldingCell) inflater.inflate(R.layout.cell, viewGroup, false);
                viewHolder = new VotesViewHolder(viewAVote);
                break;

        }
        return viewHolder;
    }

    // Fill View Recycler View Activities
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {

            case ASSOCIATIONPOST:
                AssociationViewHolder associationViewHolder = (AssociationViewHolder) viewHolder;
                configureViewHolder2(associationViewHolder);
                break;
            case CURRENT_VOTE:
                VotesViewHolder votesViewHolder = (VotesViewHolder) viewHolder;
                Vote vote;
                vote = mCurrentVotes.get(position);



                if(votesViewHolder.fc.getTag() == null) {
                    votesViewHolder = (VotesViewHolder) viewHolder;

                }else{

                    if(unfoldedIndexes.contains(position)){
                        VotesViewHolder.fc.unfold(false);
                        registerUnfold(position);
                    }else{
                        VotesViewHolder.fc.fold(false);
                        registerFold(position);
                    }

                    votesViewHolder = (VotesViewHolder) VotesViewHolder.fc.getTag();
                }
                votesViewHolder.fc.fold(false);


                // set custom btn handler for list item from that item
                if (vote.getRequestBtnClickListener() != null) {
                    votesViewHolder.contentRequestBtn.setOnClickListener(vote.getRequestBtnClickListener());
                } else {
                    // (optionally) add "default" handler if no handler found in item
                    votesViewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
                }

                break;
        }
    }




    @Override
    public int getItemViewType(int position) {
            return CURRENT_VOTE;
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public Context getContext() {
        return getContext();
    }

    public static class VotesViewHolder extends RecyclerView.ViewHolder {
        public static FoldingCell fc;

        // Need to declare and link all the view in Foding Cells

        ImageButton contentRequestBtn;

        public VotesViewHolder(View itemView) {
            super(itemView);

            fc = (FoldingCell)itemView.findViewById(R.id.currentVoteFC);
            fc.initialize(1000, Color.DKGRAY, 4);
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VotesViewHolder.fc.toggle(false);



                }
            });
            contentRequestBtn = (ImageButton) itemView.findViewById(R.id.choseVoteBT);

        }


    }

    private void configureViewHolder2(AssociationViewHolder associationViewHolder) {

    }


    //Need to check this registry Toggle stuff
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }


    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }


}
