package com.hive.hive.association.votes.tabs.questions;
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.hive.hive.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Birck on 15/03/18.
 */
 
public class GridListAdapter extends BaseAdapter {
    private Context context;
    private HashMap<Integer, ArrayList<String> > mFormQuestions;
    private ArrayList<String> arrayList;
    private LayoutInflater inflater;
    private boolean isListView;
    private int selectedPosition = -1;
    private Integer currentFormIndex = 0;
    private TimeLineAdapter refStorylineAdapter;
 
    public GridListAdapter(Context context, HashMap<Integer, ArrayList<String> > formQuestions, boolean isListView) {
        this.context = context;
        mFormQuestions = formQuestions;
        this.arrayList = mFormQuestions.get(currentFormIndex);
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
    }
 
    @Override
    public int getCount() {
        return arrayList.size();
    }
 
    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }
 
    @Override
    public long getItemId(int i) {
        return i;
    }
 
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
 
            //inflate the layout on basis of boolean
            if (isListView)
                view = inflater.inflate(R.layout.list_custom_row_layout, viewGroup, false);

            viewHolder.label = (TextView) view.findViewById(R.id.label);
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_button);
 
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
 
        viewHolder.label.setText(arrayList.get(i));
 
        //check the radio button if both position and selectedPosition matches
        viewHolder.radioButton.setChecked(i == selectedPosition);
 
        //Set the position tag to both radio button and label
        viewHolder.radioButton.setTag(i);
        viewHolder.label.setTag(i);
 
        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });
 
        viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
 
 
        });
        return view;
    }
 
    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }
 
    private class ViewHolder {
        private TextView label;
        private RadioButton radioButton;
    }
 
    //Return the selectedPosition item
    public String getSelectedItem() {
        if (selectedPosition != -1) {
            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return arrayList.get(selectedPosition);
        }
        return "";
    }

    //Delete the selected position from the arrayList
    public void previousQuestion() {

        // Nothing has been selected
        if(selectedPosition == -1){
            // Change Storyline marker
            refStorylineAdapter.activePoint(currentFormIndex-1);
            refStorylineAdapter.inactivePoint(currentFormIndex);

            refStorylineAdapter.notifyDataSetChanged();

            arrayList = mFormQuestions.get(currentFormIndex-1);
            currentFormIndex -=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1

            notifyDataSetChanged();

            //Toast.makeText(context, "You should choose an option", Toast.LENGTH_SHORT).show();
        }else if (currentFormIndex != 0) {
            // Change Storyline marker
            refStorylineAdapter.activePoint(currentFormIndex-1);
            refStorylineAdapter.completePoint(currentFormIndex);

            refStorylineAdapter.notifyDataSetChanged();

            arrayList = mFormQuestions.get(currentFormIndex-1);
            currentFormIndex -=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1

            notifyDataSetChanged();
        }
    }
    //Delete the selected position from the arrayList
    public void nextQuestion() {
        // Nothing has been selected
        if(currentFormIndex == mFormQuestions.size()-1){
            if(refStorylineAdapter.checkAnswers()){

            }
        }if(selectedPosition == -1){

            refStorylineAdapter.activePoint(currentFormIndex+1);
            refStorylineAdapter.inactivePoint(currentFormIndex);
            refStorylineAdapter.notifyDataSetChanged();

            arrayList = mFormQuestions.get(currentFormIndex+1);
            currentFormIndex +=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
            //Toast.makeText(context, "You should choose an option", Toast.LENGTH_SHORT).show();

            // Option Selected
        }else if (currentFormIndex != mFormQuestions.size()-1) {
            // Change Storyline marker
            refStorylineAdapter.activePoint(currentFormIndex+1);
            refStorylineAdapter.completePoint(currentFormIndex);

            refStorylineAdapter.notifyDataSetChanged();

            arrayList = mFormQuestions.get(currentFormIndex+1);
            currentFormIndex +=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
        }
    }

    public void setStorylineAdapter(TimeLineAdapter storylineAdapter){
        refStorylineAdapter = storylineAdapter;
    }
}