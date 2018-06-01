package com.hive.hive.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hive.hive.R;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.login.LoginActivity;
import com.hive.hive.login.LoginAndSignupHelper;
import com.hive.hive.model.association.Association;
import com.hive.hive.model.user.User;
import com.hive.hive.profiles.UserProfileActivity;
import com.hive.hive.utils.UserHelper;

public class MainFirebaseHandle {

    private final static String TAG = MainFirebaseHandle.class.getSimpleName();

    public static void getCurrentAssociation(HomeFragment fragment) {
        UserHelper.getUserData().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    //update
                    updateAssociation(user, fragment);

                }
//                else{
//                    //Logout then
//                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                //Logout then
            }
        });
    }

    private static void updateAssociation(User user, HomeFragment fragment) {
        if(user.getLastAccessAssociationRef() == null) {
            if (user.getAssociationsRef() != null && !user.getAssociationsRef().isEmpty()) {//if there is no one selected, use first association
                user.setLastAccessAssociationRef(user.getAssociationsRef().get(0)); //uses the firts ref
                updateAssociation(user, fragment);
            } else {//there is no associations, should tell user and logout
                Toast.makeText(fragment.getContext(),
                        fragment.getContext().getResources().getString(R.string.no_association), Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                fragment.getContext().startActivity(new Intent(fragment.getContext(), LoginActivity.class));
                return;
                //logout
            }
        }
        LoginAndSignupHelper.getAssociation(user.getLastAccessAssociationRef()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {//if there is a selected association use it
                    HomeFragment.mCurrentAssociationId = documentSnapshot.getId();
                    Association association = documentSnapshot.toObject(Association.class);
                    fragment.updateCurrentAssociationUI(association);
                }

            }
        });
    }
}
