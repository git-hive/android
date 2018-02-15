package com.hive.hive.association.votes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.home.AssociationViewHolder;
import com.hive.hive.home.RequestViewHolderOld;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Vote;
import com.hive.hive.model.forum.ForumPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CurrentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =  CurrentAdapter.class.getSimpleName();

    private List<Object> mCurrentVotes;
    private int mNumberItems;
    private final int REQUEST = 0, ASSOCIATIONPOST = 1, CURRENT_VOTE = 2;


    public CurrentAdapter(List<Object> currentVotes, int numListItems) {
        mCurrentVotes = currentVotes;
        mNumberItems = numListItems;
    }


    //-- Data

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

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
                View viewAVote = inflater.inflate(R.layout.item_current_vote, viewGroup, false);
                viewHolder = new VotesViewHolder(viewAVote);
                break;

        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case REQUEST:
                RequestViewHolderOld requestViewHolderOld = (RequestViewHolderOld) viewHolder;
                configureViewHolder1(requestViewHolderOld, position);
                break;
            case ASSOCIATIONPOST:
                AssociationViewHolder associationViewHolder = (AssociationViewHolder) viewHolder;
                configureViewHolder2(associationViewHolder);
                break;
            case CURRENT_VOTE:
                VotesViewHolder votesViewHolderOld = (VotesViewHolder) viewHolder;
                configureViewHolder3(votesViewHolderOld, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mCurrentVotes.get(position) instanceof Request)
            return REQUEST;
        else if(mCurrentVotes.get(position) instanceof ForumPost)
            return ASSOCIATIONPOST;
        else if(mCurrentVotes.get(position) instanceof Vote)
            return CURRENT_VOTE;
        return -1;
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class VotesViewHolder extends RecyclerView.ViewHolder {
        public VotesViewHolder(View itemView) {
            super(itemView);
        }

    }

    private void configureViewHolder1(RequestViewHolderOld associationViewHolder, int position) {
        Request request = (Request) mCurrentVotes.get(position);
        if (request != null) {
            //vh1.getLabel1().setText("Name: " + user.name);
            //vh1.getLabel2().setText("Hometown: " + user.hometown);
        }
    }

    private void configureViewHolder2(AssociationViewHolder associationViewHolder) {

    }
    private void configureViewHolder3(VotesViewHolder associationViewHolder, int position) {
        Vote vote = (Vote) mCurrentVotes.get(position);
        if (vote != null) {
            //vh1.getLabel1().setText("Name: " + user.name);
            //vh1.getLabel2().setText("Hometown: " + user.hometown);
        }
    }
}
