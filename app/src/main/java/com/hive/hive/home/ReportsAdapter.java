package com.hive.hive.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.Report;

import java.util.ArrayList;

public class ReportsAdapter extends  RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>{
    private ArrayList<Report> mReports;

    public ReportsAdapter(ArrayList<Report> mReports) {
        this.mReports = mReports;
    }

    @Override
    public ReportsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_reports, parent, false);
        return new ReportsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportsViewHolder holder, int position) {
        holder.reportDescription.setText(mReports.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    public class ReportsViewHolder extends RecyclerView.ViewHolder{
        TextView reportDescription;
        public ReportsViewHolder(View itemView) {
            super(itemView);
            reportDescription = itemView.findViewById(R.id.reportDescription);
        }
    }
}
