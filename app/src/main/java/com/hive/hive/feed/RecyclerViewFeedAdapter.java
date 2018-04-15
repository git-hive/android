package com.hive.hive.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.Forum;
import com.hive.hive.model.forum.ForumPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vplentz on 11/01/18.
 */

public class RecyclerViewFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Object> mFeedPosts;
    private final int FEED = 0;

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
        return viewHolder;
    }


    public class FeedViewHolderOld extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;

        public FeedViewHolderOld(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.forum_title_tv);
            description = itemView.findViewById(R.id.forum_content_tv);
        }
    }


}
