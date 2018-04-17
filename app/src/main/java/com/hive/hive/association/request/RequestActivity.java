package com.hive.hive.association.request;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.model.association.AssociationHelper;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;

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

    // DocumentReference -> RequestCategory
    private HashMap<DocumentReference, RequestCategory> refCategory;

    // [Request]
    private ArrayList<Request> allRequests;

    // Request -> requestID
    private HashMap<Request, String> requestIds;

    // "category name" -> [Request]
    private HashMap<String, ArrayList<Request>> categoryRequests;

    // Default category
    private String mCategoryName = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        this.refCategory = new HashMap<>();
        this.allRequests = new ArrayList<>();
        this.requestIds = new HashMap<>();
        this.categoryRequests = new HashMap<>();

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestActivity.this.startActivity(
                        new Intent(RequestActivity.this, NewRequestActivity.class)
                );
            }
        });

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


    private void getAllRequestCategoriesAndCallGetAllRequests() {
        AssociationHelper.getAllRequestCategories(
                mDB,
                associationID
        )
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.isEmpty()) {
                            Toast.makeText(
                                    RequestActivity.this,
                                    "Falha ao pegar categorias",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }


                        for (DocumentSnapshot snap : documentSnapshots) {
                            RequestCategory requestCategory = snap.toObject(RequestCategory.class);
                            refCategory.put(snap.getReference(), requestCategory);
                        }

                        getAllRequestAndCallJoinRequestsCategories();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void getAllRequestAndCallJoinRequestsCategories() {
        AssociationHelper.getAllRequests(mDB, associationID)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.isEmpty()) {
                            Toast.makeText(
                                    RequestActivity.this,
                                    "Falha ao pegar requisições",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }


                        for (DocumentSnapshot snap : documentSnapshots) {
                            Request request = snap.toObject(Request.class);
                            if (request.getAuthorRef() == null) continue;
                            allRequests.add(request);
                            requestIds.put(request, snap.getId());
                        }

                        joinRequestsWithCategoriesAndCallSetupRecyclerView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void joinRequestsWithCategoriesAndCallSetupRecyclerView() {
        categoryRequests.put("all", new ArrayList<>());

        // For each request
        for (Request request : allRequests) {
            // Add it to the "all" category
            categoryRequests.get("all").add(request);
            // If it doesn't have any category
            if (request.getCategoriesRefs() == null) continue;
            // For each of it's categories
            for (DocumentReference categoryRef : request.getCategoriesRefs()) {
                // If the category doesn't exist
                if (!refCategory.containsKey(categoryRef)) continue;

                // Get category name
                String requestCategoryName = refCategory
                        .get(categoryRef)
                        .getName();

                // If the ArrayList wasn't initialized, do so
                if (!categoryRequests.containsKey(requestCategoryName)) {
                    categoryRequests.put(requestCategoryName, new ArrayList<>());
                }

                categoryRequests.get(requestCategoryName).add(request);
            }
        }

        setupRecyclerView();
    }

    private ArrayList<String> getRequestIDs(ArrayList<Request> requests) {
        ArrayList<String> requestIDs = new ArrayList<>();
        for (Request request : requests) {
            requestIDs.add(this.requestIds.get(request));
        }
        return requestIDs;
    }

    private void setupRecyclerView() {
        ArrayList<Request> requests = categoryRequests.get(mCategoryName);
        ArrayList<String> categoryRequestIDs = getRequestIDs(requests);

        RequestAdapter mRecyclerAdapter = new RequestAdapter(
                requests,
                categoryRequestIDs,
                this
        );
        mRecyclerAdapter.notifyDataSetChanged();

        RecyclerView mRequestRV = findViewById(R.id.requestRV);
        mRequestRV.setAdapter(mRecyclerAdapter);

        mMenuRV = findViewById(R.id.recyclerMenu);
//        mMenuRV.setHasFixedSize(true);
//        mMenuRV.setItemViewCacheSize();

        mFilterTV = findViewById(R.id.menuFilterTV);

        mMenuRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                TextView filterName = recyclerView.findViewById(R.id.menuItemCategorieTV);
                super.onScrollStateChanged(recyclerView, newState);
                if (filterName != null) {
                    String categoryName = mmap.get(filterName.getText()).toLowerCase();
                    // If the category has actually changed
                    if (!categoryName.equals(mCategoryName)) {
                        mCategoryName = categoryName;
                        mFilterTV.setText(mmap.get(filterName.getText()));
                        if (categoryRequests.containsKey(categoryName)) {
                            ArrayList<Request> requests = categoryRequests.get(mCategoryName);
                            ArrayList<String> categoryRequestIDs = getRequestIDs(requests);
                            mRecyclerAdapter.setData(requests, categoryRequestIDs);
                        } else {
                            mRecyclerAdapter.setData(new ArrayList<>(), new ArrayList<>());
                        }
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        
    }
}
