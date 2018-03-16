package com.hive.hive.association.votes.tabs.questions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;



/**
 * Birck.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.text_timeline_date)
    TextView mDate;
//    @BindView(R.id.text_timeline_title)
    TextView mMessage;
//    @BindView(R.id.time_marker)
    TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

//        ButterKnife.bind(this, itemView);
        if(mTimelineView != null) {
            mTimelineView.initLine(viewType);
        }
    }
}
