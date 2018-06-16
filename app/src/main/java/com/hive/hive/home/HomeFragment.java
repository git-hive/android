package com.hive.hive.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.R;
import com.hive.hive.firebaseHelpers.UserHelper;
import com.hive.hive.main.MainActivity;
import com.hive.hive.main.MainFirebaseHandle;
import com.hive.hive.model.user.User;
import com.hive.hive.profiles.UserProfileActivity;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment {
    private boolean first = true;//used to verify if there is need to create a adapter

    public static String mCurrentAssociationId;
    public static  User mUser;
    private RecyclerView mRecyclerViewHome;
    private RecyclerViewHomeAdapter mRecyclerViewHomeAdapter;
    private String TAG = HomeFragment.class.getSimpleName();
    // Views
    ImageView mUserAvatar;
    TextView mGreetingsTV;
    Spinner mCurrentAssociationSpinner;
    CircleProgressBar mHomePB;

    // Settings
    View mView;
    Context mContext;

    // Data
    ArrayList<Object> mRecyclerObjects;



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

        initViews();

        MainFirebaseHandle.getAssociations(mUser,this);

        setCurrentUserInfo();


        initStructures();

        initOnClicks();

        showProgress();

        HomeFirebaseHandle.getSession(this, mRecyclerObjects);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        first = true;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews(){
        mHomePB = mView.findViewById(R.id.homePB);
        mCurrentAssociationSpinner = mView.findViewById(R.id.currentAssociationSpinner);
        mRecyclerViewHome = mView.findViewById(R.id.recyclerViewFeed);
        mUserAvatar = mView.findViewById(R.id.userAvatar);
        mGreetingsTV = mView.findViewById(R.id.textViewGreetings);

    }
    public void initStructures(){
        mRecyclerObjects = new ArrayList<>();
    }

    public void setCurrentUserInfo(){
        try {
            DocumentReference userRef = DocReferences.getUserRef();
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    //mUserScore.setText(user.getScore());
                    String oldGreetings = mGreetingsTV.getText().toString().substring(0, 11);
                    mGreetingsTV.setText(oldGreetings + user.getName());
                    ProfilePhotoHelper.loadImage(mContext, mUserAvatar, user.getPhotoUrl());

                }
            });
        }catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }

    }

    public void initRecycler(ArrayList<Object> objects){
        RecyclerViewHomeAdapter adapter = new RecyclerViewHomeAdapter(objects, mContext);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRecyclerViewHome.setAdapter(adapter);
        hideProgress();
    }

    public void initOnClicks(){
        mUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, UserProfileActivity.class));
            }
        });
    }

    public void updateCurrentAssociationUI(ArrayList<Pair<String, String>> associations){


        ArrayList<String> associationNames = new ArrayList<>();
        for(Pair<String, String> association : associations){
            if(association.first.equals(mCurrentAssociationId))//the first item is the current association
                associationNames.add(0, association.second);
            else{
                associationNames.add(association.second);}

        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, associationNames);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        mCurrentAssociationSpinner.setAdapter(adapter);

        mCurrentAssociationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = mCurrentAssociationSpinner.getSelectedItem().toString();
                for(Pair<String, String> association : associations){
                    if(association.second.equals(selected))
                       if(!association.first.equals(mCurrentAssociationId)) {
                           mCurrentAssociationId = association.first;
                           mUser.setLastAccessAssociationRef(DocReferences.getAssociationRef(mCurrentAssociationId));
                           UserHelper.saveUserLastAssociation(mUser);
                           startActivity(new Intent(HomeFragment.this.getContext(), MainActivity.class));

                       }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showProgress(){
        mHomePB.setVisibility(View.VISIBLE);
        mCurrentAssociationSpinner.setVisibility(View.GONE);
        mGreetingsTV.setVisibility(View.GONE);
        mRecyclerViewHome.setVisibility(View.GONE);
    }

    private void hideProgress(){
        mHomePB.setVisibility(View.GONE);
        mCurrentAssociationSpinner.setVisibility(View.VISIBLE);
        mGreetingsTV.setVisibility(View.VISIBLE);
        mRecyclerViewHome.setVisibility(View.VISIBLE);
    }
}
