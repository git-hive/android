package com.hive.hive.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.model.association.Association;

import java.util.ArrayList;

public class LoginAndSignUpFirebaseHandler {
    private final static String TAG = LoginAndSignUpFirebaseHandler.class.getSimpleName();
    public static void getAllAssoctiationsHandler(){
        LoginAndSignupHelper.getAllAssociations().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                ArrayList<Association> associations = new ArrayList<>();
                for (DocumentSnapshot dc:documentSnapshots) {
                    if(dc.exists()){
                        Association association = dc.toObject(Association.class);
                        associations.add(association);
                    }
                    //update recyclerView
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }
}
