package com.hive.hive.association.request;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.request.comments.CommentsActivity;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private String TAG = RequestAdapter.class.getSimpleName();

    private int MAX_TITLE_SIZE = 47;
    private int MAX_CONTENT_SIZE = 147;

    //-- Data
    ArrayList<Request> requests;
    private ArrayList<SupportMutex> mLocks;
    private Context context;

    //--- Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mAssociationID = "gVw7dUkuw3SSZSYRXe8s";

    public RequestAdapter(
            ArrayList<Request> requests, Context context, int max_title, int max_content
    ) {
        this.requests = requests;
        this.context = context;
        this.mLocks = new ArrayList<>();
        MAX_TITLE_SIZE = max_title - 3;
        MAX_CONTENT_SIZE = max_title - 3;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        try {
            if (mLocks.get(position) == null)
                mLocks.add(new SupportMutex(holder.mNumberOfSupportsTV, holder.mSupportsIV));
        } catch (java.lang.IndexOutOfBoundsException e) {
            mLocks.add(new SupportMutex(holder.mNumberOfSupportsTV, holder.mSupportsIV));
        }

        final Request request = requests.get(position);

        holder.mItem = request;

        // fill user
        fillUser(holder, request.getAuthorRef());

        // fill request
        holder.mRequestTitle.setText(request.getTitle());
        holder.mRequestContent.setText(request.getContent());
        holder.mNumberOfSupportsTV.setText(String.valueOf(request.getScore()));
        holder.mNumberOfCommentsTV.setText(String.valueOf(request.getNumComments()));

        // fill support if necessary
        shouldFillSupport(holder, getRequestID(position));

        holder.mView.setOnClickListener(view ->
                context.startActivity(
                        new Intent(context, CommentsActivity.class)
                                .putExtra(CommentsActivity.REQUEST_ID ,request.getId())
                )
        );

        holder.mSupportsIV.setOnClickListener(createToggleSupportOnClickListener(position));
        holder.mNumberOfSupportsTV.setOnClickListener(createToggleSupportOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    private View.OnClickListener createToggleSupportOnClickListener(int position) {
        return v -> {
            // Lock the mutex as soon as the user clicks
            SupportMutex mutex = mLocks.get(position);
            mutex.lock();

            getRequestSupportAndCallSupportActionHandler(
                    position,
                    getRequestID(position),
                    mutex
            );
        };
    }

    // TODO: FINISH
    private void fillUser(final RequestViewHolder holder, DocumentReference userRef){
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, documentSnapshot.get("name").toString());
                User user = documentSnapshot.toObject(User.class);
                holder.mUserName.setText(user.getName());
                ProfilePhotoHelper.loadImage(context, holder.mUserAvatar, user.getPhotoUrl());
            }
        });
    }

    private String getRequestID(int requestPosition) {
        return requests.get(requestPosition).getId();
    }

    private void shouldFillSupport(final RequestViewHolder holder, String requestId){
        //if exists support, then should be IV filled
        AssociationHelper.getRequestSupport(
                mDB,
                mAssociationID,
                requestId,
                mUser.getUid()
        )
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists())
                        holder.mSupportsIV.setImageDrawable(
                                context.getResources().getDrawable(R.drawable.ic_support_filled)
                        );
                    else
                        holder.mSupportsIV.setImageDrawable(
                                context.getResources().getDrawable(R.drawable.ic_support_borderline)
                        );
                });
    }

    private void getRequestSupportAndCallSupportActionHandler(
            int requestPosition,
            String requestID,
            final SupportMutex mutex
    ) {
        AssociationHelper.getRequestSupport(
                mDB,
                mAssociationID,
                requestID,
                mUser.getUid()
        )
                .addOnSuccessListener(
                        documentSnapshot -> supportActionHandler(
                                requestPosition,
                                requestID,
                                documentSnapshot,
                                mutex
                        )
                );
    }

    private void supportActionHandler(
            int requestPosition,
            String requestID,
            DocumentSnapshot supportSnap,
            final SupportMutex mutex
    ) {
        Request request = requests.get(requestPosition);
        // Toggle request support
        if (supportSnap.exists()) {
            // Remove support
            AssociationHelper.removeRequestSupport(
                    mDB,
                    mAssociationID,
                    requestID,
                    supportSnap.getId()
            )
                    .addOnCompleteListener(task -> mutex.unlock())
                    .addOnFailureListener(e -> Log.e(TAG, e.toString()))
                    .addOnSuccessListener(aVoid -> {
                        request.decrementScore();
                        RequestAdapter.this.notifyDataSetChanged();
                    });
        } else {
            // Create and save support
            DocumentReference userRef = DocReferences.getUserRef();
            DocumentReference assocRef = DocReferences.getAssociationRef(mAssociationID);
            String supportId = FirebaseAuth.getInstance().getUid();

            // TODO: Add missing refs
            Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
            AssociationSupport support = new AssociationSupport(
                    supportId,
                    currentTimeInMillis,
                    currentTimeInMillis,
                    userRef,
                    null,
                    assocRef,
                    null
            );

            AssociationHelper.setRequestSupport(
                    FirebaseFirestore.getInstance(),
                    mAssociationID,
                    requestID,
                    supportId,
                    support
            )
                    .addOnCompleteListener(task -> mutex.unlock())
                    .addOnFailureListener(e ->Log.e(TAG, e.toString()))
                    .addOnSuccessListener(aVoid -> {
                        request.incrementScore();
                        RequestAdapter.this.notifyDataSetChanged();
                    });
        }
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
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
        final TextView mUserLeaderboardPosition;
        final TextView mRequestTitle;
        final TextView mRequestCost;
        final TextView mRequestContent;
        final TextView mNumberOfCommentsTV;
        final TextView mNumberOfSupportsTV;

        final CardView mCard;

        Request mItem;

        RequestViewHolder (View view) {
            super(view);

            mView = view;

            //ImageViews
            mUserAvatar = view.findViewById(R.id.request_author_photo_iv);
            mCommentsIV = view.findViewById(R.id.commentsIV);
            mSupportsIV = view.findViewById(R.id.supportsIV);
            mRequestCategory = view.findViewById(R.id.request_budget_category_iv);

            mUserName = view.findViewById(R.id.request_author_name_tv);
            mUserLeaderboardPosition = view.findViewById(R.id.request_leaderboard_position_tv);
            mRequestTitle = view.findViewById(R.id.request_title_tv);
            mRequestCost = view.findViewById(R.id.request_cost_tv);
            mRequestContent = view.findViewById(R.id.request_content_tv);
            mNumberOfCommentsTV = view.findViewById(R.id.request_number_of_comments_tv);
            mNumberOfSupportsTV = view.findViewById(R.id.supportsTV);

            mCard = view.findViewById(R.id.requestCV);

        }

        @Override
        public String toString(){
            return "";
        }
    }

}
