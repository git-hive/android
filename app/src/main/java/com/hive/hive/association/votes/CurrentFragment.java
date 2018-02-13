package com.hive.hive.association.votes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;

// In this case, the fragment displays simple text based on the page
public class CurrentFragment extends Fragment {
    private static final int NUM_LIST_ITEMS= 100;
    public static final String ARG_PAGE = "Passadas";

    private int mPage;
    // Content of (past, current and future)
    RecyclerView mCurrentVoteList;
    CurrentAdapter mCurrentAdapter;

    public static CurrentFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CurrentFragment fragment = new CurrentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current, container, false);

        //Set currentTab Layout and data
        mCurrentVoteList = (RecyclerView) view.findViewById(R.id.current_votes_list_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mCurrentVoteList.setLayoutManager(layoutManager);
        mCurrentVoteList.setHasFixedSize(true);

        mCurrentAdapter = new CurrentAdapter(NUM_LIST_ITEMS);
        mCurrentVoteList.setAdapter(mCurrentAdapter);

        return view;
    }
}