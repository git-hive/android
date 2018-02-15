package com.hive.hive.association.request;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.model.association.AssociationComment;

import java.util.UUID;

public class CommentaryActivity extends AppCompatActivity {
    private final String TAG = CommentaryActivity.class.getSimpleName();
    public final static String REQUEST_ID = "request_id";
    private TextView mCommentTV;
    private ImageView mCommentIV;
    private String mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary);


        Toolbar toolbar = findViewById(R.id.requestTB);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            Log.d(TAG, "Home as Up setted");
        }
        else
            Log.e(TAG, "Home as Up not setted. Action Bar not found.");

        //getting extra info from Request
        mRequestId = getIntent().getExtras().getString(REQUEST_ID);
        Toast.makeText(this, mRequestId, Toast.LENGTH_SHORT).show();

        //finding views
        mCommentIV = findViewById(R.id.commentIV);
        mCommentTV = findViewById(R.id.commentTV);

        mCommentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCommentTV.getText().equals("")){
                    mCommentTV.setError(getString(R.string.should_text));
                    mCommentTV.requestFocus();
                    return;
                }
                String commentID = UUID.randomUUID().toString();
                String commentText = mCommentTV.getText().toString();
                AssociationComment comment = new AssociationComment(commentID, 0, 0,
                        null, null, null , commentText, 0, null);
                //TODO static associationId
                AssociationHelper.setRequestComment(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mRequestId,
                       commentID,  comment);
                mCommentTV.setText(null);
                mCommentTV.clearFocus();
            }
        });
    }
}
