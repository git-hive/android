package com.hive.hive.feed;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.association.transparency.tabs.budget.BudgetFragment;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    private void initializeData(){
        budgets = new ArrayList<>();
        budgets.add(new BudgetFragment.Budget("Praça Food-Truck", 0));
        budgets.add(new BudgetFragment.Budget("Playground Holandês", 0));
        budgets.add(new BudgetFragment.Budget("Quadra Poliesportiva", 0));
    }

}
