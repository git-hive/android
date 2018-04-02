package com.hive.hive.association.votes.tabs.questions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.hive.hive.R;
import com.hive.hive.association.votes.tabs.questions.model.OrderStatus;
import com.hive.hive.association.votes.tabs.questions.model.Orientation;
import com.hive.hive.association.votes.tabs.questions.model.TimeLineModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionForm extends AppCompatActivity {

    // Timeline stuff
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;


    // Data stuff
    private HashMap<Integer, ArrayList<String> > formQuestions;
    private ArrayList<String> arrayList;
    private ArrayList<OrderStatus> mQuestionStatus;
    private ArrayList<Integer> mQuestionStatusValue;
    private GridListAdapter formAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);
        context = this;
        loadListView();

        onClickEvent();

        mOrientation = Orientation.HORIZONTAL;
        mRecyclerView = (RecyclerView) findViewById(R.id.question_timeline_RV);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        initView();


        // Pass Storyline adapter as reference
        formAdapter.setStorylineAdapter(mTimeLineAdapter);

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
        mTimeLineAdapter = new TimeLineAdapter(formQuestions, mQuestionStatus, mQuestionStatusValue);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    // Populate Form locally for now
    private void loadListView() {
        ListView formListView = (ListView) findViewById(R.id.list_view);
        formListView.setDivider(null);
        formListView.setDividerHeight(0);
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

        formAdapter = new GridListAdapter(context, formQuestions, true);
        formListView.setAdapter(formAdapter);
    }



    // Get actions in form
    private void onClickEvent() {
        findViewById(R.id.questionBackBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next Question
                formAdapter.previousQuestion();
            }
        });
        findViewById(R.id.questionNextBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next Question
                formAdapter.nextQuestion();
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
