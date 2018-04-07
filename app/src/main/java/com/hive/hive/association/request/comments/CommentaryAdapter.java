package com.hive.hive.association.request.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

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
    private ArrayList<SupportMutex> mLocks;
    public CommentaryAdapter(Context context, HashMap<String, AssociationComment> comments, ArrayList<String> ids, String requestId){
        this.mContext = context;
        this.mComments = comments;
        this.mIds = ids;
        this.mRequestId = requestId;
        this.mLocks = new ArrayList<>();
    }
    @Override
    public CommentaryAdapter.CommentaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false);

        return new CommentaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentaryAdapter.CommentaryViewHolder holder, final int position) {
        //SUPPORT LOCK
        try{
            if(mLocks.get(position) == null) mLocks.add(new SupportMutex(holder.supportTV, holder.supportIV));
        }catch(java.lang.IndexOutOfBoundsException e){
            mLocks.add(new SupportMutex(holder.supportTV, holder.supportIV));
        }

        final AssociationComment comment = mComments.get(mIds.get(position));
        shouldFillSupport(holder, mRequestId, mIds.get(position));
        fillUser(holder, comment.getAuthorRef());
        holder.contentTV.setText(comment.getContent());
        holder.supportTV.setText(comment.getScore()+"");

        holder.supportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position), mLocks.get(position));
            }
        });

        holder.supportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position), mLocks.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
    private void fillUser(final CommentaryAdapter.CommentaryViewHolder holder, DocumentReference userRef){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(RequestAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    holder.authorTV.setText(user.getName());
                    ProfilePhotoHelper.loadImage(mContext.getApplicationContext(), holder.avatarIV, user.getPhotoUrl());
                }
            }
        });
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
    private void scoreClick(final CommentaryViewHolder holder, final String commentId, final SupportMutex mutex){
        mutex.lock();
        AssociationHelper.getRequestCommentSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                mRequestId, commentId, FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //if support already exists, should delete it
                        if(documentSnapshot.exists()) {
                            AssociationHelper.deleteRequestCommentSupport(FirebaseFirestore.getInstance(),
                                    "gVw7dUkuw3SSZSYRXe8s", mRequestId, commentId, FirebaseAuth.getInstance().getUid())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mutex.unlock();
                                }
                            });
                        }else {// else should add it
                            DocumentReference userRef = DocReferences.getUserRef();
                            DocumentReference assocRef = DocReferences.getAssociationRef("gVw7dUkuw3SSZSYRXe8s");
                            String supportId = FirebaseAuth.getInstance().getUid();
                            //TODO review refs

                            AssociationSupport support = new AssociationSupport( Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis(),
                                    userRef, null, assocRef, null);
                           AssociationHelper.setRequestCommentSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                                   mRequestId, commentId, supportId, support).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   mutex.unlock();
                               }
                           });
                        }
                        CommentaryAdapter.this.notifyDataSetChanged();
                    }
                });
    }
    public class CommentaryViewHolder extends RecyclerView.ViewHolder{
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

        public ImageView getSupportIV() {
            return supportIV;
        }

        public TextView getSupportTV() {
            return supportTV;
        }
    }
}
