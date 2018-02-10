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
    public static String SUPPORTS_COLLECTION = "supports";

    //--- Request

    /**
     * Fetches all requests
     * @param db Database reference
     * @param associationID Document ID where to get requests from
     * @return Task that results in all user actions documents
     */
    public static Task<QuerySnapshot> getAllRequests(FirebaseFirestore db, String associationID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single request document
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID
     * @return Task that results in the request document
     */
    public static Task<DocumentSnapshot> getRequest(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .get();
    }

    /**
     * Deletes a request document
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID
     * @return Empty task that resolves successfully if the document was removed
     */
    public static Task<Void> deleteRequest(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .delete();
    }

    /**
     * Sets an association request document
     * @param db Database reference
     * @param associationID Association document ID where to set the request to
     * @param requestID Request document ID to be set
     * @param request Document to be set under the provided id
     * @return Empty task that resolves successfully if the document was set
     */
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

    //--- Support Request

    /**
     * Fetches a single request support document
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request ID where to get the support from
     * @param supportID Support document ID to be fetched
     * @return Task that results in the request document
     */
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
                .collection(SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    /**
     * Sets and request support document
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID to be supported
     * @param supportID Support document ID to be set
     * @param support Support document to be set under the support ID provided
     * @return Empty task that resolves successfully if the document was set
     */
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
                .collection(SUPPORTS_COLLECTION)
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

    /**
     * Removes a request support
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to
     * @param supportID Support document ID to deleted
     * @return Empty task that resolves successfully if the document was removed
     */
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

    /**
     * Increments the request score by one unit
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID who's score will be incremented
     * @return Empty task that resolves successfully if the score was incremented
     */
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

    //--- Request comment

    /**
     * Fetches all request comments
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comments from
     * @return Task that results in all request comment documents
     */
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

    /**
     * Fetches a single request comment
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comment from
     * @param commentID Comment document ID
     * @return Task that results in the request comment document
     */
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

    /**
     * Sets a request comment
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comment from
     * @param commentID Comment document ID
     * @param comment Comment document to be set under the comment ID provided
     * @return Empty task that resolves successfully if the document was set
     */
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

    /**
     * Deletes a request comment
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comment from
     * @param commentID Comment document ID
     * @return Empty task that resolves successfully if the document was deleted
     */
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

    /**
     * Gets all supports from a request comment
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comment from
     * @param commentID Comment document ID where to get the supports from
     * @return Task that results in all request comment supports documents
     */
    public static Task<QuerySnapshot> getAllRequestCommentSupports(
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
                .collection(SUPPORTS_COLLECTION)
                .get();
    }

    /**
     * Sets a support on a request comment
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comment from
     * @param commentID Comment document ID where to set the support to
     * @param supportID Support document ID to be set
     * @param support Document to be set under the provided support ID
     * @return Empty task that resolves successfully if document was set
     */
    public static Task<Void> setRequestCommentSupport(
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
                .collection(SUPPORTS_COLLECTION)
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

    /**
     * Deletes a support from a request comment
     * @param db Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID Request document ID where to get the comment from
     * @param commentID Comment document ID where to set the support to
     * @param supportID Support document ID to be deleted
     * @return Empty task that resolves successfully if document was deleted
     */
    public static Task<Void> deleteRequestCommentSupport(
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
}
