package com.hive.hive.feed.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.feed.FeedHelper;
import com.hive.hive.feed.RecyclerViewFeedAdapter;
import com.hive.hive.model.forum.ForumComment;
import com.hive.hive.model.forum.ForumSupport;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by vplentz on 15/02/18.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentaryViewHolder>  {
    private HashMap<String, ForumComment> mComments;
    private ArrayList<String> mIds;
    private Context mContext;
    private String mRequestId;
    private ArrayList<SupportMutex> mLocks;
    private ArrayList<LinkedList<Boolean>> mSupportQueues;
    private ArrayList<Boolean> mLastSupports;

    public CommentsAdapter(Context context, HashMap<String, ForumComment> comments, ArrayList<String> ids, String requestId){
        this.mContext = context;
        this.mComments = comments;
        this.mIds = ids;
        this.mRequestId = requestId;
        this.mLocks = new ArrayList<>();
        this.mSupportQueues = new ArrayList<>();
        this.mLastSupports = new ArrayList<>();
    }
    @Override
    public CommentsAdapter.CommentaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false);

        return new CommentaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.CommentaryViewHolder holder, final int position) {
        //SUPPORT LOCK
        try{
            if(mLocks.get(position) == null) mLocks.add(new SupportMutex(holder.supportTV, holder.supportIV));
        }catch(java.lang.IndexOutOfBoundsException e){
            mLocks.add(new SupportMutex(holder.supportTV, holder.supportIV));
        }

        final ForumComment comment = mComments.get(mIds.get(position));
        try {
            mLastSupports.get(position); //if it exists then do nothing
        }catch (IndexOutOfBoundsException e){
            shouldFillSupport(holder, mRequestId, mIds.get(position), position);// if doesnt should be verified
        }
        fillUser(holder, comment.getAuthorRef());
        holder.contentTV.setText(comment.getContent());
        holder.supportTV.setText(comment.getSupportScore()+"");

        holder.supportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position), position);
            }
        });

        holder.supportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
    private void fillUser(final CommentsAdapter.CommentaryViewHolder holder, DocumentReference userRef){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(RecyclerViewFeedAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    holder.authorTV.setText(user.getName());
                    ProfilePhotoHelper.loadImage(mContext.getApplicationContext(), holder.avatarIV, user.getPhotoUrl());
                }
            }
        });
    }
    private void shouldFillSupport(final CommentaryViewHolder holder, String requestId, String commentId, int position){

        //if exists support, then should be IV filled
        FeedHelper.getForumPostCommentSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                requestId, commentId, FirebaseAuth.getInstance().getUid() )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            holder.supportIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_support_filled));
                            try {
                                mLastSupports.set(position, true);
                                mSupportQueues.set(position, new LinkedList<>());
                            }catch (IndexOutOfBoundsException e){
                                mLastSupports.add(true);
                                mSupportQueues.add(new LinkedList<>());
                            }
                        }else {
                            holder.supportIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_support_borderline));
                            try {
                                mLastSupports.set(position, false);
                                mSupportQueues.set(position, new LinkedList<>());
                            }catch (IndexOutOfBoundsException e){
                                mLastSupports.add(false);
                                mSupportQueues.add(new LinkedList<>());
                            }
                        }
                    }
                });
    }
    private void scoreClick(final CommentaryViewHolder holder, final String commentId,  int position){
        if(mLastSupports.get(position)){//support already filled, decrease it then
            holder.supportIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_support_borderline));
            holder.supportTV.setText(mComments.get(commentId).getSupportScore()-1 +"");
            mSupportQueues.get(position).add(false);
            mLastSupports.set(position,  false);
            score(commentId, position);
        }else{
            holder.supportIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_support_filled));
            holder.supportTV.setText(mComments.get(commentId).getSupportScore()+1 +"");
            mSupportQueues.get(position).add(true);
            mLastSupports.set(position, true);
            score(commentId, position);
        }

    }
    private void score(final String commentId,  int position){
        mLocks.get(position).lock();
        if(!mSupportQueues.get(position).getFirst()){//decrease score
            FeedHelper.deleteForumPostCommentSupport(FirebaseFirestore.getInstance(),
                    "gVw7dUkuw3SSZSYRXe8s", mRequestId, commentId, FirebaseAuth.getInstance().getUid())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mSupportQueues.get(position).removeFirst();
                            mLocks.get(position).unlock();
                            if(!mSupportQueues.get(position).isEmpty()) score(commentId, position);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// try again
                    if(!mSupportQueues.get(position).isEmpty()) score(commentId, position);
                }
            });

        }else{//increase score
            DocumentReference userRef = DocReferences.getUserRef();
            DocumentReference assocRef = DocReferences.getAssociationRef("gVw7dUkuw3SSZSYRXe8s");
            String supportId = FirebaseAuth.getInstance().getUid();

            ForumSupport support = new ForumSupport( Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis(),
                    userRef, null, assocRef.toString(), null);
            FeedHelper.setSupportForumPostComment(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s",
                    mRequestId, commentId, supportId, support).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mSupportQueues.get(position).removeFirst();
                    mLocks.get(position).unlock();
                    if(!mSupportQueues.get(position).isEmpty()) score(commentId, position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(!mSupportQueues.get(position).isEmpty()) score(commentId, position);
                }
            });
        }
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
