package com.hive.hive.feed;

import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.utils.DocReferences;

import java.util.UUID;

import static com.hive.hive.feed.FeedHelper.setForumPost;


public class NewPostActivity extends AppCompatActivity {

    // Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    // Association
    final String associationID = "gVw7dUkuw3SSZSYRXe8s";

    // Settings
    Context mContext;


    // Views
    EditText mTitleED;
    EditText mDescriptionED;
    Button mSendBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mContext = getApplicationContext();


        // TODO: Remember setting all necessary references
        setViewsReferences();
        //________________________________________________//
        // TODO: Remember setting Click Listeners
        setClickListeners();

    }

    public void setViewsReferences(){
        mTitleED = findViewById(R.id.postTitle);
        mDescriptionED = findViewById(R.id.descriptionET);
        mSendBT = findViewById(R.id.sendButton);
    }

    public void setClickListeners(){

        mSendBT.setOnClickListener(v -> addNewForumPost());

    }

    public  void addNewForumPost(){
        if(mTitleED.getText().toString().equals("")){
            mTitleED.setError(getResources().getString(R.string.feed_should_have_title));
            mTitleED.requestFocus();
            return;
        }
        if(mDescriptionED.getText().toString().equals("")){
            mDescriptionED.setError(getResources().getString(R.string.feed_should_have_description));
            mTitleED.requestFocus();
            return;
        }
        String forumUUID = UUID.randomUUID().toString();
        long currentTimeMillis = System.currentTimeMillis();

        ForumPost forumPost = new ForumPost(
                currentTimeMillis,
                currentTimeMillis,
                DocReferences.getUserRef(),
                null,
                mTitleED.getText().toString(),
                mDescriptionED.getText().toString(),
                null,
                null);

        setForumPost(mDB,associationID, forumUUID, forumPost);
        finish();
    }

}
