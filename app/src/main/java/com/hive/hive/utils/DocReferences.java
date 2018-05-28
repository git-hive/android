package com.hive.hive.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by vplentz on 21/02/18.
 */

public class DocReferences {
    private static String USERS_COLLECTION = "users";
    private static String ASSOCIATIONS_COLLECTION = "associations";
    private static String REQUESTS_COLLECTION = "requests";
    private static String SESSIONS_COLLECTION = "sessions";
    private static String AGENDAS_COLLECTION = "agendas";
    private static String QUESTIONS_COLLECTION = "questions";
    private static String VOTES_COLLECTION = "votes";
    private static String FORUM_COLLECTION = "forum";


    public static DocumentReference getUserRef(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return  db.collection(USERS_COLLECTION)
                .document(user.getUid());
        return null;
    }
    public static DocumentReference getAssociationRef(String associationId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //TODO HARDCODED ASSOCIATION
        return  db.collection(ASSOCIATIONS_COLLECTION)
                    .document(associationId);
    }
    public static DocumentReference getRequestRef(String associationId, String requestId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db
                .collection(ASSOCIATIONS_COLLECTION)
                .document(associationId)
                .collection(REQUESTS_COLLECTION)
                .document(requestId);
    }
    public static DocumentReference getPostRef(String associationId, String postId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db
                .collection(ASSOCIATIONS_COLLECTION)
                .document(associationId)
                .collection(FORUM_COLLECTION)
                .document(postId);
    }
    public static CollectionReference getVotersRef(String associationId, String sessionId, String agendaId, String questionId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db
                .collection(ASSOCIATIONS_COLLECTION)
                .document(associationId)
                .collection(SESSIONS_COLLECTION)
                .document(sessionId)
                .collection(AGENDAS_COLLECTION)
                .document(agendaId)
                .collection(QUESTIONS_COLLECTION)
                .document(questionId)
                .collection(VOTES_COLLECTION);
    }


}
