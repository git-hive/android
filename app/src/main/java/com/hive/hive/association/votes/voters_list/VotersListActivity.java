package com.hive.hive.association.votes.voters_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.hive.hive.R;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.Utils;

import java.util.ArrayList;

public class VotersListActivity extends AppCompatActivity {
    private final String TAG = VotersListActivity.class.getSimpleName();

    //voters things
    public final static String VOTERS_REF_STRING = "voterRef";
    public final static String QUESTIONS_IDS = "questionsIds";

    String votersRef;

    RecyclerView mSupportProfileRV;
    RecyclerView mSupportFilterRV;

    ImageView mBackArrowIV;

    //Adapters
    ProfileFilterAdapter mFilterListAdapter;

    ArrayList<User> mUsers;
    ArrayList<Integer> mQuestionOptions;
    ArrayList<String> mFilterOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_list);


        votersRef = this.getIntent().getStringExtra(VOTERS_REF_STRING);

        mQuestionOptions = this.getIntent().getIntegerArrayListExtra(QUESTIONS_IDS);
        mFilterOptions = new ArrayList<>();
        mFilterOptions.add("Todos");
        for(int option : mQuestionOptions) {
            //plus one to convert 0 too
            mFilterOptions.add(Utils.getCharForNumber(option+1));
            Log.d(TAG, Utils.getCharForNumber(option+1));
        }
        mUsers = new ArrayList<>();

        mSupportFilterRV = findViewById(R.id.superiorFilterRV);
        mSupportProfileRV = findViewById(R.id.supportProfileListRV);

        mBackArrowIV = findViewById(R.id.exitVotesIV);
        mBackArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);


        // Setting profile filter list content
        mFilterListAdapter = new ProfileFilterAdapter(getApplicationContext(), mFilterOptions, mQuestionOptions, mSupportProfileRV, votersRef);

        mSupportFilterRV.setHasFixedSize(true);
        mSupportFilterRV.setLayoutManager(horizontalLayoutManager);
        mSupportFilterRV.setAdapter(mFilterListAdapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mFilterListAdapter.getmVotersLR().remove();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

}
