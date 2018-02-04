package com.hive.hive.association;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.hive.hive.R;
import com.hive.hive.association.request.RequestActivity;
import com.hive.hive.association.votes.VotesActivity;



public class AssociationFragment extends Fragment {

    ImageButton requestBT;
    ImageButton votesBT;
    public AssociationFragment() {
        // Required empty public constructor
    }

    public static AssociationFragment newInstance() {
        AssociationFragment fragment = new AssociationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_association, container, false);

        requestBT = v.findViewById(R.id.requestsBT);
        votesBT = v.findViewById(R.id.voteBT);

        requestBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), RequestActivity.class));
            }
        });

        votesBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), VotesActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
