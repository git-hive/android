package com.hive.hive.association.votes.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.association.votes.tabs.current.CurrentAdapter;
import com.hive.hive.model.association.Vote;

import java.util.ArrayList;

// In this case, the fragment displays simple text based on the page
public class FutureFragment extends Fragment {
    private static final int NUM_LIST_ITEMS= 6;

    public static final String ARG_PAGE = "Passadas";

    // Content of (past, current and future)
    RecyclerView mCurrentVotesListRV;
    CurrentAdapter mCurrentAdapter;

    ArrayList<Vote> DUMMYARRAY;


    private int mPage;

    public static FutureFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FutureFragment fragment = new FutureFragment();
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

        DUMMYARRAY = new ArrayList<Vote>();
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());

        // Adding Action Stuff
        DUMMYARRAY.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
            }
        });



//        //Set currentTab Layout and data
//        mCurrentVotesListRV = (RecyclerView) view.findViewById(R.id.current_votes_list_RV);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        mCurrentVotesListRV.setLayoutManager(layoutManager);
//        //mCurrentVotesListRV.setHasFixedSize(true);
//
//        mCurrentAdapter = new CurrentAdapter(DUMMYARRAY, NUM_LIST_ITEMS);
//        //Adding Click Listener To Card Buttons
//        mCurrentAdapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mCurrentVotesListRV.setAdapter(mCurrentAdapter);
//
//

        return view;
    }
}