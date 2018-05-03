package com.hive.hive.store;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.R;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class StoreFragment extends Fragment {

    // Settings
    Context mContext;


    private RecyclerView mRecyclerViewHome;
    private RecyclerViewStoreAdapter mRecyclerViewHomeAdapter;

    // Views
    ImageView mUserPhoto;
    TextView mUserScore;

    ArrayList<Object> DUMMYARRAY;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance() {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        DUMMYARRAY = new ArrayList<>();
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());

        mRecyclerViewHome = v.findViewById(R.id.recyclerViewFeed);
        mRecyclerViewHomeAdapter = new RecyclerViewStoreAdapter(DUMMYARRAY);
        mRecyclerViewHome.setAdapter(mRecyclerViewHomeAdapter);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(v.getContext()));

        mUserPhoto = v.findViewById(R.id.userAvatar);
        fillLoggedUserView();
        return v;
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
}
