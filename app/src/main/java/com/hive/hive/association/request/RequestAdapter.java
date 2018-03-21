package com.hive.hive.association.request;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.request.comments.CommentaryActivity;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by vplentz on 04/02/18.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    //-- Data
    private HashMap<String,  Request> mRequests;
    private ArrayList<String> mIds;
    private ArrayList<SupportMutex> mLocks;
    private Context context;
    public RequestAdapter(HashMap<String, Request> requests, ArrayList<String> mIds, Context context){
        this.mRequests = requests;
        this.mIds = mIds;
        this.context = context;
        mLocks = new ArrayList<>();
    }
    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        try{
            if(mLocks.get(position) == null) mLocks.add(new SupportMutex(holder.mNumberOfSupportsTV, holder.mSupportsIV));
        }catch(java.lang.IndexOutOfBoundsException e){
            mLocks.add(new SupportMutex(holder.mNumberOfSupportsTV, holder.mSupportsIV));
        }
        final Request request = mRequests.get(mIds.get(position));

        holder.mItem = request;

        //fill user
        fillUser(holder, request.getAuthorRef());
        //        holder.mUserAvatar.setImageResource(R.drawable.ic_profile_photo);
        //fill request
        holder.mRequestTitle.setText(request.getTitle());
        holder.mRequestContent.setText(request.getContent());
        holder.mNumberOfSupportsTV.setText(String.valueOf(request.getScore()));
        holder.mNumberOfCommentsTV.setText(String.valueOf(request.getNumComments()));
        //fill support if necessary
        shouldFillSupport(holder, mIds.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CommentaryActivity.class).putExtra(CommentaryActivity.REQUEST_ID ,request.getId()));
            }
        });
        holder.mSupportsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position), mLocks.get(position));
            }
        });
        holder.mNumberOfSupportsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreClick(holder, mIds.get(position), mLocks.get(position));
            }
        });


    }
    @Override
    public int getItemCount() {
        return mRequests.size();
    }
    //TODO TO FINISH
    private void fillUser(final RequestViewHolder holder, DocumentReference userRef){
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(RequestAdapter.class.getSimpleName(), documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    holder.mUserName.setText(user.getName());
                    ProfilePhotoHelper.loadImage(context, holder.mUserAvatar, user.getPhotoUrl());
                    //Log.d(RequestAdapter.class.getSimpleName(), user.getPhotoUrl());
                }
            }
        });
    }
    private void shouldFillSupport(final RequestViewHolder holder, String requestId){
        //if exists support, then should be IV filled
        AssociationHelper.getRequestSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", requestId, FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                            holder.mSupportsIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_support_filled));
                        else
                            holder.mSupportsIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_support_borderline));
                    }
                });
    }
    private void scoreClick(final RequestViewHolder holder, final String requestId, final SupportMutex mutex){

        mutex.lock();
            AssociationHelper.getRequestSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", requestId, FirebaseAuth.getInstance().getUid())
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            //if support already exists, should delete it
                            if (documentSnapshot.exists()) {
                                AssociationHelper.removeRequestSupport(FirebaseFirestore.getInstance(),
                                        "gVw7dUkuw3SSZSYRXe8s", requestId, FirebaseAuth.getInstance().getUid())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mutex.unlock();
                                    }
                                });
                            } else {// else should add it
                                DocumentReference userRef = DocReferences.getUserRef();
                                DocumentReference assocRef = DocReferences.getAssociationRef("gVw7dUkuw3SSZSYRXe8s");
                                String supportId = FirebaseAuth.getInstance().getUid();
                                //TODO review refs

                                AssociationSupport support = new AssociationSupport(supportId, Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis(),
                                        userRef, null, assocRef, null);
                                AssociationHelper.setRequestSupport(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s"
                                        , requestId, supportId, support)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mutex.unlock();
                                    }
                                });
                            }
                            RequestAdapter.this.notifyDataSetChanged();

                        }
                    });
    }

    /**
     * Class to serve as ViewHolder for a Request model in this adapter
     */
    public class RequestViewHolder extends RecyclerView.ViewHolder{

        final View mView;

        final ImageView mUserAvatar;
        final ImageView mRequestCategory;
        final ImageView mSupportsIV;
        final ImageView mCommentsIV;

        final TextView mUserName;
        final TextView mUserLeaderboardPostion;
        final TextView mRequestTitle;
        final TextView mRequestCost;
        final TextView mRequestContent;
        final TextView mNumberOfCommentsTV;
        final TextView mNumberOfSupportsTV;

        final CardView nCard;
        Request mItem;

        RequestViewHolder(View view){
            super(view);

            mView = view;
            //ImageViews
            mUserAvatar = view.findViewById(R.id.request_author_photo_iv);
            mCommentsIV = view.findViewById(R.id.commentsIV);
            mSupportsIV = view.findViewById(R.id.supportsIV);
            mRequestCategory = view.findViewById(R.id.request_budget_category_iv);

            mUserName = view.findViewById(R.id.request_author_name_tv);
            mUserLeaderboardPostion = view.findViewById(R.id.request_leaderboard_position_tv);
            mRequestTitle = view.findViewById(R.id.request_title_tv);
            mRequestCost = view.findViewById(R.id.request_cost_tv);
            mRequestContent = view.findViewById(R.id.request_content_tv);
            mNumberOfCommentsTV = view.findViewById(R.id.request_number_of_comments_tv);
            mNumberOfSupportsTV = view.findViewById(R.id.supportsTV);

            nCard = view.findViewById(R.id.requestCV);

        }

        @Override
        public String toString(){
            return "";
        }
    }

}
