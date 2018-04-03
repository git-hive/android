package com.hive.hive.association.transparency.tabs.document;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.association.transparency.TransparencyActivity;
import com.hive.hive.association.transparency.tabs.budget.BudgetFragment;

import java.util.ArrayList;
import java.util.List;

public class DocumentFragment extends Fragment {

    private static final String TAG = DocumentFragment.class.getSimpleName();
    public static final String ARG_PAGE = "Documentos";

    //--- Data
    private List<Bill> bills;

    //--- Context
    private TransparencyActivity mActivity;

    public DocumentFragment() {
        // Required empty public constructor
    }

    public static DocumentFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DocumentFragment fragment = new DocumentFragment();
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

        View view = inflater.inflate(R.layout.fragment_document, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.bills_RV);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        initializeData();
        DocumentAdapter mAdapter = new DocumentAdapter(bills);
        rv.setAdapter(mAdapter);

        mActivity = (TransparencyActivity) getActivity();

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
