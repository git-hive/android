package com.hive.hive.association.request.comments;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.model.association.AssociationComment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class CommentaryActivity extends AppCompatActivity {
    private final String TAG = CommentaryActivity.class.getSimpleName();
    public final static String REQUEST_ID = "request_id";


    //--- Views
    private RecyclerView mCommentRV;
    private CommentaryAdapter mRecyclerAdapter;

    private TextView mCommentTV;
    private ImageView mCommentIV;
    private String mRequestId;

    //--- Data
    private ArrayList<String> mIds;
    private HashMap<String, AssociationComment> mComments;

    //--- Listeners
    private EventListener<QuerySnapshot> mCommentEL;
    private ListenerRegistration mCommentLR;


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
                AssociationComment comment = new AssociationComment(commentID, Calendar.getInstance().getTimeInMillis(), 0,
                        null, null, null , commentText, 0, null);
                //TODO static associationId
                AssociationHelper.setRequestComment(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mRequestId,
                       commentID,  comment);
                mCommentTV.setText(null);
                mCommentTV.clearFocus();
            }
        });

        //GETTING ALL REQUESTS
        mComments = new HashMap<>();
        mIds = new ArrayList<>();
        mCommentEL = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            AssociationComment comment = dc.getDocument().toObject(AssociationComment.class);
                            mComments.put(dc.getDocument().getId(), comment);
                            mIds.add(dc.getDocument().getId());
                            mRecyclerAdapter.notifyDataSetChanged();
                            mCommentRV.smoothScrollToPosition(mIds.size()-1);
                            Log.d(TAG, comment.getContent());
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mComments.get(modifiedId).setUpdatedAt(dc.getDocument().getLong("updatedAt"));
                            mComments.remove(modifiedId);
                            mComments.put(modifiedId, dc.getDocument().toObject(AssociationComment.class));
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();
                            mComments.remove(removedId);
                            mIds.remove(removedId);
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                    }
                }

            }
        };

        //TODO change associationID and LIMIT
        //getting the 10 newest comments
        mCommentLR = AssociationHelper.getAllRequestComments(FirebaseFirestore.getInstance(),
                "gVw7dUkuw3SSZSYRXe8s", mRequestId).orderBy("createdAt", Query.Direction.ASCENDING).limit(10).addSnapshotListener(mCommentEL);

        //--- Recycle View Setup

        //Find views
        mCommentRV = findViewById(R.id.commentsRV);

        //Set Size
        mCommentRV.setHasFixedSize(true);

        //Set Adapter
        mRecyclerAdapter = new CommentaryAdapter(this, mComments, mIds, mRequestId);
        mCommentRV.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mCommentLR.remove();
    }
}
