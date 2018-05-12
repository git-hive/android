package com.hive.hive.association.votes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;

public class AgendasViewHolder extends RecyclerView.ViewHolder{
    CardView mAgendaCV;
    TextView mTitle;
    TextView mTime;
    TextView mRequestScore;
    ImageView mCategoryIcon;
    ImageView mTimeIV;



    public AgendasViewHolder(View view){
        super(view);
        mTitle = view.findViewById(R.id.titleTV);
        mTime = view.findViewById(R.id.timeTV);
        mAgendaCV =  view.findViewById(R.id.cardVote);
        mRequestScore = view.findViewById(R.id.budgetTotalAppliedTV);
        mCategoryIcon = view.findViewById(R.id.category_IV);
        mTimeIV = view.findViewById(R.id.timeIV);
    }

    public CardView getmAgendaCV() {
        return mAgendaCV;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public TextView getmTime() {
        return mTime;
    }

    public ImageView getmTimeIV() {
        return mTimeIV;
    }

    public TextView getmRequestScore() {
        return mRequestScore;
    }

    public ImageView getmCategoryIcon() {
        return mCategoryIcon;
    }

    @Override
    public String toString(){
        return "";
    }
}

