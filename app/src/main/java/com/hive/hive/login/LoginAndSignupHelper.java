package com.hive.hive.login;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginAndSignupHelper {

    public static String ASSOCIATION_COLLECTION = "associations";

    public static Task<QuerySnapshot> getAllAssociations(){
        return FirebaseFirestore.getInstance().collection(ASSOCIATION_COLLECTION).get();
    }
}
