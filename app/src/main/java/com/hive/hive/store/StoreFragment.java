package com.hive.hive.store;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.forum.ForumPost;

import java.util.ArrayList;

public class StoreFragment extends Fragment {

    private RecyclerView mRecyclerViewHome;
    private RecyclerViewStoreAdapter mRecyclerViewHomeAdapter;

    ArrayList<Object> DUMMYARRAY;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance() {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        DUMMYARRAY = new ArrayList<>();
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new ForumPost());
        DUMMYARRAY.add(new Request());

        mRecyclerViewHome = v.findViewById(R.id.recyclerViewFeed);
        mRecyclerViewHomeAdapter = new RecyclerViewStoreAdapter(DUMMYARRAY);
        mRecyclerViewHome.setAdapter(mRecyclerViewHomeAdapter);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }
}
