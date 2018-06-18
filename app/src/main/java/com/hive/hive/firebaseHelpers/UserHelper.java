package com.hive.hive.firebaseHelpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.hive.hive.model.association.IngressRequest;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;

import java.util.ArrayList;

public class UserHelper extends FirebaseHelpers {

    public static Task<DocumentSnapshot> getUserData(){
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION).document(FirebaseAuth.getInstance().getUid()).get();
    }

    //saves user signupData and its associations ingress requests
    public static Task<Void> saveUserAndSendRequestIngress(User newUser, ArrayList<String> selectedAssociationsIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //user data
        DocumentReference userRef = DocReferences.getUserRef();
        //ingressRequest data
        IngressRequest ingressRequest = new IngressRequest(DocReferences.getUserRef(), System.currentTimeMillis(),
                newUser.getCpf(), newUser.getName(), newUser.getEmail(), newUser.getPhotoUrl());
        String ingressRequestId = FirebaseAuth.getInstance().getUid();

        //Write Batch
        WriteBatch batch = db.batch();
        if(userRef != null)
            batch.set(userRef, newUser);
        else return null; // got some problem
        if(ingressRequestId != null)
            for(String selectedAssociationId : selectedAssociationsIds) {
                DocumentReference ingressRequestRef = db.collection(ASSOCIATION_COLLECTION).document(selectedAssociationId)
                        .collection(INGRESS_REQUEST_COLLECTION).document(ingressRequestId);
                batch.set(ingressRequestRef, ingressRequest);
            }
        else return null;  //got some problem


        return batch.commit();
    }

    //save the last association
    public static void saveUserLastAssociation(User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).document(FirebaseAuth.getInstance().getUid()).set(user);
    }
}
