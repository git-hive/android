package com.hive.hive.association.transparency.tabs.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;

/**
 * Created by birck on 17/02/18.
 */


public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] mMobileValues;

    public GridAdapter(Context context, String[] mobileValues) {
        mContext = context;
        mMobileValues = mobileValues;
    }

    @Override
    public int getCount() {
        return mMobileValues.length;
    }

    @Override
    public String getItem(int position) {
        return mMobileValues[position];
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_staff, parent, false);
            holder = new ViewHolder();

            holder.text = (ImageView) convertView.findViewById(R.id.lblListItem);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.text.setText(mMobileValues[position]);
        return convertView;
    }

    static class ViewHolder {
        ImageView text;
    }
}