package com.hive.hive.association.votes.tabs.SupportList;

/**
 * Created by birck on 05/04/18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFilterAdapter extends RecyclerView.Adapter<ProfileFilterAdapter.MyViewHolder> {

    private List<String> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.person_name);
            //photo = view.findViewById(R.id.person_photo);
        }
    }


    public ProfileFilterAdapter(ArrayList<String> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_text_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String option = userList.get(position);
        holder.name.setText(option);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
