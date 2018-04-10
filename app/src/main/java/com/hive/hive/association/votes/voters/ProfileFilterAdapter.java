package com.hive.hive.association.votes.voters;

/**
 * Created by birck on 05/04/18.
 */

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFilterAdapter extends RecyclerView.Adapter<ProfileFilterAdapter.MyViewHolder> {

    private List<String> userList;

    public String mCurrentSelected;

    public ArrayList<User> mUsers;

    private ProfileListAdapter mProfileListAdapter;

    public ProfileFilterAdapter(ArrayList<String> userList, ArrayList<User> users, ProfileListAdapter profileListAdapter) {
        this.userList = userList;
        mCurrentSelected = "";
        this.mUsers = users;
        this.mProfileListAdapter = profileListAdapter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_text_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String option = userList.get(position);
        holder.name.setText(option);
        holder.name.setTextColor(Color.BLACK);

        if(holder.name.getText() == mCurrentSelected ||  (holder.name.getText().toString().contains("Todos") && mCurrentSelected == ""))
            holder.name.setTextColor(Color.parseColor("#ffa500"));
        else
            holder.name.setTextColor(Color.BLACK);

        View.OnClickListener filterOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentSelected = (String) holder.name.getText();
                notifyDataSetChanged();
                //to filter the users
                if(position != 0) {
                    ArrayList<User> filteredUsers = new ArrayList<>();
                    for (User user : mUsers) {
                        if (user.getName().startsWith(mCurrentSelected.toUpperCase()))
                            filteredUsers.add(user);
                    }
                    mProfileListAdapter.setUserList(filteredUsers);
                    mProfileListAdapter.notifyDataSetChanged();
                }else {
                    mProfileListAdapter.setUserList(mUsers);
                    mProfileListAdapter.notifyDataSetChanged();
                }
            }
        };
        holder.name.setOnClickListener(filterOnclick);

        holder.frame.setOnClickListener(filterOnclick);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public LinearLayout frame;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.person_name);
            frame = view.findViewById(R.id.frame);
        }
    }


}
