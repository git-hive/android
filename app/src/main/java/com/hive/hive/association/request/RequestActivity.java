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
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.model.association.AssociationHelper;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;
import com.hive.hive.utils.GlideApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.hive.hive.utils.Utils.getHashMapFilter;

public class RequestActivity extends AppCompatActivity {
    private final String TAG = RequestActivity.class.getSimpleName();

    //--- Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    private Map<String, String> mmap = getHashMapFilter();

    // circularFilter Views
    private RecyclerView mMenuRV;
    private TextView mFilterTV;

    //--- Association
    // TODO: Change hardcoded associationID
    private String associationID = "gVw7dUkuw3SSZSYRXe8s";
    private ArrayList<Request> allRequests;
    private String mCategoryName = "services";

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

        getAllRequestCategoriesAndCallGetAllRequests();
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getAllRequestCategoriesAndCallGetAllRequests() {
        com.hive.hive.model.association.AssociationHelper.getAllRequestCategories(
                mDB,
                associationID
        )
                .addOnSuccessListener(documentSnapshots -> {
                    ArrayMap<DocumentReference, RequestCategory> categories = new ArrayMap<>();
                    for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                        RequestCategory requestCategory = doc.toObject(RequestCategory.class);
                        categories.put(doc.getReference(), requestCategory);
                    }
                    getAllRequestAndCallJoinRequestsCategories(categories);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getAllRequestAndCallJoinRequestsCategories(
            ArrayMap<DocumentReference, RequestCategory> categories
    ) {
        AssociationHelper.getAllRequests(mDB, associationID)
                .addOnSuccessListener(documentSnapshots -> {
                    ArrayList<Request> requests = new ArrayList<>();
                    for (DocumentSnapshot doc : documentSnapshots) {
                        requests.add(doc.toObject(Request.class));
                    }
                    joinRequestsWithCategoriesAndCallSetupRecyclerView(categories, requests);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void joinRequestsWithCategoriesAndCallSetupRecyclerView(
            ArrayMap<DocumentReference, RequestCategory> categories,
            ArrayList<Request> requests
    ) {
        // "category name" -> [RequestCategory]
        HashMap<String, ArrayList<Request>> categoriesRequests = new HashMap<>();
        categoriesRequests.put("all", new ArrayList<>());
        // For each request
        for (Request request : requests) {
            // For each of it's categories
            for (DocumentReference categoryRef : request.getCategoriesRefs()) {
                // Get category name
                String requestCategoryName = categories.get(categoryRef).getName();

                // If the ArrayList wasn't initialized, do so
                if (!categoriesRequests.containsKey(requestCategoryName)) {
                    categoriesRequests.put(requestCategoryName, new ArrayList<>());
                }

                categoriesRequests.get("all").add(request);
                categoriesRequests.get(requestCategoryName).add(request);
            }
        }

        setupRecyclerView(categoriesRequests);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupRecyclerView(HashMap<String, ArrayList<Request>> categoriesRequests) {
        ArrayList<Request> dataAdapter = categoriesRequests.get(mCategoryName);

        RequestAdapter mRecyclerAdapter = new RequestAdapter(
                dataAdapter,
                this
        );
        mRecyclerAdapter.notifyDataSetChanged();

        RecyclerView mRequestRV = findViewById(R.id.requestRV);
        mRequestRV.setHasFixedSize(true);
        mRequestRV.setAdapter(mRecyclerAdapter);

        mMenuRV = findViewById(R.id.recyclerMenu);
        mFilterTV = findViewById(R.id.menuFilterTV);

        mMenuRV.setOnScrollChangeListener((
                View v,
                int scrollX,
                int scrollY,
                int oldScrollX,
                int oldScrollY
        ) -> {
            TextView filterName = v.findViewById(R.id.menuItemCategorieTV);
            if (filterName != null) {
                String categoryName = mmap.get(filterName.getText()).toLowerCase();
                // If the category has actually changed
                if (!categoryName.equals(mCategoryName)) {
                    mCategoryName = categoryName;
                    mFilterTV.setText(mmap.get(filterName.getText()));
                    if (categoriesRequests.containsKey(categoryName)) {
                        mRecyclerAdapter.setRequests(categoriesRequests.get(categoryName));
                    } else {
                        mRecyclerAdapter.setRequests(new ArrayList<>());
                    }
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        GlideApp.with(getApplicationContext()).resumeRequestsRecursive();
    }
    @Override
    public void onStop(){
        super.onStop();
        GlideApp.with(getApplicationContext()).pauseRequestsRecursive();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRequestsLR.remove();
    }


}
