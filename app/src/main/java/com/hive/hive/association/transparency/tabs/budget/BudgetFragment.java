package com.hive.hive.association.transparency.tabs.budget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.association.transparency.tabs.bills.BillsFragment;

import java.util.ArrayList;
import java.util.List;


public class BudgetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGE = "Orçamento";
    private List<Budget> budgets;




    public BudgetFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        BudgetFragment fragment = new BudgetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.budgets_RV);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        initializeData();
        BudgetAdapter mAdapter = new BudgetAdapter(budgets);
        rv.setAdapter(mAdapter);

        return  view;
    }

    // Dummy Content
    class Budget {
        String budgetName;
        int origin;


        Budget(String name, int origin) {
            this.budgetName = name;
            this.origin = origin;

        }
    }



    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(){
        budgets = new ArrayList<>();
        budgets.add(new BudgetFragment.Budget("Praça Food-Truck", 0));
        budgets.add(new BudgetFragment.Budget("Playground Holandês", 0));
        budgets.add(new BudgetFragment.Budget("Quadra Poliesportiva", 0));
    }


}
