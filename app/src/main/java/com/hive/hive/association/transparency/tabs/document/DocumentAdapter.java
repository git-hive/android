package com.hive.hive.association.transparency.tabs.document;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;

import java.util.List;

/**
 * Created by birck on 16/02/18.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.BillViewHolder>{

    List<DocumentFragment.Bill> mBills;

    DocumentAdapter(List<DocumentFragment.Bill> bills){
        mBills = bills;
    }
    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        BillViewHolder bvh = new BillViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.budgetName.setText(mBills.get(position).billName);
    }

    @Override
    public int getItemCount() {
        return mBills.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView budgetName;

        BillViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.billCV);
            budgetName = (TextView)itemView.findViewById(R.id.billNameTV);
        }
    }

}