package com.hive.hive.association.votes.tabs.questions;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.association.transparency.tabs.staff.CustomGridView;
import com.hive.hive.association.votes.QuestionGridAdapter;
import com.hive.hive.model.association.Question;
import com.hive.hive.utils.hexagonsPercentBar.HexagonView;
import com.hive.hive.utils.hexagonsPercentBar.HexagonalBarAdapter;

public class ExpandableListAdapter extends BaseExpandableListAdapter{

	private Context mContext;
	private HashMap<String, Question> mQuestions;
	private ArrayList<String> mQuestionsIds;

	// Percentage bar
	HexagonView mPercentageBar;
    RecyclerView mPercentageRV;
    HexagonalBarAdapter mHexBarAdapter;



	public ExpandableListAdapter(Context context, HashMap<String, Question> questions, ArrayList<String> mQuestionsIds) {
		mContext = context;
		this.mQuestions = questions;
		this.mQuestionsIds = mQuestionsIds;
	}

	@Override
	public int getGroupCount() {
		// Get header size
		return this.mQuestions.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
        return 1;
		//this.child.get(this.header.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// Get header position
        String questionId = mQuestionsIds.get(groupPosition);

        return this.mQuestions.get(questionId);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// This will return the child
        String questionId = mQuestionsIds.get(groupPosition);

		return mQuestions.get(questionId).getOptions().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		// Getting header title
		Question question = (Question) getGroup(groupPosition);

		// Inflating header layout and setting text
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.question_header, parent, false);
		}

		TextView header_text = (TextView) convertView.findViewById(R.id.headerTV);
		header_text.setText(question.getQuestion());

        ImageView headerMarkerIV = convertView.findViewById(R.id.headerMarkerIV);

		// If group is expanded then change the text into bold and change the
		if (isExpanded) {
		    headerMarkerIV.setImageResource(R.drawable.ic_baixolaranja);
		    headerMarkerIV.setRotation(-180);
//			header_text.setTypeface(null, Typeface.BOLD);
//			header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0);
		} else {
			// If group is not expanded then change the text back into normal
			// and change the icon
            headerMarkerIV.setImageResource(R.drawable.ic_baixolaranja);
            headerMarkerIV.setRotation(-360);
//			header_text.setTypeface(null, Typeface.NORMAL);
//			header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0);
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.question_grid_view, null);
		}

		CustomGridView gridView = (CustomGridView) convertView
				.findViewById(R.id.questionGV);

		gridView.setNumColumns(2);// gridView.setGravity(Gravity.CENTER);//
		gridView.setHorizontalSpacing(10);// SimpleAdapter adapter =

        String questionId = mQuestionsIds.get(groupPosition);
        Question question = mQuestions.get(questionId);
		QuestionGridAdapter adapter = new QuestionGridAdapter(mContext, question.getOptions());
		gridView.setAdapter(adapter);// Adapter

		int totalHeight = 0;
		for (int size = 0; size < adapter.getCount(); size++) {
			RelativeLayout relativeLayout = (RelativeLayout) adapter.getView(
					size, null, gridView);
			TextView questionTV = (TextView) relativeLayout.getChildAt(0);
			questionTV.measure(0, 0);
			totalHeight += questionTV.getMeasuredHeight();
		}
		gridView.SetHeight(totalHeight);

		// TODO: Take care you should call autoInit always
		mPercentageBar =  convertView.findViewById(R.id.percentageBar);
		mPercentageBar.autoInit(4);

        // Setting percentage view stuff
		mPercentageRV = convertView.findViewById(R.id.percentageRV);
		mHexBarAdapter = new HexagonalBarAdapter(((Activity)mContext), question.getOptions());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setAutoMeasureEnabled(true);
        mPercentageRV.setLayoutManager(layoutManager);
        mPercentageRV.setAdapter(mHexBarAdapter);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}