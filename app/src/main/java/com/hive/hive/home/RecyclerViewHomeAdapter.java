package com.hive.hive.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.ForumPost;

import java.util.List;

/**
 * Created by vplentz on 11/01/18.
 */

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> items;
    private final int REQUEST = 0, ASSOCIATIONPOST = 1;

    public RecyclerViewHomeAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case REQUEST:
                RequestViewHolder requestViewHolder = (RequestViewHolder) viewHolder;
                configureViewHolder1(requestViewHolder, position);
                break;
            case ASSOCIATIONPOST:
                AssociationViewHolder associationViewHolder = (AssociationViewHolder) viewHolder;
                configureViewHolder2(associationViewHolder);
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
            return REQUEST;
        else if(items.get(position) instanceof ForumPost)
            return ASSOCIATIONPOST;
        return -1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder =  null;
        switch (viewType) {
            case REQUEST:
                View viewRequisition = inflater.inflate(R.layout.card_req, viewGroup, false);
                viewHolder = new RequestViewHolder(viewRequisition);
                break;
            case ASSOCIATIONPOST:
                View viewAPost = inflater.inflate(R.layout.card_assoc, viewGroup, false);
                viewHolder = new AssociationViewHolder(viewAPost);
                break;
        }
        return viewHolder;
    }

    private void configureViewHolder1(RequestViewHolder associationViewHolder, int position) {
        Request request = (Request) items.get(position);
        if (request != null) {
            //vh1.getLabel1().setText("Name: " + user.name);
            //vh1.getLabel2().setText("Hometown: " + user.hometown);
        }
    }

    private void configureViewHolder2(AssociationViewHolder associationViewHolder) {

    }
}
