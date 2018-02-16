package com.hive.hive.association.votes;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.home.AssociationViewHolder;
import com.hive.hive.home.RequestViewHolderOld;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Vote;
import com.hive.hive.model.forum.ForumPost;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        switch (viewHolder.getItemViewType()) {
//            case REQUEST:
//                RequestViewHolderOld requestViewHolderOld = (RequestViewHolderOld) viewHolder;
//                configureViewHolder1(requestViewHolderOld, position);
//                break;
            case ASSOCIATIONPOST:
                AssociationViewHolder associationViewHolder = (AssociationViewHolder) viewHolder;
                configureViewHolder2(associationViewHolder);
                break;
            case CURRENT_VOTE:
                VotesViewHolder votesViewHolder = (VotesViewHolder) viewHolder;
                Vote vote;
                vote = mCurrentVotes.get(position);
                votesViewHolder.fc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v = (FoldingCell) v;
                        ((FoldingCell) v).toggle(true);
                    }
                });


                if(votesViewHolder.fc.getTag() == null) {

//                vote.setWeight((float) 666.0);
//                vote.setVotingOption(satan);
//                vote.setCreatedAt(666);
//                vote.setUpdatedAt(666);
//                vote.setId(satan);

                    votesViewHolder = (VotesViewHolder) viewHolder;
                    //votesViewHolder.title.setText(vote.getVotingOption());
                    //votesViewHolder.weight.setText(Float.toString(vote.getWeight()));
                    //votesViewHolder.address.setText(vote.getId());
                    //votesViewHolder.time.setText(Integer.toString((int) vote.getCreatedAt()));

                }else{

                    if(unfoldedIndexes.contains(position)){
                        VotesViewHolder.fc.unfold(true);
                    }else{
                        VotesViewHolder.fc.fold(true);
                    }

                    votesViewHolder = (VotesViewHolder) VotesViewHolder.fc.getTag();
                }

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
//        if(mCurrentVotes.get(position) instanceof Request)
//            return REQUEST;
//        else if(mCurrentVotes.get(position) instanceof ForumPost)
//            return ASSOCIATIONPOST;
//        else if(mCurrentVotes.get(position) instanceof Vote)
            return CURRENT_VOTE;
        //return -1;
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
        CardView cv;

        TextView title;
        TextView address;
        TextView time;
        TextView weight;
        TextView contentRequestBtn;

        public VotesViewHolder(View itemView) {
            super(itemView);

            fc = (FoldingCell)itemView.findViewById(R.id.currentVoteFC);
            fc.initialize(100, Color.DKGRAY, 2);



            //title = (TextView) itemView.findViewById(R.id.title_price);
            //address = (TextView) itemView.findViewById(R.id.title_date_label);
            //time = (TextView) itemView.findViewById(R.id.title_time_label);
            //weight = (TextView) itemView.findViewById(R.id.title_weight);
            contentRequestBtn = (TextView) itemView.findViewById(R.id.content_request_btn);

        }


    }

//    private void configureViewHolder1(RequestViewHolderOld associationViewHolder, int position) {
//        Request request = (Request) mCurrentVotes.get(position);
//        if (request != null) {
//            //vh1.getLabel1().setText("Name: " + user.name);
//            //vh1.getLabel2().setText("Hometown: " + user.hometown);
//        }
//    }

    private void configureViewHolder2(AssociationViewHolder associationViewHolder) {

    }
    private void configureViewHolder3(VotesViewHolder votesViewHolder, int position) {
        // get item for selected view
        Vote item = mCurrentVotes.get(position);
    }

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
