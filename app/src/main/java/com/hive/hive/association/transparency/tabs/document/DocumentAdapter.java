package com.hive.hive.association.transparency.tabs.document;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birck on 16/02/18.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.BillViewHolder>{

    private ArrayList<DocumentFragment.Document> mDocuments;
    private Activity mActivity;

    DocumentAdapter(ArrayList<DocumentFragment.Document> bills, Activity activity){
        mDocuments = bills;
        mActivity = activity;
    }
    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        BillViewHolder bvh = new BillViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.budgetName.setText(mDocuments.get(position).billName);
        final int aux = position;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtils.downloadFile(
                        mActivity, mActivity,
                        mDocuments.get(aux).billName,
                        mDocuments.get(aux).extension
                );
            }
        });
        holder.item = mDocuments.get(position);
    }

    @Override
    public int getItemCount() {
        return mDocuments.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cv;
        TextView budgetName;
        DocumentFragment.Document item;


        BillViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.document_CV);
            budgetName = (TextView)itemView.findViewById(R.id.document_name_TV);
            imageView = (ImageView) itemView.findViewById(R.id.ic_download_IV);
        }
    }

}