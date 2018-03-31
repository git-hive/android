package com.hive.hive.utils.hexagonsPercentBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.association.votes.QuestionGridAdapter;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.QuestionOptions;

import java.util.ArrayList;

/**
 * Created by birck on 30/03/18.
 */

public class HexagonalBarAdapter extends BaseAdapter{

        private final ArrayList<QuestionOptions> mQuestions;
        private final Context mContext;

        // ListView stuff
        ListView mPercentageTextView;

        public HexagonalBarAdapter(Context context, ArrayList<QuestionOptions> questions) {
            mQuestions = questions;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mQuestions.size();
        }

        @Override
        public Object getItem(int position) {
            return mQuestions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QuestionOptions question = mQuestions.get(position);

            PercentageHolder holder = null;


            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.percentages_text_view, null);

            holder = new PercentageHolder();

            holder.percentageTV =  convertView.findViewById(R.id.my_percentageTV);
            //holder.percentageTV.setText(mQuestions.get(position).getScore());
            holder.percentageTV.setText("25%");

            convertView.setTag(holder);

            return convertView;

        }

    static class PercentageHolder {
        TextView percentageTV;
    }


}
