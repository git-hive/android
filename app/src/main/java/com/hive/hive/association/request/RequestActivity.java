package com.hive.hive.association.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hive.hive.R;

public class RequestActivity extends AppCompatActivity {
    private RecyclerView mRequestRV;
    private RecyclerViewRequestAdapter mRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        //finding and setting toolbar
        Toolbar toolbar = findViewById(R.id.requestTB);
        setSupportActionBar(toolbar);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestActivity.this, NewRequestActivity.class));
            }
        });
        //finding and setting recyclerView
        DUMMYREQUESTS.createArray();
        mRequestRV = findViewById(R.id.requestRV);
        mRecyclerAdapter = new RecyclerViewRequestAdapter(DUMMYREQUESTS.requests);
        mRequestRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRequestRV.setLayoutManager(layoutManager);
        mRequestRV.setAdapter(mRecyclerAdapter);
    }
    @Override
    public void onResume(){
        super.onResume();
        mRecyclerAdapter.notifyDataSetChanged();
    }
}
