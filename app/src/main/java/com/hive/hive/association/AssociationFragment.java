package com.hive.hive.association;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.R;
import com.hive.hive.association.request.RequestActivity;
import com.hive.hive.association.transparency.TransparencyActivity;
import com.hive.hive.association.votes.VotesActivity;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;

import static android.content.ContentValues.TAG;


public class AssociationFragment extends Fragment {

    // Configs
    Context mContext;

    // Views
    ImageView mUserPhoto;

    // Menu Buttons
    ImageButton requestBT;
    ImageButton votesBT;
    ImageButton transparencyBT;

    public AssociationFragment() {
        // Required empty public constructor
    }

    public static AssociationFragment newInstance() {
        AssociationFragment fragment = new AssociationFragment();
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
        final View v = inflater.inflate(R.layout.fragment_association, container, false);

        mContext = getContext();
        requestBT = v.findViewById(R.id.requestsBT);
        votesBT = v.findViewById(R.id.voteBT);
        transparencyBT = v.findViewById(R.id.trasnparencyBT);

        mUserPhoto = v.findViewById(R.id.userAvatar);


        requestBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), RequestActivity.class));
            }
        });

        votesBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), VotesActivity.class));
            }
        });

        transparencyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), TransparencyActivity.class));
            }
        });

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
