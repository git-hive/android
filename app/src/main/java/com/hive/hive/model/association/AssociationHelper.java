package com.hive.hive.model.association;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

/**
 * Created by naraujo on 1/29/18.
 */

public class AssociationHelper {
    public static String COLLECTION_NAME = "associations";
    public static String REQUESTS_SUB_COLLECTION_NAME = "requests";

    public static Task<QuerySnapshot> getAllRequests(FirebaseFirestore db, String associationID) {
        return db
                .collection(COLLECTION_NAME)
                .document(associationID)
                .collection(REQUESTS_SUB_COLLECTION_NAME)
                .get();
    }

    public static Task<DocumentSnapshot> getRequest(FirebaseFirestore db, String associationID, String requisitionID) {
        return db
                .collection(COLLECTION_NAME)
                .document(associationID)
                .collection(REQUESTS_SUB_COLLECTION_NAME)
                .document(requisitionID)
                .get();
    }

    public static Task<Void> deleteRequest(FirebaseFirestore db, String associationID, String requestID) {
        return db
                .collection(COLLECTION_NAME)
                .document(associationID)
                .collection(REQUESTS_SUB_COLLECTION_NAME)
                .document(requestID)
                .delete();
    }

    public static Task<Void> setRequest(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            Request request
    ) {
        return db
                .collection(COLLECTION_NAME)
                .document(associationID)
                .collection(REQUESTS_SUB_COLLECTION_NAME)
                .document(requestID)
                .set(request);
    }

    public static Task<Void> incrementRequestScore(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        final DocumentReference requestRef = db
                .collection(COLLECTION_NAME)
                .document(associationID)
                .collection(REQUESTS_SUB_COLLECTION_NAME)
                .document(requestID);
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snap = transaction.get(requestRef);
                Double newScore = snap.getDouble("score") + 1;
                transaction.update(requestRef, "score", newScore);
                return null;
            }
        });
    }
}
