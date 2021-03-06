package com.hive.hive.association.votes.question_answering.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;

import com.hive.hive.R;
import com.hive.hive.association.votes.question_answering.header_model.OrderStatus;
import com.hive.hive.association.votes.question_answering.header_model.Orientation;
import com.hive.hive.utils.VectorDrawableUtils;
import com.hive.hive.model.association.Question;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Birck.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private String TAG = TimeLineAdapter.class.getSimpleName();
    private HashMap<String, Question> mQuestions;
    private ArrayList<OrderStatus> mStatusList;
    public ArrayList<Integer> mStatusListValue;
    private Context mContext;
    private Orientation mOrientation;
    private LayoutInflater mLayoutInflater;



    public TimeLineAdapter(HashMap<String, Question> mQuestions, ArrayList<OrderStatus> mStatusList, ArrayList<Integer> mStatusListValue, Context mContext) {
        this.mQuestions = mQuestions;
        this.mStatusList = mStatusList;
        this.mStatusListValue = mStatusListValue;
        this.mContext = mContext;
        this.mOrientation = Orientation.HORIZONTAL;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = null;

        if(mOrientation == Orientation.HORIZONTAL) {
            view = mLayoutInflater.inflate(R.layout.item_timeline_horizontal, parent, false);
        }

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        OrderStatus currentStatus = mStatusList.get(position);

        if(currentStatus == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorOrange));
        } else if(currentStatus == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_inactive), ContextCompat.getColor(mContext, R.color.colorOrange));
        }else{
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorOrange));
        }
        Log.d(TAG, "position : "+position);
    }

    @Override
    public int getItemCount() {
        return (mQuestions!=null? mQuestions.size():0);
    }

    public void completePoint(int position){
        mStatusList.set(position, OrderStatus.COMPLETED);
    }

    public void activePoint(int position){
        mStatusList.set(position, OrderStatus.ACTIVE);
    }

    public void inactivePoint(int position){
        mStatusList.set(position, OrderStatus.INACTIVE);
    }

    public boolean checkAnswers(){
        for (Integer value:
             mStatusListValue) {
            if(value == -1)
                return false;
        }
        return true;
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_marker)
        TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            // System.out.println(" WOW ______________________________");
            ButterKnife.bind(this, itemView);
            if(mTimelineView != null) {
                //   System.out.println("NEVER ENTER ______________________________");
                mTimelineView.initLine(viewType);
            }
        }
    }

}
