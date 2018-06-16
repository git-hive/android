package com.hive.hive.association.votes.future_and_past.future;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.firebaseHelpers.VotesHelper;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class FutureAgendasFirebaseHandle{
    private final static String  TAG = FutureAgendasFirebaseHandle.class.getSimpleName();

    public static void getFutureSessions(String associationId, FutureFragment fragment){
        ArrayList<String> sessionsIds = new ArrayList<>();
        final Session[] session = new Session[1];
        VotesHelper.getFutureSessions(FirebaseFirestore.getInstance(), associationId).get()
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
                        getFutureAgendas(associationId, sessionsIds, fragment);
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

    public static void getFutureAgendas(String associationId, ArrayList<String> pastSessionsIds, FutureFragment fragment){
        Pair<ArrayList<String>, HashMap<String, Agenda>> agendasPair= new Pair<>(new ArrayList<>(), new HashMap<>());
        for(String sessionId : pastSessionsIds){
            VotesHelper.getAgendas(FirebaseFirestore.getInstance(), associationId, sessionId).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            for(DocumentSnapshot dc : documentSnapshots){
                                if(dc.exists()){
                                    String id = dc.getId();
                                    Agenda agenda = dc.toObject(Agenda.class);
                                    agendasPair.first.add(id);
                                    agendasPair.second.put(id, agenda);
                                }
                            }
//                            for(Agenda agenda : agendasPair.second.values())
//                                FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").document("HgVkNiAVqA4JA3JXQbmO")
//                                    .collection("agendas").add(agenda);

                            getRequestScore(agendasPair, fragment);
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
    public static void getFutureQuestions(DocumentReference sessionRef, String agendaId, FutureFragment fragment){
//       map question id to agenda and session
        ArrayList<Pair<String, Question>> questions = new ArrayList<>();

        sessionRef.collection("agendas").document(agendaId).collection("questions").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for(DocumentSnapshot dc : documentSnapshots){
                            if(dc.exists()){
                                questions.add(new Pair<>(dc.getId(), dc.toObject(Question.class)));
                            }
                        }
//                        FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").document("HgVkNiAVqA4JA3JXQbmO").collection("agendas").document("cvi6bxu01BjZ183KgFKI")
//                                .collection("questions").add(questions.get(0));
//                        FirebaseFirestore.getInstance().collection("associations").document(associationId).collection("sessions").document("HgVkNiAVqA4JA3JXQbmO").collection("agendas").document("cvi6bxu01BjZ183KgFKI")
//                                .collection("questions").add(questions.get(1));

                        //TODO update ui
                        fragment.updateQuestionsUI(questions);
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

    //@param are a list of agendas ids and a mapping of ids into agendas
    public static void getRequestScore(Pair<ArrayList<String>, HashMap<String, Agenda>> agendasPair, FutureFragment fragment){
        HashMap<String, String> scoreMap = new HashMap<>(); //maps a request score into an agendaId
        for(String agendaId : agendasPair.first){
            Agenda agenda = agendasPair.second.get(agendaId);
            if(agenda.getRequestRef() != null) {
                agenda.getRequestRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            scoreMap.put(agendaId, documentSnapshot.getLong("score").toString());
                            fragment.updateAgendas();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage());
                        //TODO should do something else?!
                    }
                });
            }
        }
        fragment.setAgendas(agendasPair, scoreMap);
    }
}
