package com.hive.hive.association.votes.tabs.questions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;

import com.hive.hive.R;
import com.hive.hive.association.votes.tabs.questions.model.OrderStatus;
import com.hive.hive.association.votes.tabs.questions.model.Orientation;
import com.hive.hive.association.votes.tabs.questions.model.TimeLineModel;
import com.hive.hive.association.votes.tabs.questions.utils.VectorDrawableUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Birck.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private HashMap<Integer, ArrayList<String>> mFeedList;
    private ArrayList<OrderStatus> mStatusList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(HashMap<Integer, ArrayList<String> > feedList, ArrayList<OrderStatus> statusList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mStatusList = statusList;
        mOrientation = Orientation.HORIZONTAL;
        mWithLinePadding = withLinePadding;
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
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else if(currentStatus == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_inactive), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }else{
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
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
        return true;
    }

}
