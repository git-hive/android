package com.hive.hive.login;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.hive.hive.model.association.IngressRequest;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;

import java.util.ArrayList;


public class LoginAndSignupHelper {

    public static String ASSOCIATION_COLLECTION = "associations";

    public static String INGRESS_REQUEST_COLLECTION = "ingress_requests";


    public static Task<DocumentSnapshot> getAssociation(DocumentReference associationRef){
        return associationRef.get();
    }
    public static Task<QuerySnapshot> getAllAssociations() {
        return FirebaseFirestore.getInstance().collection(ASSOCIATION_COLLECTION).get();
    }

    public static Task<Void> saveUserAndSendRequestIngress(User newUser, ArrayList<String> selectedAssociationsIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //user data
        DocumentReference userRef = DocReferences.getUserRef();
        //ingressRequest data
        IngressRequest ingressRequest = new IngressRequest(DocReferences.getUserRef(), System.currentTimeMillis());
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
}
