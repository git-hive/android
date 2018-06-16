package com.hive.hive.firebaseHelpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.home.HomeFragment;

public class ReportsHelper extends FirebaseHelpers{
    //--- Reports
    public static Task<QuerySnapshot> getActiveReports(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(ASSOCIATION_COLLECTION).document(HomeFragment.mCurrentAssociationId)
                .collection(REPORTS_COLLECTION).whereEqualTo("showing", true).get();
    }
}
