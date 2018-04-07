package com.hive.hive.association.votes;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.QuestionOptions;
import static com.hive.hive.utils.Utils.getCharForNumber;


import java.util.ArrayList;

/**
 * Created by birck on 17/02/18.
 */


public class QuestionGridAdapter extends BaseAdapter {

    private ArrayList<Integer> mColors = null;
    private Context mContext;
    private ArrayList<QuestionOptions> mOptions;


    public QuestionGridAdapter(Context mContext, ArrayList<QuestionOptions> mOptions) {
        this.mContext = mContext;
        this.mOptions = mOptions;
        initStuff();
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
        String optionIdentifier = (getCharForNumber(position+1)+") ").toLowerCase();

        // Set text to different colors
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString redSpannable= new SpannableString(optionIdentifier);
        redSpannable.setSpan(new ForegroundColorSpan(mColors.get(position)), 0, optionIdentifier.length(), 0);
        builder.append(redSpannable);

        String optionText = mOptions.get(position).getTitle();
        SpannableString whiteSpannable= new SpannableString(optionText);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, optionText.length(), 0);
        builder.append(whiteSpannable);
        /////////////////////////////////////////



        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_question_option, parent, false);
            holder = new ViewHolder();

            holder.optionTV =  convertView.findViewById(R.id.optionTV);
            holder.optionTV.setText(builder, TextView.BufferType.SPANNABLE);
            holder.optionTV.setTextColor(mColors.get(position));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.text.setText(mMobileValues[position]);
        return convertView;
    }

    public void initStuff(){
        mColors = new ArrayList<>();
        mColors.add(0, Color.parseColor("#ff6347"));
        mColors.add(1, Color.parseColor("#82b3b3"));
        mColors.add(2, Color.parseColor("#ffbb3f"));
        mColors.add(3, Color.parseColor("#90ee90"));
    }

    static class ViewHolder {
        TextView optionTV;
    }
}