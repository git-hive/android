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
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.model.association.AssociationHelper;
import com.hive.hive.model.association.Request;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {

    //--- Static
    private final String TAG = RequestActivity.class.getSimpleName();

    //--- Firestore
    FirebaseFirestore mDB = FirebaseFirestore.getInstance();

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

        getAllRequestAndCallSetupRecyclerView(mDB, associationID);
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

    private void getAllRequestAndCallSetupRecyclerView(
            FirebaseFirestore db,
            String associationID
    ) {
        AssociationHelper.getAllRequests(db, associationID)
            .addOnSuccessListener(documentSnapshots -> {
                ArrayList<Request> requests = new ArrayList<>();
                for (DocumentSnapshot doc : documentSnapshots) {
                    requests.add(doc.toObject(Request.class));
                }
                setupRecyclerView(requests);
            })
            .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    private void setupRecyclerView(ArrayList<Request> requests) {
        RequestAdapter mRecyclerAdapter = new RequestAdapter(requests, this);
        mRecyclerAdapter.notifyDataSetChanged();

        RecyclerView mMenuRV = findViewById(R.id.recyclerMenu);
        TextView mFilterTV = findViewById(R.id.menuFilterTV);

        RecyclerView mRequestRV = findViewById(R.id.requestRV);
        mRequestRV.setHasFixedSize(true);
        mRequestRV.setAdapter(mRecyclerAdapter);
    }
}
