package com.hive.hive.association.votes;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by vplentz on 22/03/18.
 */

public class VotesHelper {
    public static String ASSOCIATION_COLLECTION = "associations";

    public static String QUESTIONS_COLLECTION = "questions";

    public static String SESSIONS_COLLECTION = "sessions";

    public static String AGENDAS_COLLECTION = "agendas";

    //--- Sessions

    //TODO ADD WHERE CLAUSE

    public static Query getCurrentSession(FirebaseFirestore db, String associationID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(SESSIONS_COLLECTION)
                .whereEqualTo("status", "Current");
    }

    //--- Agendas
    public static CollectionReference getAgendas(FirebaseFirestore db, String associationID, String sessionID){
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(AGENDAS_COLLECTION);
    }

    //--- Questions
    public static CollectionReference getQuestions(FirebaseFirestore db, String associationID, String sessionID, String agendaID){
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(AGENDAS_COLLECTION)
                .document(agendaID)
                .collection((QUESTIONS_COLLECTION));
    }
}
