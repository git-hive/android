package com.hive.hive.firebaseHelpers;

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

public class RequestsHelper extends FirebaseHelpers{
    public static DocumentReference getRequest(String requestId) {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestId);
    }
    public static Task<Void> setRequest(String requestID, Request request) {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .set(request);
    }

    //--- Support Request

    public static Task<DocumentSnapshot> getRequestSupport(
            String requestID,
            String supportID
    ) {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    public static Task<Void> setRequestSupport(
            String requestID,
            String supportID,
            final AssociationSupport support
    ) {
        final DocumentReference requestRef = FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID);

        final DocumentReference supportRef = requestRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        return FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
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

    //--- Request comment

    public static CollectionReference getAllRequestComments(String requestID) {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION);
    }

    public static Task<Void> incrementRequestNumComments(String requestID) {
        final DocumentReference requestRef = FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID);
        return FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
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
    public static Task<Void> setRequestComment(
            String requestID,
            String commentID,
            AssociationComment comment
    ) {
        incrementRequestNumComments(requestID);
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .set(comment);
    }

    public static Task<DocumentSnapshot> getRequestCommentSupport(
            String requestID,
            String commentID,
            String supportID
    ) {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    public static Task<Void> setRequestCommentSupport(
            String requestID,
            String commentID,
            String supportID,
            final AssociationSupport support
    ) {
        final DocumentReference commentRef = FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID);

        // Set the comment support and increment the comment score
        return FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
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

    //TRANSACTION CATEGORIES

    public static Task<QuerySnapshot> getAllBudgetTransactionCategories() {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(BUDGET_CATEGORIES_COLLECTION)
                .get();
    }
// REQUEST CATEGORIES
    public static Task<QuerySnapshot> getAllRequestCategories() {
        return FirebaseFirestore.getInstance()
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(REQUEST_CATEGORIES_COLLECTION)
                .get();
    }


}
