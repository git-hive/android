package com.hive.hive.firebaseHelpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.forum.ForumComment;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.forum.ForumSupport;


public class FeedHelper extends FirebaseHelpers{


    // Forum Post
    public static Task<Void> setForumPost(String forumID,
                                          ForumPost forumPost
    ){
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(forumID)
                .set(forumPost);
    }


    public static DocumentReference getForumPost(String postID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(postID);
    }

    //--- Support ForumPost

    public static Task<DocumentSnapshot> getForumPostSupport(
            String forumID,
            String supportID
    ) {

        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(forumID)
                .collection(SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    public static Task<Void> setForumPostSupport(
            String forumPostID,
            String supportID,
            final ForumSupport support
    ) {
        final DocumentReference forumPostRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(forumPostID);

        final DocumentReference supportRef = forumPostRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        return db.runTransaction(transaction -> {
            // Update forumPost score
            DocumentSnapshot forumPostSnap = transaction.get(forumPostRef);
            DocumentSnapshot supportPostSnap = transaction.get(supportRef);
            Double newScore;
            if(supportPostSnap.exists()){//shoul delete support
                newScore = forumPostSnap.getDouble("supportScore") - 1;
                transaction.delete(supportRef);
            }else{//should add support
                newScore = forumPostSnap.getDouble("supportScore") + 1;
                // Create the actual support
                transaction.set(supportRef, support);
            }
            transaction.update(forumPostRef, "supportScore", newScore);


            return null;
        });
    }
    // -- Comment ForumPost

    private static void incrementForumPostNumComments(
            String forumPostID
    ) {
        final DocumentReference forumPostRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(forumPostID);
        db.runTransaction(transaction -> {
            DocumentSnapshot snap = transaction.get(forumPostRef);
            Double newNumComments = snap.getDouble("numComments") + 1;
            transaction.update(forumPostRef, "numComments", newNumComments);
            return null;
        });
    }
    //--- ForumPost comment

    public static CollectionReference getAllForumPostComments(
            String forumPostID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
                .collection(COMMENTS_COLLECTION);
    }


    public static void setForumPostComment(
            String forumPostID,
            String commentID,
            ForumComment comment
    ) {
        incrementForumPostNumComments(forumPostID);
        db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(forumPostID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .set(comment);
    }


    //--- Support ForumPost Comment


    public static Task<DocumentSnapshot> getForumPostCommentSupport(
            String requestID,
            String commentID,
            String supportID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID)
                .collection(SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }


    public static Task<Void> setSupportForumPostComment(
            String requestID,
            String commentID,
            String supportID,
            final ForumSupport support
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(HomeFragment.mCurrentAssociationId)
                .collection(FORUM_COLLECTION)
                .document(requestID)
                .collection(COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(SUPPORTS_COLLECTION)
                .document(supportID);

        // Set the comment support and increment the comment score
        return db.runTransaction(transaction -> {
            // Get and update comment score
            DocumentSnapshot commentSnap = transaction.get(commentRef);
            DocumentSnapshot supportPostSnap = transaction.get(supportRef);
            Double newScore = commentSnap.getDouble(ForumComment.SCORE_FIELD);

            if(supportPostSnap.exists()){//shoul delete support
                newScore = commentSnap.getDouble("supportScore") - 1;
                transaction.delete(supportRef);
            }else{//should add support
                newScore = commentSnap.getDouble("supportScore") + 1;
                // Create the actual support
                transaction.set(supportRef, support);
            }
            transaction.update(commentRef, "supportScore", newScore);

            return null;
        });
    }

}
