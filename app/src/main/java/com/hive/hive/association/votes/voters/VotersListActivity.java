package com.hive.hive.association.votes.voters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.votes.VotesHelper;
import com.hive.hive.model.association.Vote;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.GlideApp;
import com.hive.hive.utils.Utils;

import java.util.ArrayList;

import static com.hive.hive.utils.Utils.getCharForNumber;

public class VotersListActivity extends AppCompatActivity {
    private final String TAG = VotersListActivity.class.getSimpleName();

    //voters things
    public final static String VOTERS_REF_STRING = "voterRef";
    public final static String QUESTIONS_IDS = "questionsIds";

    String votersRef;

    RecyclerView mSupportProfileRV;
    RecyclerView mSupportFilterRV;

    //Adapters
    ProfileFilterAdapter mFilterListAdapter;

    ArrayList<User> mUsers;
    ArrayList<String> mAlphabet;
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
        GlideApp.with(getApplicationContext()).resumeRequestsRecursive();
    }

    @Override
    public void onStop(){
        super.onStop();
        GlideApp.with(getApplication()).pauseRequestsRecursive();
    }

}
