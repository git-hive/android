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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.association.votes.tabs.questions.model.OrderStatus;
import com.hive.hive.association.votes.tabs.questions.model.Orientation;
import com.hive.hive.association.votes.tabs.questions.model.TimeLineModel;
import com.hive.hive.model.association.Question;
import com.hive.hive.model.association.Vote;

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


    // Data stuff
    private ArrayList<OrderStatus> mQuestionStatus;
    private ArrayList<Integer> mQuestionStatusValue;
    private GridListAdapter formAdapter;
    private Context context;


    HashMap<String, Question> mQuestions;
    ArrayList<String> mQuestionsIds;

    ArrayList<Vote> mVotes;

    String mAssociationID;
    String mSessionID;
    String mAgendaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);
        context = this;

        mQuestionTV = findViewById(R.id.questionTV);

        //Getting the Questions from the previusly activity
        mQuestionsIds = (ArrayList<String>) getIntent().getSerializableExtra("questionsIds");
        mQuestions = (HashMap<String, Question>) getIntent().getSerializableExtra("questions");

        //Getting ids to send vote
        mAssociationID = getIntent().getStringExtra("associationID");
        mSessionID = getIntent().getStringExtra("sessionID");
        mAgendaID = getIntent().getStringExtra("agendaID");
        //Create  Votes
        mVotes = new ArrayList<>();

        loadListView();

        onClickEvent();

        mOrientation = Orientation.HORIZONTAL;

        mRecyclerView = (RecyclerView) findViewById(R.id.question_timeline_RV);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        initView();

        // Pass Storyline adapter as reference
        formAdapter.setStorylineAdapter(mTimeLineAdapter);

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

        mTimeLineAdapter = new TimeLineAdapter(mQuestions, mQuestionStatus, mQuestionStatusValue, QuestionForm.this);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void loadListView() {

        ListView formListView =  findViewById(R.id.list_view);
        formListView.setDivider(null);
        formListView.setDividerHeight(0);
        mQuestionStatus = new ArrayList<>();
        mQuestionStatusValue = new ArrayList<>();

        for (Integer i = 0; i < mQuestions.size(); i++){
            mQuestionStatus.add(i, OrderStatus.INACTIVE);
            mQuestionStatusValue.add(i, -1);
        }

        //Set First manually
        mQuestionStatus.set(0, OrderStatus.ACTIVE);


        formAdapter = new GridListAdapter(context, mQuestions, mQuestionsIds, mQuestionTV,true);
        formListView.setAdapter(formAdapter);
    }



    // Get actions in form
    private void onClickEvent() {

        findViewById(R.id.questionBackTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next Question
                formAdapter.previousQuestion();
            }
        });

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
                Vote vote = formAdapter.getSelectedVote();
                if(vote != null) {
                    mVotes.add(formAdapter.getSelectedVote());
                    if(formAdapter.nextQuestion()){
                        VotesHelper.setVote(FirebaseFirestore.getInstance(), mAssociationID, mSessionID, mAgendaID, mQuestionsIds
                                , mTimeLineAdapter.mStatusListValue, mVotes, context);
                        finish();
                    }
                }else{
                Toast.makeText(QuestionForm.this, getString(R.string.should_answer), Toast.LENGTH_SHORT).show();
                }
             //   QuestionOptions currentOptions = mQuestions.get(mQuestionsIds.get())
            }
        });


        findViewById(R.id.questionNextTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next Question
                Vote vote = formAdapter.getSelectedVote();
                if(vote != null) {
                    mVotes.add(formAdapter.getSelectedVote());
                    if(formAdapter.nextQuestion()){
                        VotesHelper.setVote(FirebaseFirestore.getInstance(), mAssociationID, mSessionID, mAgendaID, mQuestionsIds
                                , mTimeLineAdapter.mStatusListValue, mVotes, context);
                        finish();
                    }
                }else{
                    Toast.makeText(QuestionForm.this, getString(R.string.should_answer), Toast.LENGTH_SHORT).show();
                }
                //   QuestionOptions currentOptions = mQuestions.get(mQuestionsIds.get())
            }
        });

        findViewById(R.id.backArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
