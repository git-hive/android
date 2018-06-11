package com.hive.hive.home;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Report;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Session;
import com.hive.hive.model.forum.ForumPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vplentz on 11/01/18.
 */

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = this.getClass().getName();

    private List<Object> items;
    private final int SESSION = 0, REPORT = 1;

    public RecyclerViewHomeAdapter(List<Object> items) {
        this.items = items;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = null;
        switch (viewType) {
            case SESSION:
                View viewRequisition = inflater.inflate(R.layout.item_session, viewGroup, false);
                viewHolder = new SessionViewHolder(viewRequisition);
                break;
            case REPORT:
                View viewAPost = inflater.inflate(R.layout.item_reports, viewGroup, false);
                viewHolder = new ReportsViewHolder(viewAPost);
                break;
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

//        populateItems();

        switch (viewHolder.getItemViewType()) {
            case SESSION:
                SessionViewHolder sessionViewHolder = (SessionViewHolder) viewHolder;
                configureSessionsViewHolder(sessionViewHolder, position); //configure as session
                break;
            case REPORT:
                ReportsViewHolder reportsViewHolder = (ReportsViewHolder) viewHolder;
                configureReportsViewHolder(reportsViewHolder, position); //configure as reports
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Session)
            return SESSION;
        else if (items.get(position) instanceof Report)
            return REPORT;
        return -1;
    }


    private void configureSessionsViewHolder(SessionViewHolder sessionViewHolder, int position) {
        Session session = (Session) items.get(position);
//        if (session != null) {
//            sessionViewHolder
//            //vh1.getLabel1().setText("Name: " + user.name);
//            //vh1.getLabel2().setText("Hometown: " + user.hometown);
//        }
    }

    private void configureReportsViewHolder(ReportsViewHolder reportsViewHolder, int position) {
        Report report = (Report) items.get(position);
        if(report != null){
            reportsViewHolder.reportDescription.setText(report.getDescription());
        }
    }

//    public void populateItems() {
//        if (mAgendas.first.size() == 0)
//            return;
//        items = new ArrayList<>();
//        for (Object key :
//                mAgendas.first) {
//            items.add(mAgendas.second.get(key));
//        }
//    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {

        public SessionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ReportsViewHolder extends RecyclerView.ViewHolder {
        TextView reportDescription;
        public ReportsViewHolder(View itemView) {
            super(itemView);
            reportDescription = itemView.findViewById(R.id.reportDescription);
        }
    }

}
