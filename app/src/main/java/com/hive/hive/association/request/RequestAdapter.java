package com.hive.hive.association.request;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.home.RequestViewHolderOld;
import com.hive.hive.model.association.Request;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vplentz on 04/02/18.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

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

        Request request = mRequests.get(mIds.get(position));

        holder.mItem = request;

        holder.mUserAvatar.setImageResource(R.drawable.ic_profile_photo);
        holder.mUserName.setText("Vitor Plantas");
        holder.mRequestTitle.setText(request.getTitle());
        holder.mRequestContent.setText(request.getContent());
        holder.mNumberOfSupports.setText(String.valueOf(request.getScore()));


    }
    @Override
    public int getItemCount() {
        return mRequests.size();
    }


    /**
     * Class to serve as ViewHolder for a Request model in this adapter
     */
    class RequestViewHolder extends RecyclerView.ViewHolder{

        final View mView;

        final ImageView mUserAvatar;
        final TextView mUserName;
        final TextView mUserLeaderboardPostion;
        final TextView mRequestTitle;
        final TextView mRequestCost;
        final ImageView mRequestCategory;
        final TextView mRequestContent;
        final TextView mNumberOfComments;
        final TextView mNumberOfSupports;

        Request mItem;

        RequestViewHolder(View view){
            super(view);

            mView = view;

            mUserAvatar = view.findViewById(R.id.request_author_photo_iv);
            mUserName = view.findViewById(R.id.request_author_name_tv);
            mUserLeaderboardPostion = view.findViewById(R.id.request_leaderboard_position_tv);
            mRequestTitle = view.findViewById(R.id.request_title_tv);
            mRequestCost = view.findViewById(R.id.request_cost_tv);
            mRequestCategory = view.findViewById(R.id.request_budget_category_iv);
            mRequestContent = view.findViewById(R.id.request_content_tv);
            mNumberOfComments = view.findViewById(R.id.request_number_of_comments_tv);
            mNumberOfSupports = view.findViewById(R.id.request_number_of_supports_tv);

        }

        @Override
        public String toString(){
            return "";
        }
    }

}
