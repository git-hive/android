package com.hive.hive.association.votes.tabs.questions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.association.votes.tabs.questions.model.OrderStatus;
import com.hive.hive.association.votes.tabs.questions.model.Orientation;
import com.hive.hive.association.votes.tabs.questions.model.TimeLineModel;
import com.hive.hive.model.association.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionForm extends AppCompatActivity {
    private TextView mQuestionTV;
    // Timeline stuff
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;

    // Data stuff
    private HashMap<Integer, ArrayList<String> > formQuestions;
    private ArrayList<String> arrayList;
    private ArrayList<OrderStatus> mQuestionStatus;
    private ArrayList<Integer> mQuestionStatusValue;
    private GridListAdapter adapter;
    private Context context;

    HashMap<String, Question> mQuestions;
    ArrayList<String> mQuestionsIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);
        context = this;

        mQuestionTV = findViewById(R.id.questionTV);

        mQuestionsIds = (ArrayList<String>) getIntent().getSerializableExtra("questionsIds");
        mQuestions = (HashMap<String, Question>) getIntent().getSerializableExtra("questions");

        loadListView();

        onClickEvent();

        mOrientation = Orientation.HORIZONTAL;
        mWithLinePadding = false;
        mRecyclerView = findViewById(R.id.question_timeline_RV);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        initView();

        // Pass Storyline adapter as reference
        adapter.setStorylineAdapter(mTimeLineAdapter);

        mQuestionTV.setText(mQuestions.get(mQuestionsIds.get(0)).getQuestion());
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }

    private void initView() {
        //setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(formQuestions, mQuestionStatus, mQuestionStatusValue, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    // Populate Form locally for now
    private void loadListView() {
        ListView listView = findViewById(R.id.list_view);

        formQuestions = new HashMap<Integer, ArrayList<String>>();
        mQuestionStatus = new ArrayList<OrderStatus>();
        mQuestionStatusValue = new ArrayList<Integer>();

        for (Integer i = 0; i <= 4; i++){
            arrayList = new ArrayList<>();
            for (int j = 0; j <= 4; j++)
                arrayList.add("ListView Items " + j + " from "+ i);

            formQuestions.put(i,  arrayList);
            mQuestionStatus.add(i, OrderStatus.INACTIVE);
            mQuestionStatusValue.add(i, -1);
        }

        //Set First manually
        mQuestionStatus.set(0, OrderStatus.ACTIVE);

        adapter = new GridListAdapter(context, mQuestions, mQuestionsIds, mQuestionTV,true);
        listView.setAdapter(adapter);
    }



    // Get actions in form
    private void onClickEvent() {
        findViewById(R.id.questionBackBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next Question
                adapter.previousQuestion();
            }
        });
        findViewById(R.id.questionNextBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next Question
                adapter.nextQuestion();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if(mOrientation!=null)
            savedInstanceState.putSerializable("EXTRA_ORIENTATION", mOrientation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("EXTRA_ORIENTATION")) {
                mOrientation = (Orientation) savedInstanceState.getSerializable("EXTRA_ORIENTATION");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

}
