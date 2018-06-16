package com.hive.hive.main;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.R;
import com.hive.hive.firebaseHelpers.AssociationHelper;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.login.LoginActivity;
import com.hive.hive.model.association.Association;
import com.hive.hive.model.user.User;

import java.util.ArrayList;

public class MainFirebaseHandle {

    private final static String TAG = MainFirebaseHandle.class.getSimpleName();

    public static void getCurrentAssociation(Activity activity) {
        com.hive.hive.firebaseHelpers.UserHelper.getUserData().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                //update
                updateAssociation(user, activity);

            }
            else{
                if(activity instanceof LoginActivity)
                    ((LoginActivity) activity).checkoutSignUp();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, e.getMessage());
            //Logout then
        });
    }

    private static void updateAssociation(User user, Activity activity) {
        if(user.getLastAccessAssociationRef() == null) {
            if (user.getAssociationsRef() != null && !user.getAssociationsRef().isEmpty()) {//if there is no one selected, use first association
                user.setLastAccessAssociationRef(user.getAssociationsRef().get(0)); //uses the firts ref
                updateAssociation(user, activity);
                return;
            } else {//there is no associations, should tell user and logout
                Toast.makeText(activity,
                        activity.getResources().getString(R.string.no_association), Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                activity.startActivity(new Intent(activity, LoginActivity.class));
                return;
                //logout
            }
        }
        AssociationHelper.getAssociation(user.getLastAccessAssociationRef()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {//if there is a selected association use it
                HomeFragment.mCurrentAssociationId = documentSnapshot.getId();
                HomeFragment.mUser = user;
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }

        });
    }

//
//    public static void getCurrentAssociation(HomeFragment fragment) {
//        LoginAndSignupHelper.getAssociation(DocReferences.getAssociationRef(HomeFragment.mCurrentAssociationId)).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {//if there is a selected association use it
//                    HomeFragment.mCurrentAssociationId = documentSnapshot.getId();
//                    Association association = documentSnapshot.toObject(Association.class);
//                    if(fragment != null)
//                        fragment.updateCurrentAssociationUI(association);
//                }
//
//            }
//        });
//    }

    public static void getAssociations(User user, HomeFragment fragment){
        ArrayList<Pair<String, String>> associations = new ArrayList<>();//id and name
        for(DocumentReference ref : user.getAssociationsRef()){
            AssociationHelper.getAssociation(ref).addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String associationId = documentSnapshot.getId();
                    Association association = documentSnapshot.toObject(Association.class);
                    associations.add(new Pair<>(associationId, association.getName()));
                    callUI(associations, user, fragment);
                }

            });
        }

    }

    private static void callUI(ArrayList<Pair<String, String>> associations, User user, HomeFragment fragment){
        if(associations.size() == user.getAssociationsRef().size())
            if(fragment != null)
                fragment.updateCurrentAssociationUI(associations);
    }

}
