package com.hive.hive.firebaseHelpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.home.HomeFragment;

public class FilesHelper extends FirebaseHelpers{
    //class used to get transparency storage files

    public static Task<QuerySnapshot> getFiles(){
        return FirebaseFirestore.getInstance().collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FILES_COLLECTION).get();
    }
}
