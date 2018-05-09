package com.hive.hive.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import com.hive.hive.profiles.UserProfileActivity;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerViewHome;
    private RecyclerViewHomeAdapter mRecyclerViewHomeAdapter;
    private String TAG = HomeFragment.class.getSimpleName();

    // Views
    private ImageView mUserAvatar;
    private TextView mGreetingsTV;
    private TextView mTodayTV;
    private TextView mUserNameTV;
    private ImageView mToolbarAvatar;

    // Settings
    View mView;
    Context mContext;

    ArrayList<Object> DUMMYARRAY;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Set settings
        mView = v;
        mContext = getContext();

        setCurrentUserInfo();

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
        mRecyclerViewHomeAdapter = new RecyclerViewHomeAdapter(DUMMYARRAY);
        mRecyclerViewHome.setAdapter(mRecyclerViewHomeAdapter);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(v.getContext()));


        mGreetingsTV = v.findViewById(R.id.textViewGreetings);
        mTodayTV = v.findViewById(R.id.textViewToday);

        AppBarLayout appBarLayout = v.findViewById(R.id.home_app_bar);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout1.getTotalScrollRange()) {
                // If collapsed, then do
                mGreetingsTV.setVisibility(View.GONE);
                mTodayTV.setVisibility(View.GONE);
                mUserAvatar.setVisibility(View.GONE);
                v.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

            } else if (verticalOffset == 0) {
                mGreetingsTV.setVisibility(View.VISIBLE);
                mTodayTV.setVisibility(View.VISIBLE);
                mUserAvatar.setVisibility(View.VISIBLE);
                v.findViewById(R.id.toolbar).setVisibility(View.GONE);
            } else {
                mGreetingsTV.setVisibility(View.VISIBLE);
                mTodayTV.setVisibility(View.VISIBLE);
                mUserAvatar.setVisibility(View.VISIBLE);
                v.findViewById(R.id.toolbar).setVisibility(View.GONE);
                // Somewhere in between
                // Do according to your requirement
            }
        });


        mUserAvatar.setOnClickListener(view -> startActivity(new Intent(mContext, UserProfileActivity.class)));
        mToolbarAvatar = v.findViewById(R.id.toolbar_profile_pic);
        mToolbarAvatar.setOnClickListener(view -> startActivity(new Intent(mContext, UserProfileActivity.class)));
        mUserNameTV = v.findViewById(R.id.toolbar_profile_name);


        return v;
    }

    public void setCurrentUserInfo(){
        mUserAvatar = mView.findViewById(R.id.userAvatar);
        mGreetingsTV = mView.findViewById(R.id.textViewGreetings);
        try {
            DocumentReference userRef = DocReferences.getUserRef();
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    //mUserScore.setText(user.getScore());
                    String oldGreetings = mGreetingsTV.getText().toString().substring(0, 11);
                    mGreetingsTV.setText(oldGreetings + user.getName());
                    ProfilePhotoHelper.loadImage(mContext, mUserAvatar, user.getPhotoUrl());
                    String aux = user.getName().trim();
                    if (aux.length() > 25)
                        aux = aux.substring(0, 22) + "...";
                    mUserNameTV.setText(aux);
                    ProfilePhotoHelper.loadImage(mContext, mToolbarAvatar, user.getPhotoUrl());

                }
            });
        }catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }

    }

}
