package com.hive.hive.association.transparency.tabs.staff;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;

import java.util.ArrayList;

/**
 * Created by Nícolas Oreques de Araujo on 4/6/18.
 */

class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private final StaffFragment.OnListFragmentInteractionListener mListener;
    private ArrayList<StaffFragment.Staff> mStaff;

    StaffAdapter(ArrayList<StaffFragment.Staff> staff, StaffFragment.OnListFragmentInteractionListener listener) {
        mStaff = staff;
        mListener = listener;
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StaffViewHolder holder, int position) {
        holder.mItem = mStaff.get(position);
        holder.mName.setText(holder.mItem.name);
        holder.mImageIcon.setImageResource(holder.mItem.imageId);


    }

    @Override
    public int getItemCount() {
        return mStaff.size();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageIcon;
        final TextView mName;
        StaffFragment.Staff mItem;

        StaffViewHolder(View view) {
            super(view);
            mView = view;
            mImageIcon = view.findViewById(R.id.staff_avatar_IV);
            mName = view.findViewById(R.id.staff_name_TV);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }

    private class ContextCompat {
    }
}