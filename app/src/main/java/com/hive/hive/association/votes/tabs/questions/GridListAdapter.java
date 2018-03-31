package com.hive.hive.association.votes.tabs.questions;
 
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.hive.hive.R;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.QuestionOptions;
import com.hive.hive.model.association.Vote;
import com.hive.hive.utils.DocReferences;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Birck on 15/03/18.
 */

public class GridListAdapter extends BaseAdapter {

    private Context mContext;

    private HashMap<String, Question> mQuestions;
    private ArrayList<String> mQuestionsIds;
    private ArrayList<QuestionOptions> mOptions;

    private TextView mContentTV;

    private LayoutInflater inflater;
    private boolean isListView;
    private int selectedPosition = -1;
    private Integer currentFormIndex = 0;
    private TimeLineAdapter refStorylineAdapter;
 
    public GridListAdapter(Context context, HashMap<String, Question> questions, ArrayList<String> questionsIds, TextView contentTV,boolean isListView) {
        mContext = context;
        mQuestions = questions;
        mQuestionsIds = questionsIds;
        mOptions = mQuestions.get(mQuestionsIds.get(currentFormIndex)).getOptions();
        this.isListView = isListView;
        mContentTV = contentTV;
        inflater = LayoutInflater.from(mContext);
        mContentTV.setText(mQuestions.get(mQuestionsIds.get(currentFormIndex)).getQuestion());
    }

 
    @Override
    public int getCount() {
        return mOptions.size();
    }
 
    @Override
    public Object getItem(int i) {
        return mOptions.get(i);
    }
 
    @Override
    public long getItemId(int i) {
        return i;
    }
 
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        selectedPosition = refStorylineAdapter.mStatusListValue.get(currentFormIndex);
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
 
        viewHolder.label.setText(((QuestionOptions) getItem(i)).getContent());
 
        //check the radio button if both position and selectedPosition matches
        viewHolder.radioButton.setChecked(i == selectedPosition);
 
        //Set the position tag to both radio button and label
        viewHolder.radioButton.setTag(i);
        viewHolder.label.setTag(i);
 
        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
                refStorylineAdapter.mStatusListValue.set(currentFormIndex, selectedPosition);
                System.out.println(i + " " + selectedPosition);
            }
        });
 
        viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
                //refStorylineAdapter.mStatusListValue.set(i, selectedPosition);
            }
        });

        return view;
    }
 
    //On selecting any view set the current position to selected Positon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }
 
    private class ViewHolder {
        private TextView label;
        private RadioButton radioButton;
    }
 
    //Return the selectedPosition item
    public Vote getSelectedVote() {
        if (selectedPosition != -1) {
            //TODO FILL REFERENCES
            QuestionOptions selectedOption = mQuestions.get(mQuestionsIds.get(currentFormIndex)).getOptions().get(selectedPosition);
            Vote vote = new Vote(0, 0, DocReferences.getUserRef(), null, null,
                    selectedOption.getTitle(), 0, null, null, null);
            Toast.makeText(mContext, "Selected Item : " + selectedOption.getTitle(), Toast.LENGTH_SHORT).show();
            return vote;
        }
        return null;
    }

    //Delete the selected position from the arrayList
    public void previousQuestion() {

        selectedPosition = refStorylineAdapter.mStatusListValue.get(currentFormIndex);
        notifyDataSetChanged();

        if(currentFormIndex == 0){
            //not sure what to do
        }// Nothing has been selected
        else if(selectedPosition == -1){
            // Change Storyline marker
            refStorylineAdapter.activePoint(currentFormIndex-1);
            refStorylineAdapter.inactivePoint(currentFormIndex);

            refStorylineAdapter.notifyDataSetChanged();

            mOptions = mQuestions.get(mQuestionsIds.get(currentFormIndex-1)).getOptions();
            currentFormIndex -=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1


            mContentTV.setText(mQuestions.get(mQuestionsIds.get(currentFormIndex)).getQuestion());

            notifyDataSetChanged();

            //Toast.makeText(context, "You should choose an option", Toast.LENGTH_SHORT).show();
        }else if (currentFormIndex != 0) {
            // Change Storyline marker
            refStorylineAdapter.activePoint(currentFormIndex-1);
            refStorylineAdapter.completePoint(currentFormIndex);

            refStorylineAdapter.notifyDataSetChanged();

            mOptions = mQuestions.get(mQuestionsIds.get(currentFormIndex-1)).getOptions();
            currentFormIndex -=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1

            notifyDataSetChanged();
        }
    }
    //Delete the selected position from the arrayList
    public void nextQuestion() {

        selectedPosition = refStorylineAdapter.mStatusListValue.get(currentFormIndex);
        notifyDataSetChanged();

        // Check if all is selected and send answers
        if(currentFormIndex == mQuestions.size()-1){

            // Everything is answered and send options
            if(refStorylineAdapter.checkAnswers()){
                Toast.makeText(mContext, "All done, questions sent!", Toast.LENGTH_LONG).show();
                ((Activity)mContext).finish();

            // When there is still questions to be answered
            }else{
                Toast.makeText(mContext, "You should answer all questions!", Toast.LENGTH_LONG).show();

            }
        }else if(selectedPosition == -1){

            refStorylineAdapter.activePoint(currentFormIndex+1);
            refStorylineAdapter.inactivePoint(currentFormIndex);
            refStorylineAdapter.notifyDataSetChanged();

            mOptions = mQuestions.get(mQuestionsIds.get(currentFormIndex+1)).getOptions();

            currentFormIndex +=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1

            mContentTV.setText(mQuestions.get(mQuestionsIds.get(currentFormIndex)).getQuestion());

            notifyDataSetChanged();
            //Toast.makeText(context, "You should choose an option", Toast.LENGTH_SHORT).show();

            // Option Selected
        }else if (currentFormIndex != mQuestions.size()-1) {
            // Change Storyline marker
            refStorylineAdapter.activePoint(currentFormIndex+1);
            refStorylineAdapter.completePoint(currentFormIndex);

            refStorylineAdapter.notifyDataSetChanged();

            mOptions = mQuestions.get(mQuestionsIds.get(currentFormIndex+1)).getOptions();

            currentFormIndex +=1;
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
        }
    }

    public void setStorylineAdapter(TimeLineAdapter storylineAdapter){
        refStorylineAdapter = storylineAdapter;
    }
}