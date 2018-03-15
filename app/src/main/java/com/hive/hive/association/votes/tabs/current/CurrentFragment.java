package com.hive.hive.association.votes.tabs.current;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.hive.hive.R;
import com.hive.hive.association.votes.tabs.questions.QuestionForm;
import com.hive.hive.association.votes.tabs.questions.ExpandableListAdapter;
import com.hive.hive.model.association.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // Expandable List View
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;

    // Views references
    ImageButton choseVoteBT;


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
        final View view = inflater.inflate(R.layout.fragment_current, container, false);

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

        choseVoteBT = view.findViewById(R.id.choseVoteBT);

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

        // TODO: Check this expandable element Height, since it has some workarounds, either than set it fixed;
        expandableListView = (ExpandableListView) view.findViewById(R.id.questionExpandableLV);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        setItems();

        // Start Questions activity stuff
        choseVoteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), QuestionForm.class));

            }
        });

        return view;
    }

    // Setting headers and childs to expandable listview
    void setItems(){

        // Array list for header
        ArrayList<String> header = new ArrayList<String>();

        // Array list for child items
        List<String> child1 = new ArrayList<String>();
        List<String> child2 = new ArrayList<String>();
        List<String> child3 = new ArrayList<String>();
        List<String> child4 = new ArrayList<String>();

        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        // Adding headers to list
        for (int i = 1; i < 5; i++) {
            header.add("Question " + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child1.add("Question 1  " + " : Child" + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child2.add("Question 2  " + " : Child" + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child3.add("Question 3  " + " : Child" + i);
        }
        // Adding child data
        for (int i = 1; i < 2; i++) {
            child4.add("Question 4  " + " : Child" + i);
        }

        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);

        adapter = new ExpandableListAdapter(getContext(), header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);

    }
}