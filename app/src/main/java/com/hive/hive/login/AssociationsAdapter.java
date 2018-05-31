package com.hive.hive.login;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.Association;

import java.util.ArrayList;
import java.util.HashMap;

public class AssociationsAdapter extends RecyclerView.Adapter<AssociationsAdapter.AssociationViewHolder>{


    private Pair<ArrayList<String>, HashMap<String, Association>> mAssociations;
    private ArrayList<String> mSelectedAssociationsIds;

    public AssociationsAdapter(Pair<ArrayList<String>, HashMap<String, Association>> mAssociations) {
        this.mAssociations = mAssociations;
        this.mSelectedAssociationsIds = new ArrayList<>();
    }

    public ArrayList<String> getmSelectedAssociationsIds() {
        return mSelectedAssociationsIds;
    }

    @Override
    public AssociationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_associations, parent, false);
        return new AssociationViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(AssociationViewHolder holder, int position) {
        Association association = mAssociations.second.get(mAssociations.first.get(position));
        holder.mAssociationName.setText(association.getName());
        holder.mAssociationType.setText(association.getType());
        holder.mAssociationCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(holder, position);
            }
        });
        holder.mAssociationCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mAssociationCB.isChecked()){
                    mSelectedAssociationsIds.add(mAssociations.first.get(position));
                }else{
                    mSelectedAssociationsIds.remove(mAssociations.first.get(position));
                }
            }
        });
    }
    private void check(AssociationViewHolder holder, int position){
        if(!holder.mAssociationCB.isChecked()){
            holder.mAssociationCB.setChecked(true);
            mSelectedAssociationsIds.add(mAssociations.first.get(position));
        }else{
            holder.mAssociationCB.setChecked(false);
            mSelectedAssociationsIds.remove(mAssociations.first.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mAssociations.first.size();
    }

    public class AssociationViewHolder extends RecyclerView.ViewHolder{
        CardView mAssociationCV;
        ImageView mAssocitationImage;
        TextView mAssociationName, mAssociationType;
        CheckBox mAssociationCB;
        public AssociationViewHolder(View itemView) {
            super(itemView);
            mAssociationCV = itemView.findViewById(R.id.associationCV);
            mAssocitationImage = itemView.findViewById(R.id.associationIV);
            mAssociationName = itemView.findViewById(R.id.associationNameTV);
            mAssociationType = itemView.findViewById(R.id.associationTypeTV);
            mAssociationCB = itemView.findViewById(R.id.associationCB);
        }
    }
}
