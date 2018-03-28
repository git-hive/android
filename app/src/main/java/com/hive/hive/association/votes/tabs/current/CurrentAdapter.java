package com.hive.hive.association.votes.tabs.current;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.hive.hive.R;
import com.hive.hive.model.association.Vote;

import java.util.ArrayList;



public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.RequestViewHolder> {

    //-- Data
    private ArrayList<Vote> mVotes;
    private  UnfoldableView mUnfoldableView;
    private  FrameLayout mDetailsLayout;
    public CurrentAdapter(ArrayList<Vote> mVotes, UnfoldableView unfoldableView, FrameLayout detailsLayout){
        this.mVotes = mVotes;
        this.mUnfoldableView = unfoldableView;
        this.mDetailsLayout = detailsLayout;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_cell, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {

        Vote request = mVotes.get(position);
        holder.mTitle.setText("Titulo da Pauta");
        holder.mVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUnfoldableView.unfold(view, mDetailsLayout);
            }
        });


    }
    @Override
    public int getItemCount() {
        return mVotes.size();
    }


    /**
     * Class to serve as ViewHolder for a Request model in this adapter
     */
    class RequestViewHolder extends RecyclerView.ViewHolder{
        final CardView mVote;
        final TextView mTitle;


        RequestViewHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.budgetNameTV);
            mVote =  view.findViewById(R.id.cardVote);
        }

        @Override
        public String toString(){
            return "";
        }
    }

}
