package com.hive.hive.association.request.comments;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.feed.FeedHelper;
import com.hive.hive.feed.RecyclerViewFeedAdapter;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.association.AssociationComment;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.forum.ForumPost;
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
    private final String TAG = CommentsAdapter.class.getSimpleName();

    private Context mContext;
    //--Data
    private String mAssociationID = HomeFragment.mCurrentAssociationId;
    private HashMap<String,  AssociationComment> mComments;
    private ArrayList<String> mIds;
    private HashMap<Integer, Boolean> commentsSupport;
    private String mRequestId;

    //Supports Data
    //control like send
    private SupportMutex lock ;
    private ArrayList<String> changedSupportsCommentsIds; //commentId
    private HashMap<String, Boolean> changedSupports; //CommentId

    public CommentsAdapter(Context context, HashMap<String, AssociationComment> comments, ArrayList<String> ids, String requestId){
        this.mContext = context;
        this.mComments = comments;
        this.mIds = ids;
        this.mRequestId = requestId;
        this.commentsSupport = new HashMap<>();
        this.changedSupports = new HashMap<>();
        this.changedSupportsCommentsIds = new ArrayList<>();
        this.lock = new SupportMutex();
    }

    public ArrayList<String> getChangedSupportsCommentsIds() {
        return changedSupportsCommentsIds;
    }

    public HashMap<String, Boolean> getChangedSupports() {
        return changedSupports;
    }

    @Override
    public CommentsAdapter.CommentaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false);

        return new CommentaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.CommentaryViewHolder holder, final int position) {
        final AssociationComment comment = mComments.get(mIds.get(position));

        fillUser(holder, comment.getAuthorRef());
        holder.contentTV.setText(comment.getContent());
        holder.supportTV.setText(comment.getScore()+"");
        shouldFillSupport(holder, position);
        holder.supportTV
                .setOnClickListener(createToggleSupportOnClickListener(position, holder));
        holder.supportIV
                .setOnClickListener(createToggleSupportOnClickListener(position, holder));
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
                    Log.d(RequestAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    holder.authorTV.setText(user.getName().split(" ")[0]);
                    ProfilePhotoHelper.loadImage(mContext.getApplicationContext(), holder.avatarIV, user.getPhotoUrl());
                }
            }
        });
    }
    private void shouldFillSupport(
            final CommentaryViewHolder holder,
            final int position
    ) {
        // Check if it has on memory
        if (commentsSupport.containsKey(position)) {
            if (commentsSupport.get(position)) {
                holder.supportIV.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_support_filled)
                );
            } else {
                holder.supportIV.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_support_borderline)
                );
            }
            return;
        }
        // If it doesn't, fetch it

        AssociationHelper.getRequestCommentSupport(
                FirebaseFirestore.getInstance(),
                mAssociationID,
                mRequestId,
                mIds.get(position),
                FirebaseAuth.getInstance().getUid()
        )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            commentsSupport.put(position, true);
                            holder.supportIV.setImageDrawable(
                                    mContext.getResources().getDrawable(R.drawable.ic_support_filled)
                            );
                        } else {
                            commentsSupport.put(position, false);
                            holder.supportIV.setImageDrawable(
                                    mContext.getResources().getDrawable(R.drawable.ic_support_borderline)
                            );
                        }
                    }
                });
    }

    public void sendToFirebase() {
        lock.lock();
        for (String changedSupportCommentId : changedSupportsCommentsIds) {
            supportActionHandler(changedSupportCommentId);
        }
        changedSupports.clear();
        changedSupportsCommentsIds.clear();
        lock.unlock();
    }

    private void supportActionHandler(String commentId) {
        // Toggle post support
        // Create or delete  support
        DocumentReference userRef = DocReferences.getUserRef();
        DocumentReference assocRef = DocReferences.getAssociationRef(mAssociationID);
        String supportId = FirebaseAuth.getInstance().getUid();

        // TODO: Add missing refs
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        AssociationSupport support = new AssociationSupport(
                currentTimeInMillis,
                currentTimeInMillis,
                userRef,
                null,
                null,
                null
        );

        AssociationHelper.setRequestCommentSupport(
                FirebaseFirestore.getInstance(),
                mAssociationID,
                mRequestId,
                commentId,
                supportId,
                support
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "SUCESSO");
            }
        });
    }

    private View.OnClickListener createToggleSupportOnClickListener(
            int position,
            final CommentsAdapter.CommentaryViewHolder holder
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePostSupport(position, holder.supportTV, holder.supportIV);
            }
        };
    }

    private void togglePostSupport(
            int position,
            TextView numberOfSupportsTV,
            ImageView supportIV
    ) {
        Drawable filledSupportIC = mContext
                .getResources()
                .getDrawable(R.drawable.ic_support_filled);

        Drawable borderlineSupportIC = mContext
                .getResources()
                .getDrawable(R.drawable.ic_support_borderline);

        String commentId = mIds.get(position);
        AssociationComment comment = this.mComments.get(commentId);
        if (commentsSupport.get(position)) {// in case youve supported
            supportIV.setImageDrawable(borderlineSupportIC);
            comment.decrementScore();
            if (changedSupportsCommentsIds.contains(commentId)) { //if there was a change you should undo it
                changedSupports.remove(commentId);
                changedSupportsCommentsIds.remove(commentId);
            } else {
                changedSupports.put(commentId, false);
                changedSupportsCommentsIds.add(commentId);
            }
        } else {
            supportIV.setImageDrawable(filledSupportIC);
            comment.incrementScore();
            if(changedSupportsCommentsIds.contains(commentId)) {
                changedSupports.remove(commentId);
                changedSupportsCommentsIds.remove(commentId);
            }else{
                changedSupports.put(commentId, true);
                changedSupportsCommentsIds.add(commentId);
            }
        }
        numberOfSupportsTV.setText(String.valueOf(comment.getScore()));
        commentsSupport.put(position, !commentsSupport.get(position));

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
