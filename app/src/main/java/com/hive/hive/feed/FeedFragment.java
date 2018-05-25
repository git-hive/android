package com.hive.hive.feed;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;

import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.ForumPost;

import static android.content.ContentValues.TAG;
import static com.hive.hive.feed.FeedHelper.getAllForumPosts;

import com.hive.hive.model.user.User;
import com.hive.hive.profiles.UserProfileActivity;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;


import java.util.ArrayList;
import java.util.HashMap;

public class FeedFragment extends Fragment {

    private final String TAG = FeedFragment.class.getSimpleName();

    //--- Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    // recycler view
    private SwipeRefreshLayout mFeedRefresh;
    private RecyclerView mFeedRV;
    private RecyclerViewFeedAdapter mRecyclerAdapter;

    // Views
    private View mView;


    //--- Association
    // TODO: Change hardcoded associationID
    private String associationID = "gVw7dUkuw3SSZSYRXe8s";
    private CollectionReference mAssociationFeedRef;

    //--- Data
    private Pair<ArrayList<String>, HashMap<String, ForumPost>> posts;

    public FeedFragment() {
        // Required empty public constructor
    }


    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RecyclerViewFeedAdapter getmRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        mView = v;

        posts = new Pair<>(new ArrayList<>(), new HashMap<>());


        mFeedRefresh = v.findViewById(R.id.feedSR);
        mFeedRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.first.clear();
                posts.second.clear();
                addRequestSnapListenerAndCallSetupRecyclerView();
            }
        });
        mFeedRefresh.setColorSchemeColors(getResources().getColor(R.color.colorOrange));
        mFeedRefresh.setRefreshing(true);


        mAssociationFeedRef = mDB
                .collection("associations")
                .document(associationID)
                .collection("forum");

        addRequestSnapListenerAndCallSetupRecyclerView();

        return v;
    }

    /**
     * Attaches a listener to 'associationRequestsRef',
     * handling it's changes and call setupRecyclerView
     *
     */
    private void addRequestSnapListenerAndCallSetupRecyclerView() {
        mAssociationFeedRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (DocumentSnapshot dc : documentSnapshots) {
                    if (dc.exists()) {
                        posts.first.add(dc.getId());
                        posts.second.put(dc.getId(), dc.toObject(ForumPost.class));

                    }
                }
                setupRecyclerView();
                if(mFeedRefresh.isRefreshing()){
                    mFeedRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerAdapter = new RecyclerViewFeedAdapter(posts, getContext().getApplicationContext());
        mRecyclerAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mFeedRV = mView.findViewById(R.id.feedRV);
        mFeedRV.setLayoutManager(linearLayoutManager);
        mFeedRV.setAdapter(mRecyclerAdapter);

        disableProgressBarAndShowRecycler();
    }

    private void disableProgressBarAndShowRecycler() {
        mFeedRV.setVisibility(View.VISIBLE);
        mFeedRefresh.setRefreshing(false);
    }

}
