package com.hive.hive.feed.comments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
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

import com.hive.hive.firebaseHelpers.FeedHelper;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.model.association.BudgetTransactionCategories;
import com.hive.hive.model.forum.ForumComment;
import com.hive.hive.model.forum.ForumPost;
import com.hive.hive.model.forum.ForumSupport;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;

import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class FeedCommentsActivity extends AppCompatActivity {
    private final String TAG = FeedCommentsActivity.class.getSimpleName();
    public final static String Post_ID = "Post_id";


    //--- Views
    private RecyclerView mCommentRV;
    private  FeedCommentsAdapter mRecyclerAdapter;

    private EditText mCommentET;
    private ImageView mCommentIV;

    private TextView mPostAuthorTV;
    private TextView mPostTitleTV;
    private TextView mPostCommentsCountTV;
    private TextView mPostSupportsCountTV;
    private TextView mPostContentTV;
    private TextView mPostCost;

    private ImageView mPostCategory;
    private ImageView mPostAuthorIV;
    private ImageView mPostSupportsIV;
    //--- Data
    private ArrayList<String> mIds;
    private HashMap<String, ForumComment> mComments;

    private String mPostId;
    private ForumPost mPost;
    private SupportMutex lock;
    private boolean PostSupport;
    private boolean PostSupportHasChanged = false;

    private HashMap<String, Integer> budgetCategoryNameResource;

    //--- Listeners
    private EventListener<QuerySnapshot> mCommentEL;
    private EventListener<DocumentSnapshot> mPostEL;
    private ListenerRegistration mCommentLR;
    private ListenerRegistration mPostLR;

    //--
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        initViews();

        //getting extra info from Post
        mPostId = getIntent().getExtras().getString(Post_ID);
        lock = new SupportMutex();

        budgetCategoryNameResource = new HashMap<>();
        budgetCategoryNameResource.put(
                BudgetTransactionCategories.EXTRAORDINARY,
                R.drawable.ic_budget_category_extraordinary
        );
        budgetCategoryNameResource.put(
                BudgetTransactionCategories.ORDINARY,
                R.drawable.ic_budget_category_ordinary
        );
        budgetCategoryNameResource.put(
                BudgetTransactionCategories.SAVINGS,
                R.drawable.ic_budget_category_savings
        );

        onclicks();
        //GETTING Post
        mPostEL = new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                if (documentSnapshot.exists()) {
                    mPost = documentSnapshot.toObject(ForumPost.class);
                    updatePostUI();
                    fillUser(mPost.getAuthorRef());
                } else {
                    finish();
                }
            }
        };

        shouldFillSupport();


        mPostLR = FeedHelper.getForumPost(mPostId)
                .addSnapshotListener(mPostEL);

        //GETTING ALL Comments
        mComments = new HashMap<>();
        mIds = new ArrayList<>();
        mCommentEL = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            ForumComment comment = dc.getDocument().toObject(ForumComment.class);
                            mComments.put(dc.getDocument().getId(), comment);
                            mIds.add(dc.getDocument().getId());
                            mRecyclerAdapter.notifyDataSetChanged();
                            mCommentRV.smoothScrollToPosition(mIds.size() - 1);
                            mPostCommentsCountTV.setText(mComments.size() + "");
                            Log.d(TAG, comment.getContent());
                            break;
                        case MODIFIED:
                            String modifiedId = dc.getDocument().getId();
                            mComments.get(modifiedId).setUpdatedAt(dc.getDocument().getLong("updatedAt"));
                            mComments.remove(modifiedId);
                            mComments.put(modifiedId, dc.getDocument().toObject(ForumComment.class));
                            if (mRecyclerAdapter.getChangedSupportsCommentsIds().contains(modifiedId)) {
                                if (mRecyclerAdapter.getChangedSupports().get(modifiedId)) {//has changed and it is a like
                                    mComments.get(modifiedId).incrementScore();
                                } else {// has changed but it itsn a like
                                    mComments.get(modifiedId).decrementScore();
                                }
                            }
                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            String removedId = dc.getDocument().getId();
                            mComments.remove(removedId);
                            mIds.remove(removedId);

                            //remove like if needed
                            mRecyclerAdapter.getChangedSupports().remove(removedId);
                            mRecyclerAdapter.getChangedSupportsCommentsIds().remove(removedId);

                            mRecyclerAdapter.notifyDataSetChanged();
                            break;
                    }
                }

            }
        };

        //TODO change associationID and LIMIT
        //getting the 10 newest comments
        mCommentLR = FeedHelper.getAllForumPostComments(mPostId).orderBy("createdAt", Query.Direction.ASCENDING).limit(10)
                .addSnapshotListener(mCommentEL);

        //--- Recycle View Setup

        //Find views
        mCommentRV = findViewById(R.id.commentsRV);

        //Set Size
        mCommentRV.setHasFixedSize(true);
        mCommentRV.setLayoutManager(new LinearLayoutManager(this));
        //Set Adapter
        mRecyclerAdapter = new FeedCommentsAdapter(this, mComments, mIds, mPostId);
        mCommentRV.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {

        super.onStop();
        sendToFirebase();
        mRecyclerAdapter.sendToFirebase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.sendToFirebase();
        sendToFirebase();
        //removing db listeners
        mCommentLR.remove();
        mPostLR.remove();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.requestTB);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Log.d(TAG, "Home as Up setted");
        } else
            Log.e(TAG, "Home as Up not setted. Action Bar not found.");

        //finding views
        mCommentIV = findViewById(R.id.commentIV);
        mCommentET = findViewById(R.id.commentET);

        mPostAuthorTV = findViewById(R.id.request_author_name_tv);
        mPostTitleTV = findViewById(R.id.request_title_tv);
        mPostCommentsCountTV = findViewById(R.id.request_number_of_comments_tv);
        mPostSupportsCountTV = findViewById(R.id.supportsTV);
        mPostContentTV = findViewById(R.id.request_content_tv);
        mPostCost = findViewById(R.id.request_cost_tv);

        mPostCategory = findViewById(R.id.request_budget_category_iv);
        mPostAuthorIV = findViewById(R.id.request_author_photo_iv);
        mPostSupportsIV = findViewById(R.id.supportsIV);
    }

    private void onclicks() {
        //onclick to save comment
        mCommentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCommentET.getText().toString().trim().equals("")) {
                    mCommentET.setError(getString(R.string.should_text));
                    mCommentET.requestFocus();
                    return;
                }
                String commentID = UUID.randomUUID().toString();
                String commentText = mCommentET.getText().toString();
                ForumComment comment = new ForumComment(Calendar.getInstance().getTimeInMillis(),
                        Calendar.getInstance().getTimeInMillis(),
                        DocReferences.getUserRef(), null, commentText);
                //TODO static associationId
                FeedHelper.setForumPostComment(mPostId,
                        commentID, comment);
                mCommentET.setText(null);
                mCommentET.clearFocus();
            }
        });


        //onclick support Post
        mPostSupportsCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostSupport();
            }
        });
        mPostSupportsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostSupport();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void updatePostUI() {
        //mPostAuthorTV.setText(mPost.get);
        mPostTitleTV.setText(mPost.getTitle());
        mPostCommentsCountTV.setText(mPost.getNumComments() + "");
        mPostSupportsCountTV.setText(mPost.getSupportScore() + "");
        mPostContentTV.setText(mPost.getContent());

        //no need of these fields
        mPostCategory.setVisibility(View.GONE);
        mPostCost.setVisibility(View.GONE);
        mPostCategory.setVisibility(View.GONE);

        shouldFillSupport();
        //private ImageView mPostAuthorIV;
        //private ImageView mPostSupportsIV;
    }


    private void togglePostSupport() {
        Drawable filledSupportIC = this
                .getResources()
                .getDrawable(R.drawable.ic_support_filled);

        Drawable borderlineSupportIC = this
                .getResources()
                .getDrawable(R.drawable.ic_support_borderline);

        if (PostSupport) {// if im supporting
            mPostSupportsIV.setImageDrawable(borderlineSupportIC);
            mPost.decrementScore();
            if (PostSupportHasChanged) { //if im changing my mind
                PostSupportHasChanged = false;
            } else {
                PostSupportHasChanged = true;
            }
        } else {
            mPostSupportsIV.setImageDrawable(filledSupportIC);
            mPost.incrementScore();
            if (PostSupportHasChanged) {// if im changing my mind
                PostSupportHasChanged = false;
            } else {
                PostSupportHasChanged = true;
            }
        }
        mPostSupportsCountTV.setText(String.valueOf(mPost.getSupportScore()));
        PostSupport = !PostSupport;
    }


    public void sendToFirebase() {
        lock.lock();
        if (PostSupportHasChanged) {
            supportActionHandler(mPostId);
            PostSupportHasChanged = false;
        }
        lock.unlock();
    }


    private void supportActionHandler(
            String PostID) {

        // Create or remove support
        DocumentReference userRef = DocReferences.getUserRef();
        DocumentReference assocRef = DocReferences.getAssociationRef(HomeFragment.mCurrentAssociationId);
        String supportId = FirebaseAuth.getInstance().getUid();

        // TODO: Add missing refs
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        ForumSupport support = new ForumSupport(
                currentTimeInMillis,
                currentTimeInMillis,
                userRef,
                null);

        FeedHelper.setForumPostSupport(
                PostID,
                supportId,
                support
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void shouldFillSupport(){
        //if exists support, then should be IV filled
        FeedHelper.getForumPostSupport(mPostId, FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            mPostSupportsIV.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_support_filled));
                            PostSupport = true;
                        }else {
                            mPostSupportsIV.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_support_borderline));
                            PostSupport = false;
                        }
                    }
                });
    }
    private void fillUser(DocumentReference userRef){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
//                    Log.d(PostAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    mPostAuthorTV.setText(user.getName());
                    ProfilePhotoHelper.loadImage(getApplicationContext(), mPostAuthorIV, user.getPhotoUrl());
                }
            }
        });
    }
}
