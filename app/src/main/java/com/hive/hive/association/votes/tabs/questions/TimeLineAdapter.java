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
    private ArrayList<Boolean> mStatusList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(HashMap<Integer, ArrayList<String> > feedList, ArrayList<Boolean> statusList, Orientation orientation, boolean withLinePadding) {
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

        Boolean currentStatus = mStatusList.get(position);

        if(currentStatus == Boolean.TRUE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

//        if(holder.mDate ==null) {
//            holder.mDate = null;
//        }else if(!timeLineModel.getDate().isEmpty()) {
//            holder.mDate.setVisibility(View.VISIBLE);
//            //holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy"));
//        }
//        else
//            holder.mDate.setVisibility(View.GONE);
//
//        if(holder.mMessage != null)
//            holder.mMessage.setText(timeLineModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}
