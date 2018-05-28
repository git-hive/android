package com.hive.hive.feed;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.model.forum.ForumPost;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FeedFragment extends Fragment {

    private final String TAG = FeedFragment.class.getSimpleName();

    //--- Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    // private ListenerRegistration mFeedPostsLR;
    // recycler view
    private CircleProgressBar mProgressBar;
    private RecyclerView mFeedRV;
    private RecyclerViewFeedAdapter mRecyclerAdapter;

    // Views
    private View mView;
    private FloatingActionButton mFab;

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


        mProgressBar = v.findViewById(R.id.feedPB);
        mFab = v.findViewById(R.id.fab);

        onClicks();

        String associationID = "gVw7dUkuw3SSZSYRXe8s";
        mAssociationFeedRef = mDB
                .collection("associations")
                .document(associationID)
                .collection("forum");

        addRequestSnapListenerAndCallSetupRecyclerView();

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void onClicks() {
        mFab.setOnClickListener(view -> startActivity(new Intent(getContext(), NewPostActivity.class)));
    }

    /**
     * Attaches a listener to 'associationRequestsRef',
     * handling it's changes and call setupRecyclerView
     */
    private void addRequestSnapListenerAndCallSetupRecyclerView() {
        setupRecyclerView();
//        mFeedPostsLR =
        mAssociationFeedRef.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
                Log.e(TAG, e.getMessage());
                return;
            }
            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        posts.first.add(dc.getDocument().getId());
                        posts.second.put(dc.getDocument().getId(), dc.getDocument().toObject(ForumPost.class));
                        mRecyclerAdapter.notifyDataSetChanged();
                        break;
                    case MODIFIED:
                        String modifiedId = dc.getDocument().getId();
                        posts.second.put(modifiedId, dc.getDocument().toObject(ForumPost.class));
                        //verify if it has a like or not
                        if (mRecyclerAdapter.getChangedSupportsPostsIds().contains(modifiedId)) {
                            if (mRecyclerAdapter.getChangedSupports().get(modifiedId)) {//has changed and it is a like
                                posts.second.get(modifiedId).incrementScore();
                            } else {// has changed but it itsn a like
                                posts.second.get(modifiedId).decrementScore();
                            }
                        }
                        mRecyclerAdapter.notifyDataSetChanged();
                        break;
                    case REMOVED:
                        String removedId = dc.getDocument().getId();
                        posts.first.remove(removedId);
                        posts.second.remove(removedId);
                        //remove like if needed
                        mRecyclerAdapter.getChangedSupports().remove(removedId);
                        mRecyclerAdapter.getChangedSupportsPostsIds().remove(removedId);
                        mRecyclerAdapter.notifyDataSetChanged();
                        break;
                }

                disableProgressBarAndShowRecycler();

            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerAdapter = new RecyclerViewFeedAdapter(posts, Objects.requireNonNull(getContext()).getApplicationContext());
        //mRecyclerAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mFeedRV = mView.findViewById(R.id.feedRV);
        mFeedRV.setLayoutManager(linearLayoutManager);
        mFeedRV.setAdapter(mRecyclerAdapter);

    }

    private void disableProgressBarAndShowRecycler() {
        mFeedRV.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

}
