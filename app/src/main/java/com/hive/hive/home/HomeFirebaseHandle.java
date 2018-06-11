package com.hive.hive.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.model.association.Report;
import com.hive.hive.model.association.Session;

import java.util.ArrayList;

public class HomeFirebaseHandle {
    private final static String TAG = HomeFirebaseHandle.class.getSimpleName();
    public static void getSession(HomeFragment homeFragment, ArrayList<Object> objects){
        VotesHelper.getCurrentSession(FirebaseFirestore.getInstance(), HomeFragment.mCurrentAssociationId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots){
                            if (documentSnapshot.exists())
                                objects.add(0, documentSnapshot.toObject(Session.class));
                        }
                        getReports(homeFragment, objects);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                getReports(homeFragment, objects);
            }
        });
    }
    public static void getReports(HomeFragment homeFragment, ArrayList<Object> objects){
        AssociationHelper.getActiveReports().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for(DocumentSnapshot documentSnapshot : documentSnapshots){
                    if(documentSnapshot.exists()){
                        objects.add(documentSnapshot.toObject(Report.class));
                        Log.d(TAG, documentSnapshot.toObject(Report.class).getDescription());
                    }
                }
                homeFragment.initRecycler(objects);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(homeFragment.getContext(), homeFragment.getContext()
                        .getResources().getString(R.string.no_reports), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
