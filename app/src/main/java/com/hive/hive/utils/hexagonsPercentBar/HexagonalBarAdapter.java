package com.hive.hive.utils.hexagonsPercentBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.association.votes.QuestionGridAdapter;
import com.hive.hive.association.votes.tabs.current.CurrentAdapter;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.QuestionOptions;

import java.util.ArrayList;

/**
 * Created by birck on 30/03/18.
 */

public class HexagonalBarAdapter  extends RecyclerView.Adapter<HexagonalBarAdapter.PercentageHolder>{

        private ArrayList<QuestionOptions> mQuestions;
        private ArrayList<Integer> mColors = null;
        private Context mContext;

        // ListView stuff
        RecyclerView mPercentageTextView;

        public HexagonalBarAdapter(Context context, ArrayList<QuestionOptions> questions) {
            mQuestions = questions;
            mContext = context;
            initStuff();
        }
        @Override
        public PercentageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.percentages_text_view, parent, false);
            return new PercentageHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PercentageHolder holder, int position) {
            //final String percentage = String.valueOf(mQuestions.get(position).getScore());
            final String percentage = "25%";
            holder.percentageTV.setText(percentage);
            holder.percentageTV.setTextColor(mColors.get(position));
        }

        @Override
        public int getItemCount() {
        return mQuestions.size();
    }

        public void initStuff(){
            mColors = new ArrayList<>();
            mColors.add(0, Color.parseColor("#ff6347"));
            mColors.add(1, Color.parseColor("#82b3b3"));
            mColors.add(2, Color.parseColor("#ffbb3f"));
            mColors.add(3, Color.parseColor("#90ee90"));
        }


//    @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            QuestionOptions question = mQuestions.get(position);
//
//            PercentageHolder holder = null;
//
//
//            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.percentages_text_view, null);
//
//            holder = new PercentageHolder();
//
//            holder.percentageTV =  convertView.findViewById(R.id.my_percentageTV);
//            //holder.percentageTV.setText(mQuestions.get(position).getScore());
//            holder.percentageTV.setText("25%");
//
//            convertView.setTag(holder);
//
//            return convertView;
//
//        }

    static class PercentageHolder extends RecyclerView.ViewHolder {
        TextView percentageTV;

        public PercentageHolder(View itemView) {
            super(itemView);
            if(itemView != null) {
                percentageTV = itemView.findViewById(R.id.my_percentageTV);
            }
        }
    }


}
