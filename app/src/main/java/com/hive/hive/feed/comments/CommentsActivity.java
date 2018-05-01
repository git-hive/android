package com.hive.hive.feed.comments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.request.RequestAdapter;
import com.hive.hive.model.association.AssociationComment;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.GlideApp;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class CommentsActivity extends AppCompatActivity {
    private final String TAG = CommentsActivity.class.getSimpleName();
    public final static String FORUM_POST_ID = "forum_post_id";


    //--- Views
    private RecyclerView mCommentRV;
    private CommentsAdapter mRecyclerAdapter;

    private EditText mCommentET;
    private ImageView mCommentIV;
    private String mRequestId;

    private TextView mRequestAuthorTV;
    private TextView mRequestTitleTV;
    private TextView mRequestCommentsCountTV;
    private TextView mRequestSupportsCountTV;
    private TextView mRequestContentTV;

    private ImageView mRequestAuthorIV;
    private ImageView mRequestSupportsIV;
    //--- Data
    private ArrayList<String> mIds;
    private HashMap<String, AssociationComment> mComments;
    private Request mRequest;
    private SupportMutex mSupportMutex;
    //--- Listeners
    private EventListener<QuerySnapshot> mCommentEL;
    private EventListener<DocumentSnapshot> mRequestEL;
    private ListenerRegistration mCommentLR;
    private ListenerRegistration mRequestLR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


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
        mRequestId = getIntent().getExtras().getString(FORUM_POST_ID);
        Toast.makeText(this, mRequestId, Toast.LENGTH_SHORT).show();

        //finding views
        mCommentIV = findViewById(R.id.commentIV);
        mCommentET = findViewById(R.id.commentET);

        mRequestAuthorTV = findViewById(R.id.request_author_name_tv);
        mRequestTitleTV = findViewById(R.id.request_title_tv);
        mRequestCommentsCountTV = findViewById(R.id.request_number_of_comments_tv);
        mRequestSupportsCountTV = findViewById(R.id.supportsTV);
        mRequestContentTV = findViewById(R.id.request_content_tv);

        mRequestAuthorIV = findViewById(R.id.request_author_photo_iv);
        mRequestSupportsIV = findViewById(R.id.supportsIV);
        //creating support mutex
        mSupportMutex = new SupportMutex(mRequestSupportsCountTV, mRequestSupportsIV);
        //onclick to save comment
        mCommentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCommentET.getText().toString().trim().equals("")){
                    mCommentET.setError(getString(R.string.should_text));
                    mCommentET.requestFocus();
                    return;
                }
                String commentID = UUID.randomUUID().toString();
                String commentText = mCommentET.getText().toString();
                AssociationComment comment = new AssociationComment( Calendar.getInstance().getTimeInMillis(),
                        Calendar.getInstance().getTimeInMillis(),
                        DocReferences.getUserRef(), null, DocReferences.getAssociationRef("gVw7dUkuw3SSZSYRXe8s"),
                        commentText, 0, DocReferences.getRequestRef("gVw7dUkuw3SSZSYRXe8s", mRequestId));
                //TODO static associationId
                AssociationHelper.setRequestComment(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mRequestId,
                       commentID,  comment);
                mCommentET.setText(null);
                mCommentET.clearFocus();
            }
        });
        //onclick support request
        mRequestSupportsCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick();
            }
        });
        mRequestSupportsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick();
            }
        });
        //GETTING REQUEST
        mRequestEL = new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                if(documentSnapshot.exists()) {
                    mRequest = documentSnapshot.toObject(Request.class);
                    updateRequestUI();
                    shouldFillSupport();
                    fillUser(mRequest.getAuthorRef());
                }
            }
        };
        mRequestLR = AssociationHelper.getRequest(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", mRequestId)
                .addSnapshotListener(mRequestEL);

        //GETTING ALL Comments
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
                "gVw7dUkuw3SSZSYRXe8s", mRequestId).orderBy("createdAt", Query.Direction.ASCENDING).limit(10)
                .addSnapshotListener(mCommentEL);

        //--- Recycle View Setup

        //Find views
        mCommentRV = findViewById(R.id.commentsRV);

        //Set Size
        mCommentRV.setHasFixedSize(true);

        //Set Adapter
        mRecyclerAdapter = new CommentsAdapter(this, mComments, mIds, mRequestId);
        mCommentRV.setAdapter(mRecyclerAdapter);

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        //removing db listeners
        mCommentLR.remove();
        mRequestLR.remove();
    }
    private void updateRequestUI(){
        //mRequestAuthorTV.setText(mRequest.get);
        mRequestTitleTV.setText(mRequest.getTitle());
        //mRequestCommentsCountTV.setText(mRequest.get);
        mRequestSupportsCountTV.setText(mRequest.getScore()+"");
        mRequestContentTV.setText(mRequest.getContent());

        //private ImageView mRequestAuthorIV;
        //private ImageView mRequestSupportsIV;
    }
    private void scoreClick(){
        mSupportMutex.lock();
        AssociationHelper.getRequestSupport(FirebaseFirestore.getInstance(),
                "gVw7dUkuw3SSZSYRXe8s", mRequestId, FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //if support already exists, should delete it
                        if(documentSnapshot.exists()) {
                            AssociationHelper.removeRequestSupport(FirebaseFirestore.getInstance(),
                                    "gVw7dUkuw3SSZSYRXe8s", mRequestId, FirebaseAuth.getInstance().getUid())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mSupportMutex.unlock();
                                }
                            });
                        }else {// else should add it
                            DocumentReference userRef = DocReferences.getUserRef();
                            DocumentReference assocRef = DocReferences.getAssociationRef("gVw7dUkuw3SSZSYRXe8s");
                            String supportId = FirebaseAuth.getInstance().getUid();
                            //TODO review refs

                            AssociationSupport support = new AssociationSupport( Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis(),
                                    userRef, null, assocRef, null);
                            AssociationHelper.setRequestSupport(FirebaseFirestore.getInstance(),
                                    "gVw7dUkuw3SSZSYRXe8s", mRequestId, supportId, support).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mSupportMutex.unlock();
                                }
                            });
                        }
                    }
                });
    }
    private void shouldFillSupport(){
        //if exists support, then should be IV filled
        AssociationHelper.getRequestSupport(FirebaseFirestore.getInstance(),
                "gVw7dUkuw3SSZSYRXe8s", mRequestId, FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                            mRequestSupportsIV.setImageDrawable(CommentsActivity.this.getResources().getDrawable(R.drawable.ic_support_filled));
                        else
                            mRequestSupportsIV.setImageDrawable(CommentsActivity.this.getResources().getDrawable(R.drawable.ic_support_borderline));
                    }
                });
    }
    private void fillUser(DocumentReference userRef){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(RequestAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    mRequestAuthorTV.setText(user.getName());
                    ProfilePhotoHelper.loadImage(getApplicationContext(), mRequestAuthorIV, user.getPhotoUrl());
                }
            }
        });
    }
}
