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
    private ArrayList<QuestionOptions> mOptions;


    public QuestionGridAdapter(Context mContext, ArrayList<QuestionOptions> mOptions) {
        this.mContext = mContext;
        this.mOptions = mOptions;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public QuestionOptions getItem(int position) {
        return mOptions.get(position);
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
            holder.optionTV.setText(mOptions.get(position).getTitle());

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