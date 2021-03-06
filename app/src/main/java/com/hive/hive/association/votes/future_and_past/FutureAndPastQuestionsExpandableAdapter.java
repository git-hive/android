package com.hive.hive.association.votes.future_and_past;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.association.transparency.tabs.staff.CustomGridView;
import com.hive.hive.association.votes.questions.adapters.QuestionGridAdapter;
import com.hive.hive.association.votes.voters_list.VotersListActivity;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.QuestionOptions;
import com.hive.hive.utils.hexagonsPercentBar.HexagonView;
import com.hive.hive.utils.hexagonsPercentBar.HexagonalBarAdapter;
import java.util.ArrayList;

public class FutureAndPastQuestionsExpandableAdapter extends BaseExpandableListAdapter {
    private String TAG = FutureAndPastQuestionsExpandableAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<Pair<String, Question>>  mQuestions;

    private boolean mIsFuture; //true if future false if past
    // Percentage bar
    private HexagonView mPercentageBar;
    private RecyclerView mPercentageRV;
    private HexagonalBarAdapter mHexBarAdapter;

    // List of percentages
    private ArrayList<Float> mPercentages;

    public FutureAndPastQuestionsExpandableAdapter(Context context, ArrayList<Pair<String, Question>> questions, boolean isFuture) {
        mContext = context;
        this.mQuestions = questions;
        this.mIsFuture = isFuture;
    }


    @Override
    public int getGroupCount() {
        return mQuestions.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return this.mQuestions.get(i).second;
    }

    @Override
    public Object getChild(int i, int i1) {
        return mQuestions.get(i).second.getOptions().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Question question = (Question) getGroup(i);

        // Inflating header layout and setting text
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.question_header, viewGroup, false);
        }

        TextView header_text = (TextView) view.findViewById(R.id.headerTV);
        header_text.setText("Pergunta "+String.valueOf(i+1));

        ImageView headerMarkerIV = view.findViewById(R.id.headerMarkerIV);

        // If group is expanded then change the text into bold and change the
        if (b) {
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

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.question_grid_view, null);
        }
        //Question Data
        Question question = mQuestions.get(i).second;
        String questionId = mQuestions.get(i).first;



        TextView questionTV = view.findViewById(R.id.questionTV);
        questionTV.setText( question.getQuestion());

        CustomGridView gridView = (CustomGridView) view
                .findViewById(R.id.questionGV);

        gridView.setNumColumns(2);// gridView.setGravity(Gravity.CENTER);//
        gridView.setHorizontalSpacing(10);// SimpleAdapter mExpandableQuestionsAdapter =

        QuestionGridAdapter adapter = new QuestionGridAdapter(mContext, question.getOptions());
        gridView.setAdapter(adapter);// Adapter

        int totalHeight = 0;
        for (int size = 0; size < adapter.getCount(); size++) {
            RelativeLayout relativeLayout = (RelativeLayout) adapter.getView(
                    size, null, gridView);
            TextView answerTV = (TextView) relativeLayout.getChildAt(0);
            answerTV.measure(0, 0);
            totalHeight += answerTV.getMeasuredHeight();
        }
        gridView.SetHeight(totalHeight);

        //HEXAGON PERCENTAGE STUFF
        // TODO: Take care you should call autoInit always
        mPercentageBar =  view.findViewById(R.id.percentageBar);
        mPercentageBar.autoInit();

        // Update when this is called
        updatePercentages(question.getOptions());
        mPercentageBar.setConfig(mPercentages);

        // Setting percentage view stuff
        mPercentageRV = view.findViewById(R.id.percentageRV);
        mHexBarAdapter = new HexagonalBarAdapter(((Activity)mContext), question.getOptions());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setAutoMeasureEnabled(true);
        mPercentageRV.setLayoutManager(layoutManager);
        mPercentageRV.setAdapter(mHexBarAdapter);

        //if is future then there isn any vote, no need to show
        if(!mIsFuture)
            mPercentageBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent votersIntent = new Intent(mContext, VotersListActivity.class);
                    ArrayList<Integer> questionsIndex = new ArrayList<>();
                    int i = 0;
                    for(QuestionOptions questionOptions : question.getOptions()){
                        questionsIndex.add(i);
                        i++;
                    }
                    votersIntent.putExtra(VotersListActivity.QUESTIONS_IDS, questionsIndex);
                    votersIntent.putExtra(VotersListActivity.VOTERS_REF_STRING,
                            question.getAgendaRef().collection("questions").document(questionId).collection("votes").getPath());
                    mContext.startActivity(votersIntent);
                }
            });
        else
            mPercentageBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.no_voters), Toast.LENGTH_SHORT).show();
                }
            });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public void updatePercentages(ArrayList<QuestionOptions> options){
        mPercentages = new ArrayList<>();
        for (int i=0;i<options.size();i++) {
            mPercentages.add(i, (float) options.get(i).getScore());
        }
    }
}
