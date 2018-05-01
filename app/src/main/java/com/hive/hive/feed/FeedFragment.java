package com.hive.hive.feed;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;

import com.hive.hive.model.forum.ForumPost;

import static android.content.ContentValues.TAG;
import static com.hive.hive.feed.FeedHelper.getAllForumPosts;

import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;


import java.util.ArrayList;
import java.util.HashMap;

public class FeedFragment extends Fragment {

    // Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    // Association
    final String associationID = "gVw7dUkuw3SSZSYRXe8s";


    // Data
    private RecyclerView mRecyclerViewFeed;
    private RecyclerViewFeedAdapter mRecyclerViewFeedAdapter;
    private ArrayList<Object> mFeedPosts;
    private HashMap<ForumPost, String> mFeedPostHashIDs;
    private ArrayList<String> mFeedPostIds;
    private FloatingActionButton feedFab;

    // Views
    ImageView mUserPhoto;
    TextView mUserScore;

    //Settings
    Context mContext;

    public FeedFragment() {
        // Required empty public constructor
        mFeedPosts = new ArrayList<>();
        mFeedPostIds = new ArrayList<>();
        mFeedPostHashIDs = new HashMap<>();
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
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        mContext = getContext();

        // User Views references

        mUserScore = v.findViewById(R.id.score);
        mUserPhoto = v.findViewById(R.id.userAvatar);

        fillLoggedUserView();
        getAllFeedPostsAndCallJoinFeedPostsCategories();
        mFeedPostIds = getFeedPostIDs(mFeedPosts);


        mRecyclerViewFeed = v.findViewById(R.id.recyclerViewFeed);

        mRecyclerViewFeedAdapter = new RecyclerViewFeedAdapter(mFeedPosts, mFeedPostIds, mContext);

        mRecyclerViewFeed.setAdapter(mRecyclerViewFeedAdapter);
        mRecyclerViewFeed.setLayoutManager(new LinearLayoutManager(v.getContext()));

        feedFab = v.findViewById(R.id.fab);

        feedFab.setOnClickListener(view -> startActivity(
                new Intent(view.getContext(), NewPostActivity.class)
        ));

        return v;
    }

    private void getAllFeedPostsAndCallJoinFeedPostsCategories() {
        FeedHelper.getAllForumPosts(mDB, associationID)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.isEmpty()) {
                            Toast.makeText(
                                    mContext,
                                    "Falha ao pegar requisições",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }


                        for (DocumentSnapshot snap : documentSnapshots) {
                            ForumPost post = snap.toObject(ForumPost.class);
                            Log.d(TAG, post.getAuthorRef().toString()+"???????????????BEFORE CONTINUE?????????????????"+post.getForumId());
                            if (post.getAuthorRef() == null) continue;
                            Log.d(TAG, post.getAuthorRef().toString()+"???????????????AFTER CONTINUE?????????????????"+post.getForumId());

                            mFeedPosts.add(post);
                            mFeedPostHashIDs.put(post, snap.getId());

                            Log.d(TAG, snap.getId()+"+++++++++++++++++++++++++");

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

    public void getFeedPosts(){

        getAllForumPosts(mDB, associationID).addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()) {
                Toast.makeText(getContext(), "Falha ao pegar ForumPosts", Toast.LENGTH_SHORT).show();
                return;
            }

            for (DocumentSnapshot doc : documentSnapshots)
                mFeedPosts.add(doc.toObject(ForumPost.class));

        }).addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    public void fillLoggedUserView(){
        try {
            DocumentReference userRef = DocReferences.getUserRef();
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    //mUserScore.setText(user.getScore());
                    ProfilePhotoHelper.loadImage(mContext, mUserPhoto, user.getPhotoUrl());
                }
            });
        }catch(NullPointerException e){
            Log.e(TAG, e.getMessage());
        }
    }

    private ArrayList<String> getFeedPostIDs(ArrayList<Object> feedPosts) {
        ArrayList<String> newFeedPostIDs = new ArrayList<>();
        Log.d(TAG, mFeedPostHashIDs.size()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Object post : feedPosts) {
            newFeedPostIDs.add(mFeedPostHashIDs.get((ForumPost) post));
            Log.d(TAG, mFeedPostHashIDs.get(post).toString()+"????????????????????????????????");

        }
        return newFeedPostIDs;
    }



}
