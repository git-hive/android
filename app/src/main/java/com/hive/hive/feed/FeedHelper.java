package com.hive.hive.feed;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.model.forum.ForumPost;


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
        return db.collection(ASSOCIATION_COLLECTION).document(associationID).collection(FORUM_COLLECTION).document(forumID).set(forumPost);
    }

    public static Task<QuerySnapshot> getAllForumPosts(FirebaseFirestore db,
                                                       String associationID
    ){
        return db.collection(ASSOCIATION_COLLECTION).document(associationID).collection(FORUM_COLLECTION).get();
    }




}
