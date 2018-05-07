package com.hive.hive.profiles;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hive.hive.model.user.User;

public class ProfilesFirebaseHandle {

    private static final String TAG = ProfilesFirebaseHandle.class.getSimpleName();

    public static void getUser(DocumentReference userRef, Activity activity){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    ((UserProfileActivity) activity).updateUI(user);
                }
                //TODO should do something in case it doesnt find user
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }
}
