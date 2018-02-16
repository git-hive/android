package com.hive.hive.association.request.comments;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.AssociationComment;
import com.hive.hive.model.association.Request;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vplentz on 15/02/18.
 */

public class CommentaryAdapter extends RecyclerView.Adapter<CommentaryAdapter.CommentaryViewHolder>  {
    private HashMap<String,  AssociationComment> mComments;
    private ArrayList<String> mIds;

    public CommentaryAdapter(HashMap<String, AssociationComment> comments, ArrayList<String> ids){
        this.mComments = comments;
        this.mIds = ids;
    }
    @Override
    public CommentaryAdapter.CommentaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false);

        return new CommentaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentaryAdapter.CommentaryViewHolder holder, int position) {
        final AssociationComment comment = mComments.get(mIds.get(position));
        //TODO should be replaced by real user image
        holder.avatarIV.setImageResource(R.drawable.ic_profile_photo);
        //TODO should be replaced by real author name
        holder.authorTV.setText("AUTHOR");
        holder.contentTV.setText(comment.getContent());
        holder.supportTV.setText(comment.getScore()+"");
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class CommentaryViewHolder extends RecyclerView.ViewHolder{
        final ImageView avatarIV;
        final TextView contentTV;
        final TextView authorTV;
        final TextView supportTV;
        public CommentaryViewHolder(View itemView) {
            super(itemView);
            avatarIV = itemView.findViewById(R.id.commentAvatarIV);
            authorTV = itemView.findViewById(R.id.commentAuthorTV);
            contentTV = itemView.findViewById(R.id.commentContentTV);
            supportTV = itemView.findViewById(R.id.commentsSupportsTV);
        }
    }
}
