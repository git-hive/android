package com.hive.hive.association.request;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.association.request.comments.CommentsActivity;
import com.hive.hive.model.association.AssociationSupport;
import com.hive.hive.model.association.BudgetTransactionCategories;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.SupportMutex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private String TAG = RequestAdapter.class.getSimpleName();

    //-- Data
    private Pair<ArrayList<String>, HashMap<String, Request>> requests;
    private HashMap<Integer, Boolean> requestsSupport;

    private SupportMutex lock ;
    private ArrayList<String> changedSupportsRequestsIds; //requestId, supportId
    private HashMap<String, Boolean> changedSupports; //requestId, supportId

    private Context context;
    private HashMap<String, Integer> budgetCategoryNameResource;

    private HashMap<DocumentReference, String> usernames;
    private HashMap<DocumentReference, String> userProfilePictures;

    //--- Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mAssociationID = "gVw7dUkuw3SSZSYRXe8s";

    public RequestAdapter(
            Pair<ArrayList<String>, HashMap<String, Request>> requests,
            Context context
    ) {
        this.requests = requests;
        this.requestsSupport = new HashMap<>();

        this.changedSupportsRequestsIds = new ArrayList<>();
        this.changedSupports = new HashMap<>();

        this.usernames = new HashMap<>();
        this.userProfilePictures = new HashMap<>();
        this.context = context;

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

        this.lock = new SupportMutex(holder.mNumberOfSupportsTV, holder.mSupportsIV);

        String requestId = requests.first.get(position);

        Request request = requests.second.get(requestId);

        holder.mItem = request;

        // fill user
        fillUser(holder, request.getAuthorRef());

        // fill request
        holder.mRequestTitle.setText(request.getTitle());
        holder.mRequestContent.setText(request.getContent());
        holder.mNumberOfSupportsTV.setText(String.valueOf(request.getScore()));
        holder.mNumberOfCommentsTV.setText(String.valueOf(request.getNumComments()));

        String budgetCategoryName = request.getBudgetCategoryName();
        if (budgetCategoryNameResource.containsKey(budgetCategoryName)) {
            holder
                    .mRequestCategory
                    .setImageResource(budgetCategoryNameResource.get(budgetCategoryName));
        }

        // fill support if necessary
        shouldFillSupport(holder, getRequestID(position), position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                sendToFirebase();
                                                context.startActivity(
                                                        new Intent(context, CommentsActivity.class)
                                                                .putExtra(CommentsActivity.REQUEST_ID, getRequestID(position))
                                                );
                                            }
                                        }
        );

        holder.mSupportsIV
                .setOnClickListener(createToggleSupportOnClickListener(position, holder));
        holder.mNumberOfSupportsTV
                .setOnClickListener(createToggleSupportOnClickListener(position, holder));
    }

    @Override
    public int getItemCount() {
        if (requests != null)
            return requests.first.size();
        return 0;
    }

    private View.OnClickListener createToggleSupportOnClickListener(
            int position,
            final RequestViewHolder holder
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleRequestSupport(position, holder.mNumberOfSupportsTV, holder.mSupportsIV);

                // Lock the mutex as soon as the user clicks
//                getRequestSupportAndCallSupportActionHandler(
//                        getRequestID(position));
            }
        };
    }

    private void fillUser(final RequestViewHolder holder, DocumentReference userRef) {
        // Check if it has on memory
        if (usernames.containsKey(userRef) && userProfilePictures.containsKey(userRef)) {
            String username = usernames.get(userRef);
            String userPhoto = userProfilePictures.get(userRef);

            holder.mUserName.setText(username);
            ProfilePhotoHelper.loadImage(context, holder.mUserAvatar, userPhoto);

            return;
        }

        // If it doesn't, fetch it
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, documentSnapshot.get("name").toString());
                    User user = documentSnapshot.toObject(User.class);
                    String username = user.getName();
                    String userPhoto = user.getPhotoUrl();

                    // Save data to local "cache"
                    usernames.put(userRef, username);
                    userProfilePictures.put(userRef, userPhoto);

                    holder.mUserName.setText(username);
                    ProfilePhotoHelper.loadImage(context, holder.mUserAvatar, userPhoto);
                }
            }
        });
    }

    private String getRequestID(int requestPosition) {
        return requests.first.get(requestPosition);
    }

    private void shouldFillSupport(
            final RequestViewHolder holder,
            String requestId,
            final int position
    ) {
        // Check if it has on memory
        if (requestsSupport.containsKey(position)) {
            if (requestsSupport.get(position)) {
                holder.mSupportsIV.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.ic_support_filled)
                );
            } else {
                holder.mSupportsIV.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.ic_support_borderline)
                );
            }
            return;
        }
        // If it doesn't, fetch it

        AssociationHelper.getRequestSupport(
                mDB,
                mAssociationID,
                requestId,
                mUser.getUid()
        )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            requestsSupport.put(position, true);
                            holder.mSupportsIV.setImageDrawable(
                                    context.getResources().getDrawable(R.drawable.ic_support_filled)
                            );
                        } else {
                            requestsSupport.put(position, false);
                            holder.mSupportsIV.setImageDrawable(
                                    context.getResources().getDrawable(R.drawable.ic_support_borderline)
                            );
                        }
                    }
                });
    }

    public void sendToFirebase() {
        lock.lock();
        for (String changedSupportRequestId : changedSupportsRequestsIds) {
            supportActionHandler(changedSupportRequestId, changedSupports.get(changedSupportRequestId));
        }
        changedSupports.clear();
        changedSupportsRequestsIds.clear();
        lock.unlock();
    }

    private void supportActionHandler(
            String requestID,
            Boolean addScore) {
        // Toggle request support
        if (!addScore) {
            // Remove support
            AssociationHelper.removeRequestSupport(
                    mDB,
                    mAssociationID,
                    requestID,
                    FirebaseAuth.getInstance().getUid()
            )
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    });
        } else {
            // Create and save support
            DocumentReference userRef = DocReferences.getUserRef();
            DocumentReference assocRef = DocReferences.getAssociationRef(mAssociationID);
            String supportId = FirebaseAuth.getInstance().getUid();

            // TODO: Add missing refs
            Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
            AssociationSupport support = new AssociationSupport(
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
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    });
        }
    }

    private void toggleRequestSupport(
            int position,
            TextView numberOfSupportsTV,
            ImageView supportIV
    ) {
        Drawable filledSupportIC = context
                .getResources()
                .getDrawable(R.drawable.ic_support_filled);

        Drawable borderlineSupportIC = context
                .getResources()
                .getDrawable(R.drawable.ic_support_borderline);

        String requestId = requests.first.get(position);
        Request request = this.requests.second.get(requestId);
        if (requestsSupport.get(position)) {
            supportIV.setImageDrawable(borderlineSupportIC);
            request.decrementScore();
            if (changedSupportsRequestsIds.contains(requestId)) {
                changedSupports.remove(requestId);
                changedSupportsRequestsIds.remove(requestId);
            } else {
                changedSupports.put(requestId, false);
                changedSupportsRequestsIds.add(requestId);
            }
        } else {
            supportIV.setImageDrawable(filledSupportIC);
            request.incrementScore();
            if(changedSupportsRequestsIds.contains(requestId)) {
                changedSupports.remove(requestId);
                changedSupportsRequestsIds.remove(requestId);
            }else{
                changedSupports.put(requestId, true);
                changedSupportsRequestsIds.add(requestId);
            }
        }
        numberOfSupportsTV.setText(String.valueOf(request.getScore()));
        requestsSupport.put(position, !requestsSupport.get(position));

    }

    public void setData(Pair<ArrayList<String> , HashMap<String, Request>> requests) {
        sendToFirebase();
        this.requests = requests;
    }

    /**
     * Class to serve as ViewHolder for a Request model in this mExpandableQuestionsAdapter
     */
    public class RequestViewHolder extends RecyclerView.ViewHolder {

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

        RequestViewHolder(View view) {
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
        public String toString() {
            return "";
        }
    }

}
