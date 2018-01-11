package com.hive.hive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.adapters.RecyclerViewHomeAdapter;
import com.hive.hive.model.AssociationPost;
import com.hive.hive.model.Request;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewHome;
    private RecyclerViewHomeAdapter mRecyclerViewHomeAdapter;

    ArrayList<Object> DUMMYARRAY;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
