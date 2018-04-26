package com.hive.hive.feed;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.association.request.comments.CommentsActivity;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.Forum;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
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
    private ArrayList<SupportMutex> mLocks;
    private final int FEED = 0;

    Context mContext;

    public RecyclerViewFeedAdapter(ArrayList<Object> items) {
        mFeedPosts = items;
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
                shouldFillSupport(feedViewHolderOld, getForumPostsID(position));

                feedViewHolderOld.mView.setOnClickListener(view ->
                        mContext.startActivity(
                                new Intent(mContext, CommentsActivity.class)
                                        .putExtra(CommentsActivity.REQUEST_ID ,item.getForumId())
                        )
                );

                feedViewHolderOld.mSupportsIV.setOnClickListener(createToggleSupportOnClickListener(position));
                feedViewHolderOld.mNumberOfSupportsTV.setOnClickListener(createToggleSupportOnClickListener(position));


                break;
        }
    }

    @Override
    public int getItemCount() {
        return mFeedPosts.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(mFeedPosts.get(position) instanceof Request)
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

        mContext = inflater.getContext();

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


    // TODO: FINISH
    private void fillUser(final FeedViewHolderOld holder, DocumentReference userRef){
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, documentSnapshot.get("name").toString());
                User user = documentSnapshot.toObject(User.class);
                holder.userName.setText(user.getName());
                ProfilePhotoHelper.loadImage(mContext, holder.userAvatar, user.getPhotoUrl());
            }
        });
    }

    private void shouldFillSupport(final FeedViewHolderOld holder, String requestId){
        //if exists support, then should be IV filled
        FeedHelper.getForumPostSupport(
                mDB,
                mAssociationID,
                requestId,
                mUser.getUid()
        )
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists())
                        holder.mSupportsIV.setImageDrawable(
                                mContext.getResources().getDrawable(R.drawable.ic_support_filled)
                        );
                    else
                        holder.mSupportsIV.setImageDrawable(
                                mContext.getResources().getDrawable(R.drawable.ic_support_borderline)
                        );
                });
    }

    private View.OnClickListener createToggleSupportOnClickListener(int position) {
        return v -> {
            // Lock the mutex as soon as the user clicks
            SupportMutex mutex = mLocks.get(position);
            mutex.lock();

            getRequestSupportAndCallSupportActionHandler(
                    position,
                    getForumPostsID(position),
                    mutex
            );
        };
    }

    private void getRequestSupportAndCallSupportActionHandler(
            int requestPosition,
            String requestID,
            final SupportMutex mutex
    ) {
        AssociationHelper.getRequestSupport(
                mDB,
                mAssociationID,
                requestID,
                mUser.getUid()
        )
                .addOnSuccessListener(
                        documentSnapshot -> supportActionHandler(
                                requestPosition,
                                requestID,
                                documentSnapshot,
                                mutex
                        )
                );
    }

    private void supportActionHandler(
            int requestPosition,
            String requestID,
            DocumentSnapshot supportSnap,
            final SupportMutex mutex
    ) {
        ForumPost forumPost = ((ForumPost)mFeedPosts.get(requestPosition));
        // Toggle request support
        if (supportSnap.exists()) {
            // Remove support
            AssociationHelper.removeRequestSupport(
                    mDB,
                    mAssociationID,
                    requestID,
                    supportSnap.getId()
            )
                    .addOnCompleteListener(task -> mutex.unlock())
                    .addOnFailureListener(e -> Log.e(TAG, e.toString()))
                    .addOnSuccessListener(aVoid -> {
                        forumPost.decrementScore();
                        RecyclerViewFeedAdapter.this.notifyDataSetChanged();
                    });
        } else {
            // Create and save support
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
                    assocRef,
                    null
            );

            AssociationHelper.setRequestSupport(
                    FirebaseFirestore.getInstance(),
                    mAssociationID,
                    requestID,
                    supportId,
                    support
            )
                    .addOnCompleteListener(task -> mutex.unlock())
                    .addOnFailureListener(e ->Log.e(TAG, e.toString()))
                    .addOnSuccessListener(aVoid -> {
                        forumPost.incrementScore();
                        RecyclerViewFeedAdapter.this.notifyDataSetChanged();
                    });
        }
    }

    private String getForumPostsID(int requestPosition) {
        return ((ForumPost)mFeedPosts.get(requestPosition)).getForumId();
    }
}
