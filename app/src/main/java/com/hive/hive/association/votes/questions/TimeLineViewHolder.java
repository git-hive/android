package com.hive.hive.association.votes.questions;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.github.vipulasri.timelineview.TimelineView;
import com.hive.hive.R;


/**
 * Birck.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    //@BindView(R.id.text_timeline_date)
    //TextView mDate;
    //@BindView(R.id.text_timeline_title)
    //TextView mMessage;
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
