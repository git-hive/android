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
import com.hive.hive.association.votes.VotesActivity;
import com.hive.hive.association.votes.tabs.questions.model.OrderStatus;
import com.hive.hive.association.votes.tabs.questions.model.Orientation;
import com.hive.hive.association.votes.tabs.questions.model.TimeLineModel;
import com.hive.hive.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionForm extends AppCompatActivity {

    // Timeline stuff
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;

    // Data stuff
    private HashMap<Integer, ArrayList<String> > formQuestions;
    private ArrayList<String> arrayList;
    private GridListAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);
        context = getApplicationContext();
        loadListView();
        onClickEvent();
        mOrientation = Orientation.HORIZONTAL;
        mWithLinePadding = false;

        setTitle(mOrientation == Orientation.HORIZONTAL ? getResources().getString(R.string.horizontal_timeline) : getResources().getString(R.string.vertical_timeline));

        mRecyclerView = (RecyclerView) findViewById(R.id.question_timeline_RV);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();

    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }

    private void initView() {
        setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems(){
        mDataList.add(new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
    }

    // Populate Form locally for now
    private void loadListView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        formQuestions = new HashMap<Integer, ArrayList<String>>();
        for (Integer i = 0; i <= 4; i++){
            arrayList = new ArrayList<>();
            for (int j = 0; j <= 4; j++)
                arrayList.add("ListView Items " + j + " from "+ i);

            formQuestions.put(i,  arrayList);

        }

        adapter = new GridListAdapter(context, formQuestions, true);
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
