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

import java.util.ArrayList;

import static com.hive.hive.utils.Utils.getCharForNumber;

public class VotersListActivity extends AppCompatActivity {
    private final String TAG = VotersListActivity.class.getSimpleName();

    //voters things
    public final static String VOTERS_REF_STRING = "voterRef";
    String votersRef;
    //Listeners
    private com.google.firebase.firestore.EventListener<QuerySnapshot> mVotersEL;
    private ListenerRegistration mVotersLR;
    RecyclerView mSupportProfileRV;
    RecyclerView mSupportFilterRV;

    //Adapters
    ProfileFilterAdapter mFilterListAdapter;
    ProfileListAdapter mProfileListAdapter;

    ArrayList<User> mUsers;
    ArrayList<String> mAlphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_list);

        votersRef = this.getIntent().getStringExtra(VOTERS_REF_STRING);

        mUsers = new ArrayList<>();

        // Init Dummy content
        initDataset();


        mSupportFilterRV = findViewById(R.id.superiorFilterRV);
        mSupportProfileRV = findViewById(R.id.supportProfileListRV);


        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);


        // Setting profile filter list content
        mFilterListAdapter = new ProfileFilterAdapter(mAlphabet);

        mSupportFilterRV.setHasFixedSize(true);
        mSupportFilterRV.setLayoutManager(horizontalLayoutManager);
        mSupportFilterRV.setAdapter(mFilterListAdapter);


        LinearLayoutManager vertcalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        // Setting profile list content
        mProfileListAdapter = new ProfileListAdapter(mUsers, this.getApplicationContext());

        mSupportProfileRV.setHasFixedSize(true);
        mSupportProfileRV.setLayoutManager(vertcalLayoutManager);
        mSupportProfileRV.setAdapter(mProfileListAdapter);


        mVotersEL = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG, e.getMessage());
                    return;
                }
                for(DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            DocumentReference userRef = dc.getDocument().toObject(Vote.class).getAuthorRef();
                            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = documentSnapshot.toObject(User.class);
                                    mUsers.add(user);
                                    mProfileListAdapter.notifyDataSetChanged();
                                    Log.d(TAG, user.getName());
                                }
                            });
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        };
        mVotersLR = VotesHelper.getVoters(FirebaseFirestore.getInstance(), votersRef).addSnapshotListener(mVotersEL);


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mVotersLR.remove();
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

    private void initDataset(){
        mAlphabet = new ArrayList<>();
        mAlphabet.add(0, "Todos os 100");
        for(int i=1;i<27;i++){
            mAlphabet.add(i, getCharForNumber(i).toLowerCase());
        }
    }
}
