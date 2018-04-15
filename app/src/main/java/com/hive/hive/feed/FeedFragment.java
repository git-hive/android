package com.hive.hive.feed;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.ForumPost;

import static android.content.ContentValues.TAG;
import static com.hive.hive.feed.FeedHelper.getAllForumPosts;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    // Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    // Association
    final String associationID = "gVw7dUkuw3SSZSYRXe8s";


    private RecyclerView mRecyclerViewFeed;
    private RecyclerViewFeedAdapter mRecyclerViewFeedAdapter;
    ArrayList<Object> DUMMYARRAY;
    ArrayList<Object> mFeedPosts;

    private FloatingActionButton feedFab;


    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        getRequests();


        mRecyclerViewFeed = v.findViewById(R.id.recyclerViewFeed);

        mRecyclerViewFeedAdapter = new RecyclerViewFeedAdapter(mFeedPosts);

        mRecyclerViewFeed.setAdapter(mRecyclerViewFeedAdapter);
        mRecyclerViewFeed.setLayoutManager(new LinearLayoutManager(v.getContext()));

        feedFab = v.findViewById(R.id.fab);

        feedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(v.getContext(), NewPostActivity.class)
                );
            }
        });

        return v;
    }


    public void getRequests(){
        mFeedPosts = new ArrayList<>();

        getAllForumPosts(mDB, associationID).addOnSuccessListener(documentSnapshots -> {

            if (documentSnapshots.isEmpty()) {
                Toast.makeText(getContext(), "Falha ao pegar ForumPosts", Toast.LENGTH_SHORT).show();
                return;
            }


            for (DocumentSnapshot doc : documentSnapshots) {
                mFeedPosts.add(doc.toObject(ForumPost.class));
            }
        }).addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

}
