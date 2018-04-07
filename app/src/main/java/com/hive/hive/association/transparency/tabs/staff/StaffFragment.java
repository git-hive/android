package com.hive.hive.association.transparency.tabs.staff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.association.transparency.TransparencyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StaffFragment extends Fragment {

    private static final String TAG = StaffFragment.class.getSimpleName();
    public static final String ARG_PAGE = "Funcionários";



    //-- Context
    private TransparencyActivity mActivity;

    public StaffFragment() {
        // Required empty public constructor
    }

    public static StaffFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StaffFragment fragment = new StaffFragment();
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
        View view =  inflater.inflate(R.layout.fragment_staff, container, false);

        mActivity = (TransparencyActivity) getActivity();

        return  view;
    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {

        // Adding child data
        List<String> gardening = new ArrayList<String>();
        gardening.add("The Shawshank Redemption");
        gardening.add("The Godfather");
        gardening.add("The Godfather: Part II");
        gardening.add("Pulp Fiction");
        gardening.add("The Good, the Bad and the Ugly");
        gardening.add("The Dark Knight");
        gardening.add("12 Angry Men");

        List<String> cleaning = new ArrayList<String>();
        cleaning.add("The Conjuring");
        cleaning.add("Despicable Me 2");
        cleaning.add("Turbo");
        cleaning.add("Grown Ups 2");
        cleaning.add("Red 2");
        cleaning.add("The Wolverine");

        List<String> security = new ArrayList<String>();
        security.add("2 Guns");
        security.add("The Smurfs 2");
        security.add("The Spectacular Now");
        security.add("The Canyons");
        security.add("Europa Report");

        List<String> door = new ArrayList<String>();
        door.add("2 Guns");
        door.add("The Smurfs 2");
        door.add("The Spectacular Now");
        door.add("The Canyons");
        door.add("Europa Report");

        List<String> services = new ArrayList<String>();
        services.add("2 Guns");
        services.add("The Smurfs 2");
        services.add("The Spectacular Now");
        services.add("The Canyons");
        services.add("Europa Report");

        List<String> manager = new ArrayList<String>();
        manager.add("2 Guns");
        manager.add("The Smurfs 2");
        manager.add("The Spectacular Now");
        manager.add("The Canyons");
        manager.add("Europa Report");
    }

}
