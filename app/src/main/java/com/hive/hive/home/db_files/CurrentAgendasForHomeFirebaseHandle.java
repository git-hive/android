package com.hive.hive.home.db_files;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;

public class CurrentAgendasForHomeFirebaseHandle {
    private static final String TAG = CurrentAgendasForHomeFirebaseHandle.class.getSimpleName();

    public static EventListener<QuerySnapshot> sessionHandler(HomeFragment fragment){
        return new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case REMOVED:
                            Log.w(TAG, "No current Session");

                            fragment.removeSession();

                            break;
                        //TODO MAY show message... there is no Session
                        case ADDED:

                            fragment.addSession(dc.getDocument().getId(), dc.getDocument().toObject(Session.class));

                            break;
                        case MODIFIED:

                            fragment.updateSession(dc.getDocument().getId(), dc.getDocument().toObject(Session.class));

                            break;
                    }
                }

            }
        };

    }

    public static EventListener<QuerySnapshot> agendasHandler(HomeFragment fragment){
        return new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Agenda addedAgenda = dc.getDocument().toObject(Agenda.class);
                            String addedId = dc.getDocument().getId();

                            fragment.addAgenda(addedId, addedAgenda);

                            break;

                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            Agenda modifiedAgenda = dc.getDocument().toObject(Agenda.class);

                            fragment.updateAgenda(modifiedId, modifiedAgenda);

                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();

                            fragment.removeAgenda(removedId);
                            break;
                    }
                }

            }
        };
    }

    public static EventListener<QuerySnapshot> questionsHanderl(HomeFragment fragment){
        return new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for(DocumentChange dc : documentSnapshots.getDocumentChanges()){
                    switch (dc.getType()){
                        case ADDED:
                            String questionId = dc.getDocument().getId();
                            Question question = dc.getDocument().toObject(Question.class);

                            fragment.addQuestions(questionId, question);
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            Question modifiedQuestion = dc.getDocument().toObject(Question.class);

                            fragment.updateQuestions(modifiedId, modifiedQuestion);

                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();

                            fragment.removeQuestions(removedId);
                            break;
                    }
                }
            }
        };
    }

}
