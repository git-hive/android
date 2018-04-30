package com.hive.hive.association.votes.question_answering.firebase_handlers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.association.votes.question_answering.QuestionFormActivity;
import com.hive.hive.model.association.Question;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionAnsweringHandle {
    private static final String TAG = QuestionAnsweringHandle.class.getSimpleName();

    //Arraylist of Ids, HashMap<Id, Question>
    public static void getQuestions(String associationId, String sessionId, String agendaId, QuestionFormActivity activity) {
        Pair<ArrayList<String>, HashMap<String, Question>> questionsPair = new Pair<>(new ArrayList<>(), new HashMap<>());
        VotesHelper.getQuestions(FirebaseFirestore.getInstance(), associationId, sessionId, agendaId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                      for(DocumentSnapshot dc: documentSnapshots){
                          if(!dc.exists())
                              return;
                          else{
                              String id = dc.getId();
                              Question question= dc.toObject(Question.class);
                              questionsPair.first.add(id);
                              questionsPair.second.put(id, question);
                          }
                      }
                      activity.updateQuestions(questionsPair);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage());
                        activity.finishWithError();
                     }
                });
    }
}
