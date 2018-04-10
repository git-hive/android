package com.hive.hive.association.votes.voters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.CustomViews.Hexagon;
import com.hive.hive.utils.ProfilePhotoHelper;

import java.util.ArrayList;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.MyViewHolder> {

    private final String TAG = ProfileListAdapter.class.getSimpleName();
    private List<User> userList;
    private ArrayList<Integer> mColors;
    private Context mContext;
 
 
    public ProfileListAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.mContext = context;
        mColors = new ArrayList<>();
        mColors.add(0, Color.parseColor("#ff6347"));
        mColors.add(1, Color.parseColor("#82b3b3"));
        mColors.add(2, Color.parseColor("#ffbb3f"));
        mColors.add(3, Color.parseColor("#90ee90"));

    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_support_item, parent, false);

        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.hexagon.setHexagonColor(getColorByName((String) holder.name.getText()));
        holder.hexagon.setFirstLetter(user.getName().charAt(0));
        ProfilePhotoHelper.loadImage(mContext, holder.photo, user.getPhotoUrl());
    }
 
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public int getColorByName(String name){
        int color;
        int firstLetter = name.charAt(0);
        color = mColors.get((firstLetter - 'A' + 1) % mColors.size());
        return color;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView photo;
        public Hexagon hexagon;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.person_name);
            photo = view.findViewById(R.id.person_photo);
            hexagon = (Hexagon) view.findViewById(R.id.coloredHexagon);
        }
    }
}