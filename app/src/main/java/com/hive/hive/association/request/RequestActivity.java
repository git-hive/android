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
import com.google.firebase.firestore.FirebaseFirestore;
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
        AssociationHelper.getAllRequestCategories(
                mDB,
                associationID
        )
                .addOnSuccessListener(documentSnapshots -> {

                    if (documentSnapshots.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Falha ao pegar categorias",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    ArrayList<DocumentSnapshot> categoryDocs =
                            (ArrayList<DocumentSnapshot>) documentSnapshots.getDocuments();
                    getAllRequestAndCallJoinRequestsCategories(categoryDocs);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getAllRequestAndCallJoinRequestsCategories(
            ArrayList<DocumentSnapshot>  categoryDocs
    ) {
        AssociationHelper.getAllRequests(mDB, associationID)
                .addOnSuccessListener(documentSnapshots -> {

                    if (documentSnapshots.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Falha ao pegar requisições",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    ArrayList<DocumentSnapshot> requestDocs =
                            (ArrayList<DocumentSnapshot>) documentSnapshots.getDocuments();
                    joinRequestsWithCategoriesAndCallSetupRecyclerView(categoryDocs, requestDocs);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.toString()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void joinRequestsWithCategoriesAndCallSetupRecyclerView(
            ArrayList<DocumentSnapshot> categoryDocs,
            ArrayList<DocumentSnapshot> requestDocs
    ) {

        HashMap<DocumentReference, DocumentSnapshot> categoryRefSnap = new HashMap<>();
        for (DocumentSnapshot categoryDoc : categoryDocs) {
            categoryRefSnap.put(categoryDoc.getReference(), categoryDoc);
        }

        // "category name" -> [DocumentSnapshot]
        HashMap<String, ArrayList<DocumentSnapshot>> categoriesRequests = new HashMap<>();
        categoriesRequests.put("all", new ArrayList<>());

        // For each request
        for (DocumentSnapshot requestSnap : requestDocs) {
            Request request = requestSnap.toObject(Request.class);
            if (request.getCategoriesRefs() == null) break;
            // For each of it's categories
            for (DocumentReference categoryRef : request.getCategoriesRefs()) {
                if (!categoryRefSnap.containsKey(categoryRef)) break;
                // Get category name
                String requestCategoryName = categoryRefSnap
                        .get(categoryRef)
                        .getString("name");

                // If the ArrayList wasn't initialized, do so
                if (!categoriesRequests.containsKey(requestCategoryName)) {
                    categoriesRequests.put(requestCategoryName, new ArrayList<>());
                }

                categoriesRequests.get("all").add(requestSnap);
                categoriesRequests.get(requestCategoryName).add(requestSnap);
            }
        }

        setupRecyclerView(categoriesRequests);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupRecyclerView(
            HashMap<String, ArrayList<DocumentSnapshot>> categoriesRequests
    ) {
        ArrayList<DocumentSnapshot> dataAdapter = categoriesRequests.get(mCategoryName);

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
                int oladScrollY
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
}
