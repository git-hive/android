package com.hive.hive.association.votes.tabs.questions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.hive.hive.R;

import java.util.ArrayList;

public class QuestionForm extends AppCompatActivity {
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

    private void loadListView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        arrayList = new ArrayList<>();
        for (int i = 1; i <= 4; i++)
            arrayList.add("ListView Items " + i);

        adapter = new GridListAdapter(context, arrayList, true);
        listView.setAdapter(adapter);
    }

    private void onClickEvent() {
        findViewById(R.id.questionBackBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected position
                adapter.getSelectedItem();
            }
        });
        findViewById(R.id.questionNextBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete the selected position
                adapter.deleteSelectedPosition();
            }
        });

    }
}
