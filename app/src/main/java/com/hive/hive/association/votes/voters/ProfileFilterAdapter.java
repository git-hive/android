package com.hive.hive.association.votes.voters;

/**
 * Created by birck on 05/04/18.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.hive.hive.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProfileFilterAdapter extends RecyclerView.Adapter<ProfileFilterAdapter.MyViewHolder> {

    private String TAG = ProfileFilterAdapter.class.getSimpleName();
    private Context mContext;
    //filter stuff
    private List<String> mQuestionOptions;
    private ArrayList<Integer> mIndexOptions;
    public String mCurrentSelected;

    //userList stuff
    private ArrayList<User> mUsers;
    private ArrayList<String> mUsersOptions;
    private ProfileListAdapter mProfileListAdapter;

    private RecyclerView mSupportProfileRV;

    private com.google.firebase.firestore.EventListener<QuerySnapshot> mVotersEL;
    private ListenerRegistration mVotersLR;

    private String votersRef;

    protected ProfileFilterAdapter(Context context, List<String> mQuestionOptions, ArrayList<Integer> mIndexOptions,
                                RecyclerView mSupportProfileRV, String votersRef) {
        this.mContext = context;
        this.mQuestionOptions = mQuestionOptions;
        this.mIndexOptions = mIndexOptions;
        this.mCurrentSelected = "";
        this.mSupportProfileRV = mSupportProfileRV;
        this.votersRef = votersRef;

        getProfileData();
    }

    public ListenerRegistration getmVotersLR() {
        return mVotersLR;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_text_view, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String option = mQuestionOptions.get(position);
        holder.name.setText(option);
        holder.name.setTextColor(Color.BLACK);

        if(holder.name.getText() == mCurrentSelected ||  (holder.name.getText().toString().contains("Todos") && mCurrentSelected == ""))
            holder.name.setTextColor(Color.parseColor("#ffa500"));
        else
            holder.name.setTextColor(Color.BLACK);

        //filter
        View.OnClickListener filterOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentSelected = (String) holder.name.getText();
                notifyDataSetChanged();
//                to filter the users
                if(position != 0) {
                    mUsers.clear();
                    mUsersOptions.clear();
                    if(mVotersLR != null) mVotersLR.remove();
                    Log.d(TAG, "index option " + mIndexOptions.get(position-1));
                    mVotersLR = VotesHelper.getVoters(FirebaseFirestore.getInstance(), votersRef, mIndexOptions.get(position-1)).addSnapshotListener(mVotersEL);
                    mProfileListAdapter.notifyDataSetChanged();
                }else {
                    mUsers.clear();
                    mUsersOptions.clear();
                    if(mVotersLR != null) mVotersLR.remove();
                    mVotersLR = VotesHelper.getVoters(FirebaseFirestore.getInstance(), votersRef, null).addSnapshotListener(mVotersEL);
                }
            }
        };
        holder.name.setOnClickListener(filterOnclick);

        holder.frame.setOnClickListener(filterOnclick);
    }

    @Override
    public int getItemCount() {
        return mQuestionOptions.size();
    }
    private void getProfileData(){
        // Setting profile list content
        mUsers = new ArrayList<>();
        mUsersOptions = new ArrayList<>();
        mProfileListAdapter = new ProfileListAdapter(mUsers, mUsersOptions, mContext);
        LinearLayoutManager vertcalLayoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

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
                            Vote vote = dc.getDocument().toObject(Vote.class);
                            DocumentReference userRef = vote.getAuthorRef();
                            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = documentSnapshot.toObject(User.class);
                                    mUsersOptions.add(Utils.getCharForNumber(vote.getVotingOption()+1));
                                    mUsers.add(user);
                                    mProfileListAdapter.notifyDataSetChanged();
//                                    mFilterListAdapter.notifyDataSetChanged();
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
        mVotersLR = VotesHelper.getVoters(FirebaseFirestore.getInstance(), votersRef, null).addSnapshotListener(mVotersEL);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public LinearLayout frame;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.person_name);
            frame = view.findViewById(R.id.frame);
        }
    }


}
