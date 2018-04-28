package com.hive.hive.utils.hexagonsPercentBar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.QuestionOptions;
import com.hive.hive.utils.Utils;

import java.util.ArrayList;

/**
 * Created by birck on 30/03/18.
 */

public class HexagonalBarAdapter  extends RecyclerView.Adapter<HexagonalBarAdapter.PercentageHolder>{

        private ArrayList<QuestionOptions> mQuestions;
        private ArrayList<Integer> mColors = null;
        ArrayList<Float> mPercentages;
        ArrayList<String> mPercentagesString;
        Context mContext;
        float mTotal;



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
            // Update percentages;
            updatePercentages();
            setConfig();


            final String percentage = mPercentagesString.get(position)+"%";
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

            mPercentages = new ArrayList<>();
            mPercentagesString = new ArrayList<>();
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


    public void updatePercentages(){
        mPercentages = new ArrayList<>();
        for (int i=0;i<mQuestions.size();i++) {
            mPercentages.add(i, (float) mQuestions.get(i).getScore());
//            System.out.println(" LOLOLOLOLOLOLOLOLOLOLOLOL "+ mQuestions.get(i).getScore());
        }
//        System.out.println(" size "+ mPercentages.size());

    }

    public void setConfig(){

        // Such an uggly code
        float totalScores = 0;
        for (Float score:
                mPercentages) {
//            System.out.println(" HAAYAYAHAHYAAHAYAYA"+score);
            totalScores +=  score;
        }

//        System.out.println(" Total scores "+ totalScores);
        for(int i = 0;i<mPercentages.size();i++){
            if(totalScores == 0) {
//                System.out.println(" BEYYYYYYYYYYYYYYYY 1 "+ mPercentages.get(i)+" " + mPercentages.get(i) / totalScores);
                mPercentagesString.add(i, "25.0 %");
                // break;
            }else {
//                System.out.println(" BEYYYYYYYYYYYYYYYY  2 "+ mPercentages.get(i)+" " + mPercentages.get(i) / totalScores);
                mPercentagesString.add(i, String.valueOf(Utils.round(mPercentages.get(i) / totalScores * 100, 2)));
                //  break;
            }
            //mPercentage.add(i, (float) 25.0);
        }

        //notifyDataSetChanged();
    }

}
