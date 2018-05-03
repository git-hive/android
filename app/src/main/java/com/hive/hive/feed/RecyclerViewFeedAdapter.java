package com.hive.hive.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;


import com.hive.hive.feed.comments.CommentsActivity;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.forum.ForumSupport;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Birck.
 */

public class RecyclerViewFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //--- Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mAssociationID = "gVw7dUkuw3SSZSYRXe8s";

    //Data
    private ArrayList<Object> mFeedPosts;
    private ArrayList<String> mFeedPostsIDs;
    private HashMap<Integer, Boolean>  mFeedPostSupport;

    private ArrayList<SupportMutex> mLocks;
    private final int FEED = 0;

    private HashMap<DocumentReference, String> usernames;
    private HashMap<DocumentReference, String> userProfilePictures;

    Context mContext;

    public RecyclerViewFeedAdapter(
            ArrayList<Object> items,
            ArrayList<String> itemsIDs,
            Context context
    ) {
        mFeedPosts = items;
        mFeedPostsIDs = itemsIDs;
        Log.d(TAG, String.valueOf(mFeedPostsIDs.size())+"??????????????????????????????????????????????????????");

        mContext = context;
        this.mFeedPostSupport = new HashMap<>();
        this.usernames = new HashMap<>();
        this.userProfilePictures = new HashMap<>();
        this.mLocks = new ArrayList<>();

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case FEED:
                  //                  Get Current Item                       //
                    ForumPost item = (ForumPost) mFeedPosts.get(position);  //
                // _______________________________________________________ //

                FeedViewHolderOld feedViewHolderOld = (FeedViewHolderOld) viewHolder;

                try {
                    if (mLocks.get(position) == null)
                        mLocks.add(new SupportMutex(feedViewHolderOld.mNumberOfSupportsTV, feedViewHolderOld.mSupportsIV));
                } catch (java.lang.IndexOutOfBoundsException e) {
                    mLocks.add(new SupportMutex(feedViewHolderOld.mNumberOfSupportsTV, feedViewHolderOld.mSupportsIV));
                }

                feedViewHolderOld.mItem = item;

                // Fill Card
                feedViewHolderOld.title.setText(item.getTitle());
                feedViewHolderOld.description.setText(item.getContent());
                feedViewHolderOld.mNumberOfSupportsTV.setText(String.valueOf(item.getSupportScore()));
                feedViewHolderOld.mNumberOfCommentsTV.setText(String.valueOf(item.getNumComments()));

                // Fill User
                fillUser(feedViewHolderOld, item.getAuthorRef());

                // fill support if necessary
                shouldFillSupport(feedViewHolderOld, getForumPostsID(position), position);

                feedViewHolderOld.mView.setOnClickListener(view ->
                        mContext.startActivity(
                                new Intent(mContext, CommentsActivity.class)
                                        .putExtra(CommentsActivity.FORUM_POST_ID ,item.getForumId())
                        )
                );

                feedViewHolderOld.mSupportsIV.setOnClickListener(createToggleSupportOnClickListener(position, feedViewHolderOld));
                feedViewHolderOld.mNumberOfSupportsTV.setOnClickListener(createToggleSupportOnClickListener(position, feedViewHolderOld));


                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(mFeedPosts.get(position) instanceof ForumPost)
            return FEED;
        else
            return FEED;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder =  null;
        switch (viewType) {
            case FEED:
                View viewRequisition = inflater.inflate(R.layout.item_feed, viewGroup, false);
                viewHolder = new FeedViewHolderOld(viewRequisition);
                break;
        }

        return viewHolder;
    }


    public class FeedViewHolderOld extends RecyclerView.ViewHolder {

        final View mView;

        TextView title;
        TextView description;
        TextView userName;
        ImageView userAvatar;

        // Counters
        TextView mNumberOfCommentsTV;
        TextView mNumberOfSupportsTV;

        // ImageViews
        ImageView mSupportsIV;
        ImageView mCommentsIV;

        ForumPost mItem;


        public FeedViewHolderOld(View itemView) {
            super(itemView);

            mView = itemView;

            title = itemView.findViewById(R.id.forum_title_tv);
            description = itemView.findViewById(R.id.forum_content_tv);
            userName = itemView.findViewById(R.id.post_author_name_tv);
            userAvatar = itemView.findViewById(R.id.post_author_photo_iv);

            // Counters
            mNumberOfCommentsTV = itemView.findViewById(R.id.post_number_of_comments_tv);
            mNumberOfSupportsTV = itemView.findViewById(R.id.supportsTV);

            // Imageviews
            mCommentsIV = itemView.findViewById(R.id.commentsIV);
            mSupportsIV = itemView.findViewById(R.id.supportsIV);
        }
    }


    @Override
    public int getItemCount() {
        if(mFeedPosts != null)
            return mFeedPosts.size();
        return 0;
    }

    private View.OnClickListener createToggleSupportOnClickListener(
            int position,
            final FeedViewHolderOld holder
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForumPostSupport(position, holder.mNumberOfSupportsTV, holder.mSupportsIV);

                // Lock the mutex as soon as the user clicks
                SupportMutex mutex = mLocks.get(position);
                mutex.lock();

                getForumPostSupportAndCallSupportActionHandler(
                        getForumPostsID(position),
                        mutex
                );
            }
        };
    }

    private void fillUser(final FeedViewHolderOld holder, DocumentReference userRef) {
        // Check if it has on memory
        if (usernames.containsKey(userRef) && userProfilePictures.containsKey(userRef)) {
            String username = usernames.get(userRef);
            String userPhoto = userProfilePictures.get(userRef);

            holder.userName.setText(username);
            ProfilePhotoHelper.loadImage(mContext, holder.userAvatar, userPhoto);

            return;
        }

        // If it doesn't, fetch it
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    String username = user.getName();
                    String userPhoto = user.getPhotoUrl();

                    // Save data to local "cache"
                    usernames.put(userRef, username);
                    userProfilePictures.put(userRef, userPhoto);

                    holder.userName.setText(username);
                    ProfilePhotoHelper.loadImage(mContext, holder.userAvatar, userPhoto);
                }
            }
        });
    }

    private String getForumPostsID(int forumPostPosition) {
        Log.d(TAG, mFeedPostsIDs.size()+" THIS MUST BE SOMETHING"+forumPostPosition);
        if(!mFeedPostsIDs.isEmpty())
            return mFeedPostsIDs.get(forumPostPosition);
        return null;
    }

    private void shouldFillSupport(
            final FeedViewHolderOld holder,
            String feedPostID,
            final int position
    ) {

        if(feedPostID == null)
            return;

        // Check if it has on memory
        if (mFeedPostSupport.containsKey(position)) {
            if (mFeedPostSupport.get(position)) {
                holder.mSupportsIV.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_support_filled)
                );
            } else {
                holder.mSupportsIV.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_support_borderline)
                );
            }
            return;
        }
        // If it doesn't, fetch it

        FeedHelper.getForumPostSupport(
                mDB,
                mAssociationID,
                feedPostID,
                mUser.getUid()
        )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            mFeedPostSupport.put(position, true);
                            holder.mSupportsIV.setImageDrawable(
                                    mContext.getResources().getDrawable(R.drawable.ic_support_filled)
                            );
                        } else {
                            mFeedPostSupport.put(position, false);
                            holder.mSupportsIV.setImageDrawable(
                                    mContext.getResources().getDrawable(R.drawable.ic_support_borderline)
                            );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void getForumPostSupportAndCallSupportActionHandler(
            String forumPostID,
            final SupportMutex mutex
    ) {
        if(forumPostID == null)
            return;
        FeedHelper.getForumPostSupport(
                mDB,
                mAssociationID,
                forumPostID,
                mUser.getUid()
        )
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                supportActionHandler(
                                        forumPostID,
                                        documentSnapshot,
                                        mutex
                                );
                            }
                        }
                );
    }

    private void supportActionHandler(
            String forumPostID,
            DocumentSnapshot supportSnap,
            final SupportMutex mutex
    ) {
        // Toggle Forum support
        if (supportSnap.exists()) {
            // Remove support
            FeedHelper.removeForumPostSupport(
                    mDB,
                    mAssociationID,
                    forumPostID,
                    supportSnap.getId()
            )
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mutex.unlock();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    });
        } else {
            // Create and save support
            DocumentReference userRef = DocReferences.getUserRef();
            DocumentReference assocRef = DocReferences.getAssociationRef(mAssociationID);
            String supportId = FirebaseAuth.getInstance().getUid();

            // TODO: Add missing refs
            Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
            ForumSupport support = new ForumSupport(
                    currentTimeInMillis,
                    currentTimeInMillis,
                    userRef,
                    null,
                    forumPostID,
                    null
            );

            FeedHelper.setForumPostSupport(
                    FirebaseFirestore.getInstance(),
                    mAssociationID,
                    forumPostID,
                    supportId,
                    support
            )
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mutex.unlock();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    });
        }
    }

    private void toggleForumPostSupport(
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

        ForumPost post = (ForumPost) mFeedPosts.get(position);
        Log.d(TAG, String.valueOf(mFeedPostSupport.size()));
        if(mFeedPostSupport.size() == 0)
            return;

        if (mFeedPostSupport.get(position)) {
            supportIV.setImageDrawable(borderlineSupportIC);
            post.decrementScore();
            Toast.makeText(mContext, "removing support", Toast.LENGTH_SHORT).show();
        } else {
            supportIV.setImageDrawable(filledSupportIC);
            post.incrementScore();
            Toast.makeText(mContext, "adding support", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
        mFeedPostSupport.put(position, !mFeedPostSupport.get(position));
    }

    public void setData(ArrayList<Object> posts, ArrayList<String> postIDs) {
        mFeedPosts = posts;
        mFeedPostsIDs = postIDs;
    }
}
