package com.hive.hive.association.request;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.home.RequestViewHolder;
import com.hive.hive.model.association.Request;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vplentz on 04/02/18.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {

    //-- Data
    private HashMap<String,  Request> mRequests;
    private ArrayList<String> mIds;

    public RequestAdapter(HashMap<String, Request> requests, ArrayList<String> mIds){
        this.mRequests = requests;
        this.mIds = mIds;
    }
    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        String id = mIds.get(position);
        holder.getReqTitleTV().setText(mRequests.get(id).getTitle());
        holder.getReqMessageTV().setText(mRequests.get(id).getContent());

    }
    @Override
    public int getItemCount() {
        return mRequests.size();
    }
}
