package com.hive.hive.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hive.hive.R;
import com.hive.hive.firebaseHelpers.AssociationHelper;
import com.hive.hive.firebaseHelpers.UserHelper;
import com.hive.hive.model.association.Association;
import com.hive.hive.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginAndSignUpFirebaseHandler {
    private final static String TAG = LoginAndSignUpFirebaseHandler.class.getSimpleName();

    public static void getAllAssoctiationsHandler(Activity activity){
        AssociationHelper.getAllAssociations().addOnSuccessListener(documentSnapshots -> {
            Pair<ArrayList<String>, HashMap<String, Association>> associations = new Pair<>(new ArrayList<>(), new HashMap<>());
            for (DocumentSnapshot dc:documentSnapshots) {
                if(dc.exists()){
                    String associationId = dc.getId();
                    Association association = dc.toObject(Association.class);
                    associations.first.add(associationId);
                    associations.second.put(associationId, association);
                }
                //update recyclerView
                if(activity instanceof  SignupActivity){
                    ((SignupActivity) activity).setupRecyclerView(associations);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    public static void saveUserDataAndSendSelectedIngressesRequests(Activity activity, User user, ArrayList<String> selectedAssociationsIds) {
            try {
                UserHelper.saveUserAndSendRequestIngress(user, selectedAssociationsIds)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (activity instanceof SignupActivity) {
                                    ((SignupActivity) activity).startHome();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage());
                        if (activity instanceof SignupActivity) {
                            Toast.makeText(activity, activity.getResources().getString(R.string.signup_problem), Toast.LENGTH_SHORT).show();
                            ((SignupActivity) activity).startLogin();
                        }
                    }
                });
            }catch (NullPointerException e){
                Log.e(TAG, e.getMessage());
                if (activity instanceof SignupActivity) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.signup_problem), Toast.LENGTH_SHORT).show();
                    ((SignupActivity) activity).startLogin();
                }
            }
    }
}
