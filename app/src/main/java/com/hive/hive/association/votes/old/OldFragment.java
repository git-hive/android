package com.hive.hive.association.votes.old;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.hive.hive.R;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import java.util.ArrayList;
import java.util.HashMap;

// In this case, the fragment displays simple text based on the page
public class OldFragment extends Fragment {

    public static final String ARG_PAGE = "Passadas";
    private final static String TAG = OldFragment.class.getSimpleName();
    //Agendas
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendasPair;

    //Recycler Things
    private RecyclerView mRV;
    private OldAgendasRVAdapter mRVAdapter;

    //Views
    private View mView;
    private View mListTouchInterceptor;
    private FrameLayout mDetailsLayout;
    private UnfoldableView mUnfoldableView;
    private ScrollView detailsScrollView;

    // Expandable List View
    public static ExpandableListView expandableListView;
    public static OldQuestionsExpandableAdapter mExpandableQuestionsAdapter;

    // Temporary solution to unfold card, TODO: Check with the @guys
    ImageView mTopClickableCardIV;


    private int mPage;

    public static OldFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        OldFragment fragment = new OldFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_current, container, false);

        initUnfoldable();

        unfoldableListener();

        //TODO change ASSOCIATIONID
        OldAgendasFirebaseHandle.getPastSessions("gVw7dUkuw3SSZSYRXe8s", this);

        return mView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUnfoldable() {

        //removing unnused views
        mView.findViewById(R.id.expandable_choseVoteBT).setVisibility(View.GONE);
        mView.findViewById(R.id.expandable_voteStatusTV).setVisibility(View.GONE);
        mView.findViewById(R.id.expandable_voteTV).setVisibility(View.GONE);
        mView.findViewById(R.id.expandable_statusHeaderTV).setVisibility(View.GONE);

        //setting to right text
        ((TextView) mView.findViewById(R.id.expandable_partialResultsTV)).setText(getText(R.string.final_result));

        // Temporary solution to unfold card, TODO: Check with the @guys
        mTopClickableCardIV = mView.findViewById(R.id.expandable_topCardIV);
        mTopClickableCardIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
                    mUnfoldableView.foldBack();
                }

            }
        });

        mListTouchInterceptor = mView.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        //used to fold and unfold
        mDetailsLayout = mView.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);


        mUnfoldableView = mView.findViewById(R.id.unfoldable_view);

        // Get scroll refence
        detailsScrollView = mView.findViewById(R.id.expandable_cardScroll);

        // Solution by: https://github.com/alexvasilkov/FoldableLayout/issues/38#issuecomment-192814520
        // Allows scroll
        detailsScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUnfoldableView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        expandableListView = mView.findViewById(R.id.expandable_questionExpandableLV);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUnfoldableView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void unfoldableListener() {
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
                unfoldableView.setGesturesEnabled(true);
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
    }

    private void initRecycler() {
        mView.findViewById(R.id.agendasPB).setVisibility(View.GONE);

        mRVAdapter = new OldAgendasRVAdapter(mAgendasPair, this.getContext().getApplicationContext(), this, mUnfoldableView, mDetailsLayout, mView);
        mRV = mView.findViewById(R.id.cellRV);
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setAdapter(mRVAdapter);

    }

    public void updateAgendas(Pair<ArrayList<String>, HashMap<String, Agenda>> agendasPair) {
        mAgendasPair = agendasPair;
        initRecycler();
    }
    public void updateQuestionsUI(ArrayList<Pair<String, Question>> questions){
        mExpandableQuestionsAdapter = new OldQuestionsExpandableAdapter(this.getContext(), questions);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(mExpandableQuestionsAdapter);

        unfoldableMagic();
    }
    private void unfoldableMagic(){
        // THIS MAGIC PEACE OF CODE MAKE THE VIEW WORK AS IT SHOULD
        expandableListView.setDividerHeight(0);

        for (int i = 0; i < mExpandableQuestionsAdapter.getGroupCount(); i++)
            expandableListView.expandGroup(i);
        setListViewHeight(expandableListView);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        for (int i = 0; i < mExpandableQuestionsAdapter.getGroupCount(); i++)
            expandableListView.collapseGroup(i);
        setListViewHeight(expandableListView);
    }

    // Workaround found in: https://thedeveloperworldisyours.com/android/expandable-listview-inside-scrollview/ to ExpandableListView
    // https://stackoverflow.com/questions/17696039/expandablelistview-inside-a-scrollview

    private static void setListViewHeight(ExpandableListView listView) {
        OldQuestionsExpandableAdapter listAdapter = (OldQuestionsExpandableAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupView = listAdapter.getGroupView(i, true, null, listView);
            groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupView.getMeasuredHeight();

            if (listView.isGroupExpanded(i)) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private static void setListViewHeight(ExpandableListView listView,
                                          int group) {
        OldQuestionsExpandableAdapter listAdapter = (OldQuestionsExpandableAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}