package com.hive.hive.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by naraujo on 1/29/18.
 * Class to store User related database methods
 */

public class UserHelper {

    private static String USER_COLLECTION = "users";

    public static Task<DocumentSnapshot> getUserData(){
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION).document(FirebaseAuth.getInstance().getUid()).get();
    }

}
