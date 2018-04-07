package com.hive.hive.store;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.model.association.Request;

import java.util.List;

/**
 * Created by vplentz on 11/01/18.
 */

public class RecyclerViewStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> items;
    private final int STORE = 0;

    public RecyclerViewStoreAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case STORE:
                FeedViewHolderOld feedViewHolderOld = (FeedViewHolderOld) viewHolder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(items.get(position) instanceof Request)
            return STORE;
        else
            return STORE;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder =  null;
        switch (viewType) {
            case STORE:
                View viewRequisition = inflater.inflate(R.layout.item_store, viewGroup, false);
                viewHolder = new FeedViewHolderOld(viewRequisition);
                break;
        }
        return viewHolder;
    }


    public class FeedViewHolderOld extends RecyclerView.ViewHolder {

        public FeedViewHolderOld(View itemView) {
            super(itemView);
        }
    }


}
