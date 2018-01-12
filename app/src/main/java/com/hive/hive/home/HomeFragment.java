package com.hive.hive.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.model.AssociationPost;
import com.hive.hive.model.Request;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerViewHome;
    private RecyclerViewHomeAdapter mRecyclerViewHomeAdapter;

    ArrayList<Object> DUMMYARRAY;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        DUMMYARRAY = new ArrayList<>();
        DUMMYARRAY.add(new AssociationPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new AssociationPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new AssociationPost());
        DUMMYARRAY.add(new Request());
        DUMMYARRAY.add(new AssociationPost());
        DUMMYARRAY.add(new Request());

        mRecyclerViewHome = v.findViewById(R.id.recyclerViewFeed);
        mRecyclerViewHomeAdapter = new RecyclerViewHomeAdapter(DUMMYARRAY);
        mRecyclerViewHome.setAdapter(mRecyclerViewHomeAdapter);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }


}
