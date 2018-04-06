package com.hive.hive.association.transparency.tabs.document;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.association.transparency.TransparencyActivity;
import com.hive.hive.association.transparency.tabs.budget.BudgetFragment;
import com.hive.hive.main.MainActivity;
import com.hive.hive.utils.FileUtils;

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

    @Override
    public void onResume() {
        super.onResume();
        isStoragePermissionGranted(getActivity());

    }

    private boolean isStoragePermissionGranted(Activity activity){
        if(Build.VERSION.SDK_INT >= 23){
            //Sdk>=23 needs to ask permission at runtime
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Storage Permission is granted");
                return true;
            } else {
                Log.d(TAG,"Storage Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FileUtils.STORAGE_REQUEST_CODE);
                return false;
            }
        }
        else{
            Log.d(TAG,"Storage Permission is granted");
            //Permission is automatically granted in sdk<23 on installation (manifest)
            return true;
        }

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
