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
import android.view.View;
import android.widget.TextView;

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
import java.util.Map;

import static com.hive.hive.utils.Utils.getHashMapFilter;

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


    //--- Menu  Recycler View
    private RecyclerView mMenuRV;
    private TextView mFilterTV;

    // Hard coded menu items


    Map<String, String> mmap = getHashMapFilter();



    @RequiresApi(api = Build.VERSION_CODES.M)
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

        // Getting menu Fragment Reference
        mMenuRV = findViewById(R.id.recyclerMenu);
        mFilterTV = findViewById(R.id.menuFilterTV);


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
                            Request request = dc.getDocument().toObject(Request.class);
                            mRequests.put(dc.getDocument().getId() ,request);
                            mIds.add(dc.getDocument().getId());
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mRequests.get(modifiedId).setUpdatedAt(dc.getDocument().getLong("updatedAt"));
                            mRequests.remove(modifiedId);
                            mRequests.put(modifiedId, dc.getDocument().toObject(Request.class));
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
        mFilterTV = findViewById(R.id.menuFilterTV);


        //Set Size
        mRequestRV.setHasFixedSize(true);

        //Set Adapter
        mRecyclerAdapter = new RequestAdapter(mRequests, mIds, this);
        mRequestRV.setAdapter(mRecyclerAdapter);

        mMenuRV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(mMenuRV.getLayoutManager()!= null) {
                    View nodeInRV = mMenuRV.getLayoutManager().getChildAt(2);
                    if(nodeInRV != null) {
                        TextView filterName = nodeInRV.findViewById(R.id.menuItemCategorieTV);
                        if (filterName != null) {
                            if(mFilterTV != null) {
                                mFilterTV.setText(mmap.get(filterName.getText()));
                                Log.d(TAG, mmap.get(filterName.getText()) + "**********************************************************"+ filterName.getText());

                            } else
                                Log.d(TAG, filterName.getText() + "**********************************************************");


                            //Toast.makeText(v.getContext(), "clicked:" + filterName, Toast.LENGTH_SHORT).show();
                        }else
                            Log.d(TAG, filterName.getText() + "**********************************************************");


                    }
                }


            }

        });
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
