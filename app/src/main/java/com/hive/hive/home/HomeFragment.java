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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.main.MainActivity;
import com.hive.hive.main.MainFirebaseHandle;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Report;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Session;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.user.User;
import com.hive.hive.profiles.UserProfileActivity;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;

import java.util.ArrayList;
import java.util.HashMap;

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

        MainFirebaseHandle.getAssociations(mUser,this);
        setCurrentUserInfo();

        initStructures();

//        initDummy();

//        initRecycler();

        initOnClicks();

//        initEventListeners();


//        mSessionLR = VotesHelper.getCurrentSession(FirebaseFirestore.getInstance(), HomeFragment.mCurrentAssociationId).addSnapshotListener(mSessionEL);

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

    public void initStructures(){
        mRecyclerObjects = new ArrayList<>();
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

                }
            });
        }catch (NullPointerException e){
            Log.e(TAG, e.getMessage());
        }

    }

    public void initRecycler(ArrayList<Object> objects){
        RecyclerViewHomeAdapter adapter = new RecyclerViewHomeAdapter(objects);
        mRecyclerViewHome = mView.findViewById(R.id.recyclerViewFeed);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRecyclerViewHome.setAdapter(adapter);
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
        mCurrentAssociationSpinner = mView.findViewById(R.id.currentAssociationSpinner);

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
                           startActivity(new Intent(HomeFragment.this.getContext(), MainActivity.class));
                       }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
