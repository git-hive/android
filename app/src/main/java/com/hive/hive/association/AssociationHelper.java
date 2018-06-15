package com.hive.hive.association;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.association.AssociationComment;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.association.Request;

/**
 * Created by naraujo on 1/29/18.
 */

public class AssociationHelper {
    public static String ASSOCIATION_COLLECTION = "associations";
    public static String REQUESTS_COLLECTION = "requests";
    public static String COMMENTS_COLLECTION = "comments";
    public static String SUPPORTS_COLLECTION = "supports";
    public static String BUDGET_CATEGORIES_COLLECTION = "budgetCategories";
    public static String REQUEST_CATEGORIES_COLLECTION = "requestCategories";
    public static String REPORTS_COLLECTION = "reports";


    public static Task<Void> removeRequestSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID);
        final DocumentReference supportRef = requestRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Update score
                DocumentSnapshot requestSnap = transaction.get(requestRef);
                Double newScore = requestSnap.getDouble("score") - 1;
                transaction.update(requestRef, "score", newScore);

                // Remove support
                transaction.delete(supportRef);

                return null;
            }
        });
    }

    public static Task<Void> incrementRequestScore(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
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

    public static Task<DocumentSnapshot> getRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .get();
    }


    public static Task<Void> deleteRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .delete();
    }

    //--- Support Request Comment


    public static Task<Void> removeSupportRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                Double newScore = commentSnap.getDouble("score") - 1;
                transaction.update(commentRef, "score", newScore);

                // Remove comment support
                transaction.delete(supportRef);

                return null;
            }
        });
    }

    //--- Reports
    public static Task<QuerySnapshot> getActiveReports(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(ASSOCIATION_COLLECTION).document(HomeFragment.mCurrentAssociationId)
                .collection(REPORTS_COLLECTION).whereEqualTo("showing", true).get();
    }
}
