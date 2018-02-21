package com.hive.hive.association.request.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.model.association.AssociationComment;
import com.hive.hive.model.association.AssociationHelper;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.utils.DocReferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by vplentz on 15/02/18.
 */

public class CommentaryAdapter extends RecyclerView.Adapter<CommentaryAdapter.CommentaryViewHolder>  {
    private HashMap<String,  AssociationComment> mComments;
    private ArrayList<String> mIds;
    private Context mContext;
    private String mRequestId;
    public CommentaryAdapter(Context context, HashMap<String, AssociationComment> comments, ArrayList<String> ids, String requestId){
        this.mContext = context;
        this.mComments = comments;
        this.mIds = ids;
        this.mRequestId = requestId;
    }
    @Override
    public CommentaryAdapter.CommentaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false);

        return new CommentaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentaryAdapter.CommentaryViewHolder holder, final int position) {
        final AssociationComment comment = mComments.get(mIds.get(position));
        shouldFillSupport(holder, mRequestId, mIds.get(position));
        //TODO should be replaced by real user image
        holder.avatarIV.setImageResource(R.drawable.ic_profile_photo);
        //TODO should be replaced by real author name
        holder.authorTV.setText("AUTHOR");
        holder.contentTV.setText(comment.getContent());
        holder.supportTV.setText(comment.getScore()+"");

        holder.supportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position));
            }
        });

        holder.supportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    private void shouldFillSupport(final CommentaryViewHolder holder, String requestId, String commentId){
        //if exists support, then should be IV filled
        AssociationHelper.getRequestCommentSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                requestId, commentId, FirebaseAuth.getInstance().getUid() )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                            holder.supportIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_support_filled));
                        else
                            holder.supportIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_support_borderline));
                    }
                });
    }
    private void scoreClick(final CommentaryViewHolder holder, final String commentId){
        AssociationHelper.getRequestCommentSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                mRequestId, commentId, FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //if support already exists, should delete it
                        if(documentSnapshot.exists()) {
                            AssociationHelper.deleteRequestCommentSupport(FirebaseFirestore.getInstance(),
                                    "gVw7dUkuw3SSZSYRXe8s", mRequestId, commentId, FirebaseAuth.getInstance().getUid());
                        }else {// else should add it
                            DocumentReference userRef = DocReferences.getUserRef();
                            DocumentReference assocRef = DocReferences.getAssociationRef();
                            String supportId = FirebaseAuth.getInstance().getUid();
                            //TODO review refs

                            AssociationSupport support = new AssociationSupport(supportId, Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis(),
                                    userRef, null, assocRef, null);
                           AssociationHelper.setRequestCommentSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                                   mRequestId, commentId, supportId, support);
                        }
                        CommentaryAdapter.this.notifyDataSetChanged();
                    }
                });
    }
    class CommentaryViewHolder extends RecyclerView.ViewHolder{
        //ImageViews
        final ImageView avatarIV;
        final ImageView supportIV;
        //TextViews
        final TextView contentTV;
        final TextView authorTV;
        final TextView supportTV;
        public CommentaryViewHolder(View itemView) {
            super(itemView);
            //ImageViews
            avatarIV = itemView.findViewById(R.id.commentAvatarIV);
            supportIV = itemView.findViewById(R.id.supportsIV);
            //TextViews
            authorTV = itemView.findViewById(R.id.commentAuthorTV);
            contentTV = itemView.findViewById(R.id.commentContentTV);
            supportTV = itemView.findViewById(R.id.commentsSupportsTV);

        }
    }
}
