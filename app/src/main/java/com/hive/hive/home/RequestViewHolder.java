package com.hive.hive.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;

/**
 * Created by vplentz on 11/01/18.
 */

public class RequestViewHolder extends RecyclerView.ViewHolder {

    private TextView reqTitleTV;
    private TextView reqMessageTV;
    private TextView commentsTV;
    private TextView likesTV;
    private ImageView reqIV;
    public RequestViewHolder(View itemView) {
        super(itemView);
        //finding fields
        reqTitleTV = itemView.findViewById(R.id.reqTitleTV);
        reqMessageTV = itemView.findViewById(R.id.reqMessageTV);
        commentsTV = itemView.findViewById(R.id.commentsTV);
        likesTV = itemView.findViewById(R.id.likesTV);
        reqIV = itemView.findViewById(R.id.reqIV);
    }

    public TextView getReqTitleTV() {
        return reqTitleTV;
    }

    public TextView getReqMessageTV() {
        return reqMessageTV;
    }

    public ImageView getReqIV() {
        return reqIV;
    }

    public TextView getCommentsTV() {
        return commentsTV;
    }

    public TextView getLikesTV() {
        return likesTV;
    }
}
