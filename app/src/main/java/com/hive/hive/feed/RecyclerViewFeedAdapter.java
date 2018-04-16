package com.hive.hive.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.R;
import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.Forum;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.ProfilePhotoHelper;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Birck.
 */

public class RecyclerViewFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Object> mFeedPosts;
    private final int FEED = 0;

    Context mContext;

    public RecyclerViewFeedAdapter(ArrayList<Object> items) {
        mFeedPosts = items;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case FEED:
                  //                  Get Current Item                       //
                    ForumPost item = (ForumPost) mFeedPosts.get(position);  //
                // _______________________________________________________ //

                FeedViewHolderOld feedViewHolderOld = (FeedViewHolderOld) viewHolder;
                feedViewHolderOld.title.setText(item.getTitle());
                feedViewHolderOld.description.setText(item.getContent());

                fillUser(feedViewHolderOld, item.getAuthorRef());

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
        TextView title;
        TextView description;
        TextView userName;
        ImageView userAvatar;

        public FeedViewHolderOld(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.forum_title_tv);
            description = itemView.findViewById(R.id.forum_content_tv);
            userName = itemView.findViewById(R.id.post_author_name_tv);
            userAvatar = itemView.findViewById(R.id.post_author_photo_iv);
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

}
