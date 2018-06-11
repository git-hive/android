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

    //--- Request

    public static CollectionReference getAllRequests(FirebaseFirestore db, String associationID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION);
    }

    public static DocumentReference getRequest(FirebaseFirestore db, String associationID, String requisitionID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requisitionID);
    }

    public static Task<Void> deleteRequest(FirebaseFirestore db, String associationID, String requestID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .delete();
    }


    /**
     * Sets an association request document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to set the request to
     * @param requestID     Request document ID to be set
     * @param request       Document to be set under the provided id
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
                DocumentSnapshot supportSnap = transaction.get(supportRef);
                Double newScore;
                if(supportSnap.exists()){//should delete vote
                    newScore = requestSnap.getDouble("score") - 1;
                    transaction.delete(supportRef);
                }else{//should add vote
                    newScore = requestSnap.getDouble("score") + 1;
                    // Create the actual support
                    transaction.set(supportRef, support);
                }
                transaction.update(requestRef, "score", newScore);
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

    public static Task<Void> incrementRequestNumComments(
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
                Double newNumComments = snap.getDouble("numComments") + 1;
                transaction.update(requestRef, "numComments", newNumComments);
                return null;
            }
        });
    }
    //--- Request comment

    public static CollectionReference getAllRequestComments(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION);
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
        incrementRequestNumComments(db, associationID, requestID);
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

    //--- Support Request Comment

    /**
     * Gets a support from a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID where to get the supports from
     * @param supportID     Support document ID where to get the document
     * @return Task that resolves in all support documents from a request comment
     */
    public static Task<DocumentSnapshot> getRequestCommentSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    /**
     * Sets a support on a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID where to set the support to
     * @param supportID     Support document ID to be set
     * @param support       Document to be set under the provided support ID
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
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID);

        // Set the comment support and increment the comment score
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update comment score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                DocumentSnapshot supportSnap = transaction.get(supportRef);
                Double newScore;
                if(supportSnap.exists()){//should decrease score and remove support
                    newScore = commentSnap.getDouble(AssociationComment.SCORE_FIELD) - 1;
                    transaction.delete(supportRef);
                }else{//should increaseScore and add support
                    newScore = commentSnap.getDouble(AssociationComment.SCORE_FIELD) + 1;
                    transaction.set(supportRef, support);
                }
                transaction.update(commentRef, AssociationComment.SCORE_FIELD, newScore);

                return null;
            }
        });
    }


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

    //TRANSACTIONS CATEGORIES
    /**
     * Fetches all association budget transaction categories
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the categories from
     * @return Task that resolves in all budget transaction categories documents
     */
    public static Task<QuerySnapshot> getAllBudgetTransactionCategories(
            FirebaseFirestore db,
            String associationID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(BUDGET_CATEGORIES_COLLECTION)
                .get();
    }

    /**
     * Fetches all request categories from an association
     *
     * @param db            Database reference
     * @param associationID Association document ID where to set the categories from
     * @return Task that resolves in all request categories documents
     */
    public static Task<QuerySnapshot> getAllRequestCategories(
            FirebaseFirestore db,
            String associationID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(REQUEST_CATEGORIES_COLLECTION)
                .get();
    }

    //--- Reports
    public static Task<QuerySnapshot> getActiveReports(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(ASSOCIATION_COLLECTION).document(HomeFragment.mCurrentAssociationId)
                .collection(REPORTS_COLLECTION).whereEqualTo("showing", true).get();
    }
}
