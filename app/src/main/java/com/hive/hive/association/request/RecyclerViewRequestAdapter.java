package com.hive.hive.association.request;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.home.RequestViewHolder;
import com.hive.hive.model.association.Request;

import java.util.HashMap;

/**
 * Created by vplentz on 04/02/18.
 */

public class RecyclerViewRequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private HashMap<String, Request> requests;
    public RecyclerViewRequestAdapter(HashMap<String, Request> requests){
        this.requests = requests;
    }
    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.getReqTitleTV().setText(requests.get(""+position).getTitle());
        holder.getReqMessageTV().setText(requests.get(""+position).getContent());
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }
}
