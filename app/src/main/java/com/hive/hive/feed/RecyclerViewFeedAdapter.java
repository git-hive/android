package com.hive.hive.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;


import com.hive.hive.feed.comments.FeedCommentsActivity;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.forum.ForumSupport;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Birck.
 */

public class RecyclerViewFeedAdapter extends RecyclerView.Adapter<RecyclerViewFeedAdapter.FeedViewHolder>{

    private String TAG = RecyclerViewFeedAdapter.class.getSimpleName();

    //-- Data
    private Pair<ArrayList<String>, HashMap<String, ForumPost>> posts;
    private HashMap<Integer, Boolean> postsSupport;

    private SupportMutex lock ;
    private ArrayList<String> changedSupportsPostsIds; //postId
    private HashMap<String, Boolean> changedSupports; //postId, changedTo

    private Context context;

    private HashMap<DocumentReference, String> usernames;
    private HashMap<DocumentReference, String> userProfilePictures;

    //--- Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mAssociationID = "gVw7dUkuw3SSZSYRXe8s";

    public RecyclerViewFeedAdapter(
            Pair<ArrayList<String>, HashMap<String, ForumPost>> posts,
            Context context
    ) {
        this.posts = posts;
        this.postsSupport = new HashMap<>();

        this.changedSupportsPostsIds = new ArrayList<>();
        this.changedSupports = new HashMap<>();

        this.usernames = new HashMap<>();
        this.userProfilePictures = new HashMap<>();
        this.context = context;
        this.lock = new SupportMutex();

        }

    public ArrayList<String> getChangedSupportsPostsIds() {
        return changedSupportsPostsIds;
    }

    public HashMap<String, Boolean> getChangedSupports() {
        return changedSupports;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FeedViewHolder holder, final int position) {


        String postId = posts.first.get(position);

        ForumPost post = posts.second.get(postId);

        holder.mItem = post;

        // fill user
        fillUser(holder, post.getAuthorRef());

        // fill post
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getContent());
        holder.mNumberOfSupportsTV.setText(String.valueOf(post.getSupportScore()));
        holder.mNumberOfCommentsTV.setText(String.valueOf(post.getNumComments()));

        // fill support if necessary
        shouldFillSupport(holder, getPostID(position), position);

        //todo onclick to comments
        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                sendToFirebase();
                                                context.startActivity(
                                                        new Intent(context, FeedCommentsActivity.class)
                                                                .putExtra(FeedCommentsActivity.Post_ID, getPostID(position))
                                                );
                                            }
                                        }
        );

        holder.mSupportsIV
                .setOnClickListener(createToggleSupportOnClickListener(position, holder));
        holder.mNumberOfSupportsTV
                .setOnClickListener(createToggleSupportOnClickListener(position, holder));
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.first.size();
        return 0;
    }

    private View.OnClickListener createToggleSupportOnClickListener(
            int position,
            final FeedViewHolder holder
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePostSupport(position, holder.mNumberOfSupportsTV, holder.mSupportsIV);

                // Lock the mutex as soon as the user clicks
//                getpostSupportAndCallSupportActionHandler(
//                        getpostID(position));
            }
        };
    }

    private void fillUser(final FeedViewHolder holder, DocumentReference userRef) {
        // Check if it has on memory
        if (usernames.containsKey(userRef) && userProfilePictures.containsKey(userRef)) {
            String username = usernames.get(userRef);
            String userPhoto = userProfilePictures.get(userRef);

            holder.userName.setText(username);
            ProfilePhotoHelper.loadImage(context, holder.userAvatar, userPhoto);

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
                    ProfilePhotoHelper.loadImage(context, holder.userAvatar, userPhoto);
                }
            }
        });
    }

    private String getPostID(int postPosition) {
        return posts.first.get(postPosition);
    }

    private void shouldFillSupport(
            final FeedViewHolder holder,
            String postId,
            final int position
    ) {
        // Check if it has on memory
        if (postsSupport.containsKey(position)) {
            if (postsSupport.get(position)) {
                holder.mSupportsIV.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.ic_support_filled)
                );
            } else {
                holder.mSupportsIV.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.ic_support_borderline)
                );
            }
            return;
        }
        // If it doesn't, fetch it

        FeedHelper.getForumPostSupport(
                mDB,
                mAssociationID,
                postId,
                mUser.getUid()
        )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            postsSupport.put(position, true);
                            holder.mSupportsIV.setImageDrawable(
                                    context.getResources().getDrawable(R.drawable.ic_support_filled)
                            );
                        } else {
                            postsSupport.put(position, false);
                            holder.mSupportsIV.setImageDrawable(
                                    context.getResources().getDrawable(R.drawable.ic_support_borderline)
                            );
                        }
                    }
                });
    }

    public void sendToFirebase() {
        lock.lock();
        for (String changedSupportPostId : changedSupportsPostsIds) {
            supportActionHandler(changedSupportPostId, changedSupports.get(changedSupportPostId));
        }
        changedSupports.clear();
        changedSupportsPostsIds.clear();
        lock.unlock();
    }

    private void supportActionHandler(
            String postID,
            Boolean addScore) {
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
                    null);

            FeedHelper.setForumPostSupport(
                    FirebaseFirestore.getInstance(),
                    mAssociationID,
                    postID,
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

    private void togglePostSupport(
            int position,
            TextView numberOfSupportsTV,
            ImageView supportIV
    ) {
        Drawable filledSupportIC = context
                .getResources()
                .getDrawable(R.drawable.ic_support_filled);

        Drawable borderlineSupportIC = context
                .getResources()
                .getDrawable(R.drawable.ic_support_borderline);

        String requestId = posts.first.get(position);
        ForumPost post = this.posts.second.get(requestId);
        if (postsSupport.get(position)) {
            supportIV.setImageDrawable(borderlineSupportIC);
            post.decrementScore();
            if (changedSupportsPostsIds.contains(requestId)) {
                changedSupports.remove(requestId);
                changedSupportsPostsIds.remove(requestId);
            } else {
                changedSupports.put(requestId, false);
                changedSupportsPostsIds.add(requestId);
            }
        } else {
            supportIV.setImageDrawable(filledSupportIC);
            post.incrementScore();
            if(changedSupportsPostsIds.contains(requestId)) {
                changedSupports.remove(requestId);
                changedSupportsPostsIds.remove(requestId);
            }else{
                changedSupports.put(requestId, true);
                changedSupportsPostsIds.add(requestId);
            }
        }
        numberOfSupportsTV.setText(String.valueOf(post.getSupportScore()));
        postsSupport.put(position, !postsSupport.get(position));

    }

    public void setData(Pair<ArrayList<String> , HashMap<String, ForumPost>> posts) {
        sendToFirebase();
        this.posts = posts;
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

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


        public FeedViewHolder(View itemView) {
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

}
