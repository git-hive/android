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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
import static com.hive.hive.utils.Utils.getHashMapFilter;

public class RequestActivity extends AppCompatActivity {
    private final String TAG = RequestActivity.class.getSimpleName();

    //--- Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    ListenerRegistration mRequestsListener;

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
    private String associationID = "gVw7dUkuw3SSZSYRXe8s";

    private CollectionReference mAssociationRequestsRef;

    private Hashtable<Integer, DocumentSnapshot> requestDocs;

    // Default category
    private String mCategoryName = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        this.requestDocs = new Hashtable<>();

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
                requestDocs.clear();
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
        if (mRequestsListener != null)
            mRequestsListener.remove();
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
                int index = 0;
                for (DocumentSnapshot dc : documentSnapshots) {
                    if (dc.exists()) {
                        requestDocs.put(index, dc);
                        index++;
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
     * @param requestSnap  Category snapshot to be matched against the category
     * @param categoryName Category name to match the request against
     * @return a boolean indicating weather or not the request belongs to the category
     */
    private boolean requestSnapBelongsToCategory(
            DocumentSnapshot requestSnap,
            String categoryName
    ) {
        if (categoryName.equals("all")) return true;

        String requestCategoryName = requestSnap.getString("categoryName");
        return requestCategoryName != null && requestCategoryName.equals(categoryName);
    }

    /**
     * Filter 'requestSnaps' requests by their category using the 'categoryName'
     *
     * @param requestSnaps request snapshots to be filtered
     * @param categoryName category name to be used as filter
     * @return request snapshots that passes the filter
     */
    private ArrayList<DocumentSnapshot> filterRequestDocsByCategory(
            ArrayList<DocumentSnapshot> requestSnaps,
            String categoryName
    ) {
        ArrayList<DocumentSnapshot> filteredRequests = new ArrayList<>();
        for (DocumentSnapshot snap : requestSnaps) {
            if (requestSnapBelongsToCategory(snap, categoryName)) filteredRequests.add(snap);
        }
        return filteredRequests;
    }

    /**
     * Sort requestsSnaps by rank (greater rank first)
     *
     * @param requestDocs
     */
    private ArrayList<DocumentSnapshot> sortRequestSnapsByRank(
            ArrayList<DocumentSnapshot> requestDocs
    ) {
        ArrayList<DocumentSnapshot> sortedRequests = new ArrayList<>(requestDocs);
        Collections.sort(sortedRequests, new Comparator<DocumentSnapshot>() {
            @Override
            public int compare(DocumentSnapshot snap1, DocumentSnapshot snap2) {
                Double request1Rank = snap1.getDouble("rank");
                Double request2Rank = snap2.getDouble("rank");
                if (request1Rank == null && request2Rank == null) return 0;

                if (request1Rank == null) return 1;
                if (request2Rank == null) return -1;

                if (request1Rank < request2Rank) return 1;
                if (request1Rank > request2Rank) return -1;

                return 0;
            }
        });

        return sortedRequests;
    }

    private int scrollCount = 0;

    private void setupRecyclerView() {
        ArrayList<DocumentSnapshot> requests = new ArrayList<>(requestDocs.values());

        mRecyclerAdapter = new RequestAdapter(
                sortRequestSnapsByRank(filterRequestDocsByCategory(requests, mCategoryName)),
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
                                        filterRequestDocsByCategory(requests, mCategoryName)
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
