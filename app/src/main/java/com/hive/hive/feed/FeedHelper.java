package com.hive.hive.feed;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import com.hive.hive.model.forum.ForumComment;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.forum.ForumSupport;

import static android.content.ContentValues.TAG;


/**
 * Created by birck on 10/04/18.
 */

public class FeedHelper {

    public static String ASSOCIATION_COLLECTION = "associations";
    public static String FORUM_COLLECTION = "forum";
    public static String COMMENTS_COLLECTION = "comments";
    public static String SUPPORTS_COLLECTION = "supports";


    // Forum Post
    public static Task<Void> setForumPost(FirebaseFirestore db,
                                          String associationID,
                                          String forumID,
                                          ForumPost forumPost
    ){
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumID)
                .set(forumPost);
    }

    public static Task<QuerySnapshot> getAllForumPosts(FirebaseFirestore db,
                                                       String associationID
    ){
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .get();
    }

    public static DocumentReference getForumPost(FirebaseFirestore db, String associationID, String requisitionID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(requisitionID);
    }

    //--- Support ForumPost

    public static Task<DocumentSnapshot> getForumPostSupport(
            FirebaseFirestore db,
            String associationID,
            String forumID,
            String supportID
    ) {

        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumID)
                .collection(SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    public static Task<Void> setForumPostSupport(
            FirebaseFirestore db,
            String associationID,
            String forumPostID,
            String supportID,
            final ForumSupport support
    ) {
        final DocumentReference forumPostRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID);

        final DocumentReference supportRef = forumPostRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Update forumPost score
                DocumentSnapshot forumPostSnap = transaction.get(forumPostRef);
                Double newScore = forumPostSnap.getDouble("supportScore") + 1;
                transaction.update(forumPostRef, "supportScore", newScore);

                // Create the actual support
                transaction.set(supportRef, support);

                return null;
            }
        });
    }

    public static Task<Void> removeForumPostSupport(
            FirebaseFirestore db,
            String associationID,
            String forumPostID,
            String supportID
    ) {
        final DocumentReference forumPostRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID);
        final DocumentReference supportRef = forumPostRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Update score
                DocumentSnapshot requestSnap = transaction.get(forumPostRef);
                Double newScore = requestSnap.getDouble("supportScore") - 1;
                transaction.update(forumPostRef, "supportScore", newScore);

                // Remove support
                transaction.delete(supportRef);

                return null;
            }
        });
    }

    // -- Comment ForumPost

    public static Task<Void> incrementForumPostNumComments(
            FirebaseFirestore db,
            String associationID,
            String forumPostID
    ) {
        final DocumentReference forumPostRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID);
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snap = transaction.get(forumPostRef);
                Double newNumComments = snap.getDouble("numComments") + 1;
                transaction.update(forumPostRef, "numComments", newNumComments);
                return null;
            }
        });
    }
    //--- ForumPost comment

    public static CollectionReference getAllForumPostComments(
            FirebaseFirestore db,
            String associationID,
            String forumPostID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
                .collection(COMMENTS_COLLECTION);
    }

    public static Task<DocumentSnapshot> getForumPostComment(
            FirebaseFirestore db,
            String associationID,
            String forumPostID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .get();
    }

    public static Task<Void> setForumPostComment(
            FirebaseFirestore db,
            String associationID,
            String forumPostID,
            String commentID,
            ForumComment comment
    ) {
        incrementForumPostNumComments(db, associationID, forumPostID);
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .set(comment);
    }

    public static Task<Void> deleteForumPostComment(
            FirebaseFirestore db,
            String associationID,
            String forumPostID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .delete();
    }

    //--- Support ForumPost Comment



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
    public static Task<DocumentSnapshot> getForumPostCommentSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .collection(SUPPORTS_COLLECTION)
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
    public static Task<Void> setSupportForumPostComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID,
            final ForumSupport support
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        // Set the comment support and increment the comment score
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update comment score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                Double newScore = commentSnap.getDouble(ForumComment.SCORE_FIELD);
                newScore += ForumSupport.SUPPORT_ACTION_VALUE;
                transaction.update(commentRef, ForumComment.SCORE_FIELD, newScore);

                // Set comment support
                transaction.set(supportRef, support);

                return null;
            }
        });
    }


    public static Task<Void> removeSupportForumPostComment(
            FirebaseFirestore db,
            String associationID,
            String forumPostID,
            String commentID,
            String supportID
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
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

    public static Task<Void> deleteForumPostCommentSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        // Delete the comment support and decrement the comment score
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update comment score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                Double newScore = commentSnap.getDouble(ForumComment.SCORE_FIELD);
                newScore -= ForumSupport.SUPPORT_ACTION_VALUE;
                transaction.update(commentRef, ForumComment.SCORE_FIELD, newScore);

                // Remove request comment support
                transaction.delete(supportRef);

                return null;
            }
        });
    }




}
