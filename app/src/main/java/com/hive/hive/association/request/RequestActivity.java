package com.hive.hive.association.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.model.association.Request;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestActivity extends AppCompatActivity {

    //--- Static
    private final String TAG = RequestActivity.class.getSimpleName();

    //--- Views
    private RecyclerView mRequestRV;
    private RequestAdapter mRecyclerAdapter;

    //--- Data
    private ArrayList<String> mIds;
    private HashMap<String, Request> mRequests;

    //--- Listeners
    private EventListener<QuerySnapshot> mRequestsEL;
    private ListenerRegistration mRequestsLR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        //finding and setting toolbar

        Toolbar toolbar = findViewById(R.id.requestTB);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            Log.d(TAG, "Home as Up setted");
        }
        else
            Log.e(TAG, "Home as Up not setted. Action Bar not found.");

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestActivity.this, NewRequestActivity.class));
            }
        });


        //GETTING ALL REQUESTS
        mRequests = new HashMap<>();
        mIds = new ArrayList<>();
        mRequestsEL = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d(TAG, dc.getDocument().get("title").toString());
                            //TODO documentSnapshot.toObject is not working
                            //TODO change nulls to real objects

                            Request request = new Request(dc.getDocument().get("id").toString(),
                                    dc.getDocument().getLong("createdAt"),
                                    dc.getDocument().getLong("updatedAt"),
                                    dc.getDocument().get("title").toString(),
                                    dc.getDocument().get("content").toString(),
                                    Integer.parseInt(dc.getDocument().get("score").toString()));
//                                        documentSnapshot.get("categories"),
//                                        documentSnapshot.get("comments"),
//                                        documentSnapshot.get("supports"));
                            mRequests.put(dc.getDocument().getId() ,request);
                            mIds.add(dc.getDocument().getId());
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mRequests.get(modifiedId).setUpdatedAt(dc.getDocument().getLong("updatedAt"));
                            mRequests.get(modifiedId).setTitle(dc.getDocument().get("title").toString());
                            // mRequests.get(dc.getDocument().getId()).setComments(dc.getDocument().get("comments").toString());
                           // mRequests.get(dc.getDocument().getId()).setSupports(dc.getDocument().get("supports").toString());
                            mRequests.get(modifiedId).setScore(Integer.parseInt(dc.getDocument().get("supportScore").toString()));
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();
                            mRequests.remove(removedId);
                            mIds.remove(removedId);
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                    }
                }

            }
        };

        //TODO change associationID
        mRequestsLR = AssociationHelper.getAllRequests(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s").addSnapshotListener(mRequestsEL);

        //--- Recycle View Setup

        //Find views
        mRequestRV = findViewById(R.id.requestRV);

        //Set Size
        mRequestRV.setHasFixedSize(true);

        //Set Adapter
        mRecyclerAdapter = new RequestAdapter(mRequests, mIds);
        mRequestRV.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRequestsLR.remove();
    }
}
