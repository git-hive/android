package com.hive.hive.feed.comments;

import android.annotation.SuppressLint;
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
import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.firebaseHelpers.FeedHelper;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.forum.ForumComment;
import com.hive.hive.model.forum.ForumSupport;
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

public class FeedCommentsAdapter extends RecyclerView.Adapter<FeedCommentsAdapter.FeedCommentaryViewHolder>  {
    private final String TAG = com.hive.hive.association.request.comments.CommentsAdapter.class.getSimpleName();

    private Context mContext;
    //--Data
    private String mAssociationID = HomeFragment.mCurrentAssociationId;
    private HashMap<String, ForumComment> mComments;
    private ArrayList<String> mIds;
    private HashMap<Integer, Boolean> commentsSupport;
    private String mPostId;

    //Supports Data
    //control like send
    private SupportMutex lock ;
    private ArrayList<String> changedSupportsCommentsIds; //commentId
    private HashMap<String, Boolean> changedSupports; //CommentId

    private HashMap<DocumentReference, String> usernames;
    private HashMap<DocumentReference, String> userProfilePictures;


    @SuppressLint("UseSparseArrays")
    FeedCommentsAdapter(Context context, HashMap<String, ForumComment> comments, ArrayList<String> ids, String requestId){
        this.mContext = context;
        this.mComments = comments;
        this.mIds = ids;
        this.mPostId = requestId;
        this.commentsSupport = new HashMap<>();
        this.changedSupports = new HashMap<>();
        this.changedSupportsCommentsIds = new ArrayList<>();
        this.lock = new SupportMutex();
        usernames = new HashMap<>();
        userProfilePictures = new HashMap<>();
    }

    public ArrayList<String> getChangedSupportsCommentsIds() {
        return changedSupportsCommentsIds;
    }

    public HashMap<String, Boolean> getChangedSupports() {
        return changedSupports;
    }

    @Override
    public FeedCommentaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false);

        return new FeedCommentaryViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FeedCommentaryViewHolder holder, final int position) {
        final ForumComment comment = mComments.get(mIds.get(position));

        fillUser(holder, comment.getAuthorRef());
        holder.contentTV.setText(comment.getContent());
        holder.supportTV.setText(comment.getSupportScore()+"");
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


    private void fillUser(final FeedCommentaryViewHolder holder, DocumentReference userRef){

        // Check if it has on memory
        if (usernames.containsKey(userRef) && userProfilePictures.containsKey(userRef)) {
            String username = usernames.get(userRef);
            String userPhoto = userProfilePictures.get(userRef);

            holder.authorTV.setText(username);
            ProfilePhotoHelper.loadImage(mContext, holder.avatarIV, userPhoto);

            return;
        }

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Log.d(RequestAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                User user = documentSnapshot.toObject(User.class);

                // Save data to local "cache"
                usernames.put(userRef, user.getName());
                userProfilePictures.put(userRef, user.getPhotoUrl());


                holder.authorTV.setText(user.getName().split(" ")[0]);
                ProfilePhotoHelper.loadImage(mContext.getApplicationContext(), holder.avatarIV, user.getPhotoUrl());
            }
        });
    }
    private void shouldFillSupport(
            final FeedCommentaryViewHolder holder,
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

        FeedHelper.getForumPostCommentSupport(
                mPostId,
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
        ForumSupport support = new ForumSupport(
                currentTimeInMillis,
                currentTimeInMillis,
                userRef,
                null
        );

        FeedHelper.setSupportForumPostComment(
                mPostId,
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
            final FeedCommentaryViewHolder holder
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
        ForumComment comment = this.mComments.get(commentId);
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
        numberOfSupportsTV.setText(String.valueOf(comment.getSupportScore()));
        commentsSupport.put(position, !commentsSupport.get(position));

    }


    public class FeedCommentaryViewHolder extends RecyclerView.ViewHolder{
        //ImageViews
        final ImageView avatarIV;
        final ImageView supportIV;
        //TextViews
        final TextView contentTV;
        final TextView authorTV;
        final TextView supportTV;

        FeedCommentaryViewHolder(View itemView) {
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
