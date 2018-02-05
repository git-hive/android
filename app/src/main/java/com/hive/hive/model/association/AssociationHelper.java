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
    public static String ASSOCIATION_COLLECTION = "associations";
    public static String REQUESTS_COLLECTION = "requests";
    public static String COMMENTS_COLLECTION = "comments";
    public static String SUPPORTS_COLLECITON = "supports";

    public static Task<QuerySnapshot> getAllRequests(FirebaseFirestore db, String associationID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .get();
    }

    public static Task<DocumentSnapshot> getRequest(FirebaseFirestore db, String associationID, String requisitionID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requisitionID)
                .get();
    }

    public static Task<Void> deleteRequest(FirebaseFirestore db, String associationID, String requestID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
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
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .set(request);
    }

    public static Task<DocumentSnapshot> getRequestSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(SUPPORTS_COLLECITON)
                .document(supportID)
                .get();
    }

    public static Task<Void> setRequestSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID,
            final AssociationSupport support
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID);

        final DocumentReference supportRef = requestRef
                .collection(SUPPORTS_COLLECITON)
                .document(supportID);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Update request score
                DocumentSnapshot requestSnap = transaction.get(requestRef);
                Double newScore = requestSnap.getDouble("score") + 1;
                transaction.update(requestRef, "score", newScore);

                // Create the actual support
                transaction.set(supportRef, support);

                return null;
            }
        });
    }

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
                .collection(SUPPORTS_COLLECITON)
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

    public static Task<QuerySnapshot> getAllRequestComments(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .get();
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

    public static Task<Void> setRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            AssociationComment comment
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .set(comment);
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

    public static Task<Void> supportRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID,
            final AssociationSupport support
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(SUPPORTS_COLLECITON)
                .document(supportID);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                Double newScore = commentSnap.getDouble("score") + 1;
                transaction.update(commentRef, "score", newScore);

                // Set the actual comment support
                transaction.set(supportRef, support);

                return null;
            }
        });
    }
}
