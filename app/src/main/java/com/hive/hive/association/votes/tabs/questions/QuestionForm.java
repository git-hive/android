package com.hive.hive.association.votes.tabs.questions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.hive.hive.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionForm extends AppCompatActivity {

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
}
