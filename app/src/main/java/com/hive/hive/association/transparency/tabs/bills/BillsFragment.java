package com.hive.hive.association.transparency.tabs.bills;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;

import java.util.ArrayList;
import java.util.List;

public class BillsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGE = "Boletos";
    private List<Bill> bills;

    public BillsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BillsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        BillsFragment fragment = new BillsFragment();
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

        View view = inflater.inflate(R.layout.fragment_bills, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.bills_RV);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        initializeData();
        BillsAdapter mAdapter = new BillsAdapter(bills);
        rv.setAdapter(mAdapter);

        return  view;
    }


    // Dummy Content
    class Bill {
        String billName;
        int origin;


        Bill(String name, int origin) {
            this.billName = name;
            this.origin = origin;

        }
    }



    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(){
        bills = new ArrayList<>();
        bills.add(new Bill("hive_staff.pdf", 0));
        bills.add(new Bill("pizzas.pdf", 0));
        bills.add(new Bill("apple_phones.pdf", 0));
    }



}
