package com.hive.hive.association.votes.tabs.current;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hive.hive.R;
import com.hive.hive.model.association.Vote;

import java.util.ArrayList;

// In this case, the fragment displays simple text based on the page
public class CurrentFragment extends Fragment {
    private static final int NUM_LIST_ITEMS= 6;
    public static final String ARG_PAGE = "Passadas";
    private static final String TAG = "#";

    ArrayList<Vote> DUMMYARRAY;
    RecyclerView mRV;
    View mListTouchInterceptor;
    FrameLayout mDetailsLayout;
    UnfoldableView mUnfoldableView;

    // Temporary solution to unfold card, TODO: Check with the @guys
    ImageView mTopClickableCardIV;

    public static CurrentFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CurrentFragment fragment = new CurrentFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current, container, false);

        DUMMYARRAY = new ArrayList<Vote>();
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());
        DUMMYARRAY.add(new Vote());

        // Adding Action Stuff
        DUMMYARRAY.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
            }
        });

        // Temporary solution to unfold card, TODO: Check with the @guys
        mTopClickableCardIV = view.findViewById(R.id.topCardIV);
        mTopClickableCardIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
                    mUnfoldableView.foldBack();
                }

            }
        });

        mListTouchInterceptor = view.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        mDetailsLayout = view.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);

        mUnfoldableView = (UnfoldableView) view.findViewById(R.id.unfoldable_view);

        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                mDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);

                // Check this out to unfold when grab down TODO: @MarcoBirck
                unfoldableView.setGesturesEnabled(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);
            }
        });
        mRV = view.findViewById(R.id.cellRV);

        mRV.setAdapter(new CurrentAdapter(DUMMYARRAY, mUnfoldableView, mDetailsLayout));

        return view;
    }
//    @Override
//    public void onBackPressed() {
//        if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
//            mUnfoldableView.foldBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

}