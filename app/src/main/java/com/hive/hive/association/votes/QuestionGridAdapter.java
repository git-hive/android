package com.hive.hive.association.votes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.QuestionOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by birck on 17/02/18.
 */


public class QuestionGridAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] mMobileValues;
    private HashMap<String, QuestionOptions> mOptions;
    private ArrayList<String> mOptionsIds;

    public QuestionGridAdapter(Context mContext, String[] mMobileValues, HashMap<String, QuestionOptions> mOptions, ArrayList<String> mOptionsIds) {
        this.mContext = mContext;
        this.mMobileValues = mMobileValues;
        this.mOptions = mOptions;
        this.mOptionsIds = mOptionsIds;
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
            convertView = inflater.inflate(R.layout.item_question_option, parent, false);
            holder = new ViewHolder();

            holder.optionTV =  convertView.findViewById(R.id.optionTV);
            String optionId = mOptionsIds.get(position);
            holder.optionTV.setText(mOptions.get(optionId).getTitle());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.text.setText(mMobileValues[position]);
        return convertView;
    }

    static class ViewHolder {
        TextView optionTV;
    }
}