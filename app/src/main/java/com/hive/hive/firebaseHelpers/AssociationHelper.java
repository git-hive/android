package com.hive.hive.firebaseHelpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AssociationHelper extends FirebaseHelpers {

    public static Task<DocumentSnapshot> getAssociation(DocumentReference associationRef){
        return associationRef.get();
    }
    public static Task<QuerySnapshot> getAllAssociations() {
        return FirebaseFirestore.getInstance().collection(ASSOCIATION_COLLECTION).get();
    }

}
