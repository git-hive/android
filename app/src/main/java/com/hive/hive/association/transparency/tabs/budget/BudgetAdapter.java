package com.hive.hive.association.transparency.tabs.budget;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;

import java.util.List;

/**
 * Created by birck on 17/02/18.
 */

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>{

    List<BudgetFragment.Budget> mBudgets;

    BudgetAdapter(List<BudgetFragment.Budget> budgets){
        mBudgets = budgets;
    }
    @Override
    public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        BudgetViewHolder bvh = new BudgetViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BudgetViewHolder holder, int position) {
        holder.budgetName.setText(mBudgets.get(position).budgetName);
    }

    @Override
    public int getItemCount() {
        return mBudgets.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView budgetName;

        BudgetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.budgetCV);
            budgetName = (TextView)itemView.findViewById(R.id.budgetNameTV);
        }
    }

}
