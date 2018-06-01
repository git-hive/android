package com.hive.hive.association.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.association.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    // recycler view
    private SwipeRefreshLayout mRequestRefresh;
    private RecyclerView mRequestRV;
    private RequestAdapter mRecyclerAdapter;
    //--- Association
    // TODO: Change hardcoded associationID
    private String associationID = HomeFragment.mCurrentAssociationId;

    private CollectionReference mAssociationRequestsRef;

    private Pair<ArrayList<String>, HashMap<String, Request>> requests;

    // Default category
    private String mCategoryName = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        this.requests = new Pair<>(new ArrayList<>(), new HashMap<>());

        // Find and set toolbar
        Toolbar toolbar = findViewById(R.id.requestTB);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "Action Bar not found");
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestActivity.this.startActivity(
                        new Intent(RequestActivity.this, NewRequestActivity.class)
                );
            }
        });

        mRequestRefresh = findViewById(R.id.requestSR);
        mRequestRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requests.first.clear();
                requests.second.clear();
                addRequestSnapListenerAndCallSetupRecyclerView();
            }
        });
        mRequestRefresh.setColorSchemeColors(getResources().getColor(R.color.colorOrange));
        mRequestRefresh.setRefreshing(true);


        mAssociationRequestsRef = mDB
                .collection("associations")
                .document(associationID)
                .collection("requests");

        addRequestSnapListenerAndCallSetupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecyclerAdapter.sendToFirebase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.sendToFirebase();
    }

    /**
     * Attaches a listener to 'associationRequestsRef',
     * handling it's changes and call setupRecyclerView
     *
     */
    private void addRequestSnapListenerAndCallSetupRecyclerView() {
        mAssociationRequestsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (DocumentSnapshot dc : documentSnapshots) {
                    if (dc.exists()) {
                        requests.first.add(dc.getId());
                        requests.second.put(dc.getId(), dc.toObject(Request.class));

                    }
                }
                setupRecyclerView();
                if(mRequestRefresh.isRefreshing()){
                    Log.d(TAG, "refreshing");
                    mRequestRefresh.setRefreshing(false);
                }
            }
        });
    }

    /**
     * Check if the request snap belongs to the provided category name
     *
     * @param request  Category request to be matched against the category
     * @param categoryName Category name to match the request against
     * @return a boolean indicating weather or not the request belongs to the category
     */
    private boolean requestSnapBelongsToCategory(
            Request request,
            String categoryName
    ) {
        if (categoryName.equals("all")) return true;

        String requestCategoryName = request.getCategoryName();
        return requestCategoryName != null && requestCategoryName.equals(categoryName);
    }

    /**
     * Filter 'requestSnaps' requests by their category using the 'categoryName'
     *
     * @param categoryName category name to be used as filter
     * @return filtered requests
     */
    private Pair<ArrayList<String>, HashMap<String, Request>> filterRequestDocsByCategory(
            String categoryName
    ) {
        Pair<ArrayList<String>, HashMap<String, Request>> filteredRequests= new Pair<>(new ArrayList<>(), new HashMap<>());

        for (String reqId : requests.first) {
            if (requestSnapBelongsToCategory(requests.second.get(reqId), categoryName)){
                filteredRequests.first.add(reqId);
                filteredRequests.second.put(reqId, requests.second.get(reqId));
            }
        }
        return filteredRequests;
    }

    /**
     * Sort requests by rank (greater rank first)
     *
     */
    private Pair<ArrayList<String>, HashMap<String, Request>> sortRequestSnapsByRank(
            Pair<ArrayList<String>, HashMap<String, Request>> filteredRequests
    ) {
        ArrayList<String> sortedRequests = filteredRequests.first;
        Pair<ArrayList<String>, HashMap<String, Request>> finalFilteredRequests = filteredRequests;
        Collections.sort(sortedRequests, new Comparator<String>() {
            @Override
            public int compare(String request1, String request2) {
                Integer request1Rank = finalFilteredRequests.second.get(request1).getRank();
                Integer request2Rank = finalFilteredRequests.second.get(request2).getRank();

                if (request1Rank < request2Rank) return 1;
                if (request1Rank > request2Rank) return -1;

                return 0;
            }
        });
        filteredRequests = new Pair<>(sortedRequests, filteredRequests.second);

        return filteredRequests;
    }

    private int scrollCount = 0;

    private void setupRecyclerView() {
        mRecyclerAdapter = new RequestAdapter(
                sortRequestSnapsByRank(filterRequestDocsByCategory(mCategoryName)),
                this
        );
        mRecyclerAdapter.notifyDataSetChanged();

        mRequestRV = findViewById(R.id.requestRV);
        mRequestRV.setAdapter(mRecyclerAdapter);

        mMenuRV = findViewById(R.id.recyclerMenu);
        mFilterTV = findViewById(R.id.menuFilterTV);

        mMenuRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                TextView filterName = recyclerView
                        .getLayoutManager()
                        .getChildAt(1)
                        .findViewById(R.id.menuItemCategorieTV);

                super.onScrollStateChanged(recyclerView, newState);
                String categoryName = mmap.get(filterName.getText()).toLowerCase();
                if (filterName != null) {
                    // If the category has actually changed
                    if (!categoryName.equals(mCategoryName)) {
                        mCategoryName = categoryName;
                        mFilterTV.setText(mmap.get(filterName.getText()));

                        mRecyclerAdapter.setData(
                                sortRequestSnapsByRank(
                                        filterRequestDocsByCategory(mCategoryName)
                                )
                        );
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        disableProgressBarAndShowRecycler();
    }

    private void disableProgressBarAndShowRecycler() {
        mFilterTV.setVisibility(View.VISIBLE);
        mRequestRV.setVisibility(View.VISIBLE);
        mRequestRefresh.setRefreshing(false);
    }


}
