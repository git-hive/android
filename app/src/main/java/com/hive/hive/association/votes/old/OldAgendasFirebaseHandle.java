package com.hive.hive.association.votes.old;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class OldAgendasFirebaseHandle {
    private final static String  TAG = OldAgendasFirebaseHandle.class.getSimpleName();

    public static void getPastSessions(String associationId, OldFragment fragment){
        ArrayList<String> sessionsIds = new ArrayList<>();
        final Session[] session = new Session[1];
        VotesHelper.getPastSessions(FirebaseFirestore.getInstance(), associationId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for(DocumentSnapshot dc : documentSnapshots){
                            if(dc.exists()){
                                String id = dc.getId();
                                session[0] = dc.toObject(Session.class);
                                sessionsIds.add(id);
                            }
                        }
                       // FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").add(session[0]);
                        getPastAgendas(associationId, sessionsIds, fragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage());
                        //TODO SHOULD FAIL
                    }
                });
    }

    public static void getPastAgendas(String associationId, ArrayList<String> pastSessionsIds, OldFragment fragment){
        Pair<ArrayList<DocumentSnapshot>, HashMap<String, Agenda>> agendasPair= new Pair<>(new ArrayList<>(), new HashMap<>());
        HashMap<String, String> agendaAndSessionIds = new HashMap<>();
        for(String sessionId : pastSessionsIds){
            VotesHelper.getAgendas(FirebaseFirestore.getInstance(), associationId, sessionId).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            for(DocumentSnapshot dc : documentSnapshots){
                                if(dc.exists()){
                                    String id = dc.getId();
                                    Agenda agenda = dc.toObject(Agenda.class);
                                    agendasPair.first.add(dc);
                                    agendasPair.second.put(id, agenda);

                                    //puts ids
                                    agendaAndSessionIds.put(id, sessionId);
                                }
                            }
//                            for(Agenda agenda : agendasPair.second.values())
//                                FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").document("HgVkNiAVqA4JA3JXQbmO")
//                                    .collection("agendas").add(agenda);

                            fragment.updateAgendas(agendasPair, agendaAndSessionIds);
                            //TODO SHOULD UPDATE UI
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getMessage());
                            //TODO SHOULD FAIL
                        }
                    });
        }
    }
    public static void getPastQuestions(String associationId, String sessionId, String agendaId, OldFragment fragment){
//       map question id to agenda and session
        HashMap<String, Pair<String, String>> sessionAndAgendaIds = new HashMap<>();
        ArrayList<Pair<String, Question>> questions = new ArrayList<>();

        VotesHelper.getQuestions(FirebaseFirestore.getInstance(), associationId, sessionId, agendaId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for(DocumentSnapshot dc : documentSnapshots){
                            if(dc.exists()){
                                questions.add(new Pair<>(dc.getId(), dc.toObject(Question.class)));
                                sessionAndAgendaIds.put(dc.getId(), new Pair<>(sessionId, agendaId));
                            }
                        }
//                        FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").document("HgVkNiAVqA4JA3JXQbmO").collection("agendas").document("cvi6bxu01BjZ183KgFKI")
//                                .collection("questions").add(questions.get(0));
//                        FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").document("HgVkNiAVqA4JA3JXQbmO").collection("agendas").document("cvi6bxu01BjZ183KgFKI")
//                                .collection("questions").add(questions.get(1));

                        //TODO update ui
                        fragment.updateQuestionsUI(questions, sessionAndAgendaIds);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage());
                        //TODO SHOULD FAIL
                    }
                });
    }
}