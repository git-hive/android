package com.hive.hive.association.votes.future_and_past.old;

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
import com.hive.hive.association.votes.future_and_past.FutureAndPastAgendasRVAdapter;
import com.hive.hive.association.votes.future_and_past.FutureAndPastQuestionsExpandableAdapter;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Question;
import com.hive.hive.utils.unfoldable.UnfoldableSharedMethods;

import java.util.ArrayList;
import java.util.HashMap;

// In this case, the fragment displays simple text based on the page
public class OldFragment extends Fragment {

    public static final String ARG_PAGE = "Passadas";
    private final static String TAG = OldFragment.class.getSimpleName();
    //Agendas
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendasPair;// arraylist of agendaIds and mapping of agenda into agendaID
    private HashMap<String, String> mScoreMap; //maps a requestScore into a agendaId
    //Recycler Things
    private RecyclerView mRV;
    private FutureAndPastAgendasRVAdapter mRVAdapter;

    //Views
    private View mView;
    private View mListTouchInterceptor;
    private FrameLayout mDetailsLayout;
    private UnfoldableView mUnfoldableView;
    private ScrollView detailsScrollView;

    // Expandable List View
    public static ExpandableListView expandableListView;
    public static FutureAndPastQuestionsExpandableAdapter mExpandableQuestionsAdapter;

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

        UnfoldableSharedMethods.unfoldableListener(mUnfoldableView, mListTouchInterceptor, mDetailsLayout);

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

    private void initRecycler() {
        mView.findViewById(R.id.agendasPB).setVisibility(View.GONE);

        mRVAdapter = new FutureAndPastAgendasRVAdapter(mAgendasPair, mScoreMap, this.getContext().getApplicationContext(), this, mUnfoldableView, mDetailsLayout, mView);
        mRV = mView.findViewById(R.id.cellRV);
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setAdapter(mRVAdapter);

    }

    public void setAgendas(Pair<ArrayList<String>, HashMap<String, Agenda>> agendasPair, HashMap<String, String> scoreMap) {
        mAgendasPair = agendasPair;
        mScoreMap = scoreMap;
        initRecycler();
    }
    public void updateAgendas(){
        mRVAdapter.notifyDataSetChanged();
    }
    public void updateQuestionsUI(ArrayList<Pair<String, Question>> questions){
        mExpandableQuestionsAdapter = new FutureAndPastQuestionsExpandableAdapter(this.getContext(), questions, false);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(mExpandableQuestionsAdapter);

        UnfoldableSharedMethods.unfoldableMagic(expandableListView, mExpandableQuestionsAdapter);
    }

}