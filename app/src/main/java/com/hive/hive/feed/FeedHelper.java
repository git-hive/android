package com.hive.hive.feed;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hive.hive.model.association.AssociationSupport;
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

    //--- Support Request

    public static Task<DocumentSnapshot> getForumPostSupport(
            FirebaseFirestore db,
            String associationID,
            String forumID,
            String supportID
    ) {
        Log.d(TAG, String.valueOf(forumID)+"((((((())))))))) &&&& ***** ¨¨¨¨ %%%%% $$$$ #### @@@@ !!!!");

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
                Double newScore = forumPostSnap.getDouble("score") + 1;
                transaction.update(forumPostRef, "score", newScore);

                // Create the actual support
                transaction.set(supportRef, support);

                return null;
            }
        });
    }

    public static Task<Void> removeForumPostSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(FORUM_COLLECTION)
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




}
