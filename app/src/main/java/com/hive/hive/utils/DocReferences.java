package com.hive.hive.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by vplentz on 21/02/18.
 */

public class DocReferences {
    private static String USERS_COLLECTION = "users";
    private static String ASSOCIATION_COLLECTION = "associations";

    public static DocumentReference getUserRef(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return  db.collection(USERS_COLLECTION)
                .document(user.getUid());
        return null;
    }
    public static DocumentReference getAssociationRef(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //TODO HARDCODED ASSOCIATION
        return  db.collection(ASSOCIATION_COLLECTION)
                    .document("gVw7dUkuw3SSZSYRXe8s");
    }
}