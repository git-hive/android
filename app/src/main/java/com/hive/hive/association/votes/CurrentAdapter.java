package com.hive.hive.association.votes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;


public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.VotesViewHolder> {
    private static final String TAG =  CurrentAdapter.class.getSimpleName();
    private int mNumberItems;

    public CurrentAdapter(int numListItems) {
        mNumberItems = numListItems;
    }

    //-- Data

    @Override
    public VotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_current_vote;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        VotesViewHolder viewHolder = new VotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VotesViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class VotesViewHolder extends RecyclerView.ViewHolder {
        TextView listItemCurrentVote;
        public VotesViewHolder(View itemView){
            super(itemView);
            listItemCurrentVote = (TextView) itemView.findViewById(R.id.currentVoteTV);
        }

        void bind(int listIndex) {
            listItemCurrentVote.setText(String.valueOf(listIndex));
        }
    }
}
