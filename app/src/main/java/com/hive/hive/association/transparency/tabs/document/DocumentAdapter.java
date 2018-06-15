package com.hive.hive.association.transparency.tabs.document;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.model.association.AssociationFile;
import com.hive.hive.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by birck on 16/02/18.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.BillViewHolder>{

    private Pair<ArrayList<String>, HashMap<String, AssociationFile>> mDocuments;
    private Activity mActivity;

    public DocumentAdapter(Pair<ArrayList<String>, HashMap<String, AssociationFile>> mDocuments, Activity mActivity) {
        this.mDocuments = mDocuments;
        this.mActivity = mActivity;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        BillViewHolder bvh = new BillViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        String fileId = mDocuments.first.get(position);
        holder.budgetName.setText(mDocuments.second.get(fileId).getName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.downloading), Toast.LENGTH_SHORT).show();
                FileUtils.downloadFile(
                        mActivity, mActivity,
                        fileId,
                        ".pdf",
                        holder.downloadPB
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDocuments.first.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cv;
        TextView budgetName;
        ProgressBar downloadPB;


        BillViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.document_CV);
            budgetName = (TextView)itemView.findViewById(R.id.document_name_TV);
            imageView = (ImageView) itemView.findViewById(R.id.ic_download_IV);
            downloadPB = itemView.findViewById(R.id.downloadPB);
        }
    }

}