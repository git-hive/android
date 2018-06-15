package com.hive.hive.association.transparency;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.association.transparency.tabs.document.DocumentFragment;
import com.hive.hive.firebaseHelpers.FilesHelper;
import com.hive.hive.model.association.AssociationFile;

import java.util.ArrayList;
import java.util.HashMap;

public class TransparencyFirebaseHandler {
    private static final String TAG = TransparencyFirebaseHandler.class.getSimpleName();
    public static void getAllAssociationStorageFiles(DocumentFragment documentFragment){
        FilesHelper.getFiles().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                Pair<ArrayList<String>, HashMap<String, AssociationFile>> associationFiles = new Pair<>(new ArrayList<>(), new HashMap<>());
                for(DocumentSnapshot doc : documentSnapshots){
                    if(doc.exists()){
                        associationFiles.first.add(doc.getId());
                        associationFiles.second.put(doc.getId(), doc.toObject(AssociationFile.class));
                        documentFragment.initRecycler(associationFiles);
                    }
                }
                //call setup recyclerview
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }
}
