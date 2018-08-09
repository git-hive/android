package com.hive.hive.association.votes.current;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;

public class CurrentAgendaFirebaseHandle {
    private static final String TAG = CurrentAgendaFirebaseHandle.class.getSimpleName();

    public static EventListener<QuerySnapshot> sessionHandler(CurrentFragment fragment){
        return new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                if(documentSnapshots.isEmpty())
                    fragment.noAgendas();
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case REMOVED:
                            Log.w(TAG, "No current Session");

                            fragment.removeSession();

                            break;
                        //TODO MAY show message... there is no Session
                        case ADDED:
                            Log.d(TAG, "addin a session");
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

    public static EventListener<QuerySnapshot> agendasHandler(CurrentFragment fragment){
        return new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
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
                            Log.d(TAG, "added agenda");
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

    public static EventListener<QuerySnapshot> questionsHanderl(CurrentFragment fragment){
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
