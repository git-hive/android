package com.hive.hive.association.request;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.hive.hive.utils.Utils.getHashMapFilter;

public class RequestActivity extends AppCompatActivity {

    //--- Static
    private final String TAG = RequestActivity.class.getSimpleName();

    //--- Menu  Recycler View
    private RecyclerView mMenuRV;
    private TextView mFilterTV;

    //--- Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //--- Association
    // TODO: Change hardcoded associationID
    private String associationID = "gVw7dUkuw3SSZSYRXe8s";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // Find and set toolbar
        Toolbar toolbar = findViewById(R.id.requestTB);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "Action Bar not found");
        }

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(
                new Intent(RequestActivity.this, NewRequestActivity.class)
        ));

        // Getting menu Fragment Reference
        mMenuRV = findViewById(R.id.recyclerMenu);
        mFilterTV = findViewById(R.id.menuFilterTV);


        getAllRequestCategoriesAndCallGetAllRequests(db, associationID);

        //--- Recycle View Setup

        //Find views
        mFilterTV = findViewById(R.id.menuFilterTV);
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
    }

    private void getAllRequestCategoriesAndCallGetAllRequests(
            FirebaseFirestore db,
            String associationID
    ) {
        HashMap<DocumentReference, RequestCategory> categories = new HashMap<>();

        com.hive.hive.model.association.AssociationHelper.getAllRequestCategories(db, associationID)
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                        RequestCategory requestCategory = doc.toObject(RequestCategory.class);
                        categories.put(doc.getReference(), requestCategory);
                    }
                    getAllRequestAndCalljoinRequestCategories(db, associationID, categories);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    private void getAllRequestAndCalljoinRequestCategories(
            FirebaseFirestore db,
            String associationID,
            HashMap<DocumentReference,RequestCategory> categories
    ) {
        ArrayList<Request> requests = new ArrayList<>();
        com.hive.hive.model.association.AssociationHelper.getAllRequests(db, associationID)
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot doc : documentSnapshots) {
                        requests.add(doc.toObject(Request.class));
                    }
                    joinRequestsWithCategoriesAndCallSetupRecyclerView(categories, requests);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    private void joinRequestsWithCategoriesAndCallSetupRecyclerView(
            HashMap<DocumentReference, RequestCategory> categories,
            ArrayList<Request> requests
    ) {
        ArrayList<Pair<Request, ArrayList<RequestCategory>>> joinedRequestsAndCategories =
                new ArrayList<>();
        // For each request
        for (Request request : requests) {
            ArrayList<RequestCategory> requestCategories = new ArrayList<>();
            // For each of it's categories
            for (DocumentReference categoryRef : request.getCategories()) {
                // If the category exists inside the provided array of categories
                if (categories.containsKey(categoryRef)) {
                    // Add to it's array of categories
                    requestCategories.add(categories.get(categoryRef));
                }
            }
            // Create pair containing the request and an array of it's categories
            Pair<Request, ArrayList<RequestCategory>> newPair =
                    new Pair<>(request, requestCategories);
            joinedRequestsAndCategories.add(newPair);
        }

        setUpRecyclerView(joinedRequestsAndCategories);
    }

    private void setUpRecyclerView(
            ArrayList<Pair<Request, ArrayList<RequestCategory>>> joinedRequestsAndCategories
    ) {
        RequestAdapter mRecyclerAdapter =
                new RequestAdapter(joinedRequestsAndCategories, this);
        mRecyclerAdapter.notifyDataSetChanged();

        RecyclerView mRequestRV;
        mRequestRV = findViewById(R.id.requestRV);
        mRequestRV.setHasFixedSize(true);
        mRequestRV.setAdapter(mRecyclerAdapter);
    }
}
