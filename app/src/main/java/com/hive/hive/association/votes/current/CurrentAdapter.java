package com.hive.hive.association.votes.current;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Session;
import com.hive.hive.utils.TimeUtils;
import com.hive.hive.utils.VotingUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentAdapter extends RecyclerView.Adapter<AgendasViewHolder> {
    private String TAG = CurrentAdapter.class.getSimpleName();
    //-- Data
    private Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas; //first its agenda ids, second its a map <agendaid, agenda>
    private HashMap<String, Integer> mAgendaScore;
    public static String mCurrentAgendaId;
    private Pair<String, Session> mCurrentSession;

    //-- Timer
    ArrayList<CountDownTimer> mTimers;
    CountDownTimer mUnfoldableTimer;
    // Local for now
    private HashMap<String, String> mIconsDrawablePaths;
    private HashMap<String, Integer> mIconsDrawable;

    //-- Views
    private UnfoldableView mUnfoldableView;
    private FrameLayout mDetailsLayout;
    private View mView;

    private Context mContext;

    private CurrentFragment mFragment;

    public CurrentAdapter(Context context, CurrentFragment fragment, Pair<String, Session> session, Pair<ArrayList<String>, HashMap<String, Agenda>> agendas,
                          HashMap<String, Integer> agendaScore,
                          UnfoldableView unfoldableView, FrameLayout detailsLayout, View view) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mCurrentSession = session;
        this.mAgendas = agendas;
        this.mAgendaScore = agendaScore;
        this.mUnfoldableView = unfoldableView;
        this.mDetailsLayout = detailsLayout;
        this.mView = view;
        this.mTimers = new ArrayList<>();
        mIconsDrawable = new HashMap<>();
        mIconsDrawablePaths = new HashMap<>();

    }

    public CountDownTimer getmUnfoldableTimer() {
        return mUnfoldableTimer;
    }

    public void setmCurrentSession(Pair<String, Session> mCurrentSession) {
        this.mCurrentSession = mCurrentSession;
    }

    @Override
    public AgendasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_cell, parent, false);

        // Init locally
        VotingUtils.initPossibleCategoryIcons(mContext, mIconsDrawablePaths, mIconsDrawable);

        return new AgendasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AgendasViewHolder holder, int position) {
        //get current agenda
        final String[] agendaId = {mAgendas.first.get(position)};

        final Agenda agenda = mAgendas.second.get(agendaId[0]);

        //populate views
        holder.getmTitle().setText(agenda.getTitle());
        //TODO:Change this line to get from server
        holder.getmCategoryIcon().setImageResource(VotingUtils.getDrawable("services", mIconsDrawable));

        //sets Agenda Score
        if (mAgendaScore != null && mAgendas.first.get(position) != null && mAgendaScore.get(mAgendas.first.get(position)) != null)
            holder.getmRequestScore().setText(mAgendaScore.get(mAgendas.first.get(position)).toString());
        else {
            holder.getmRequestScore().setVisibility(View.GONE);
            holder.getmRequestScoreIV().setVisibility(View.GONE);
        }

        //TODO USE RETURN FROM CLOCK TO STOP SHIT
        //loads agenda remaining time
        mTimers.add(TimeUtils.clock(holder.getmTime(), mCurrentSession.second, mContext));


        holder.getmAgendaCV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFragment.clearQuestions();        //solves questions mExpandableQuestionsAdapter bug


                agendaId[0] = mAgendas.first.get(position);

                changeUnfoldableContent(agenda, agendaId[0]);

                mUnfoldableView.unfold(view, mDetailsLayout);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mAgendas.first.size();
    }

    private void changeUnfoldableContent(Agenda agenda, String agendaId) {
        //load views
        TextView titleTV = mView.findViewById(R.id.expandable_titleContentTV);
        TextView descriptionTV = mView.findViewById(R.id.expandable_contentTV);
        TextView timeTV = mView.findViewById(R.id.expandable_timerTV);
        TextView requestScoreTV = mView.findViewById(R.id.expandable_supportTV);
        ImageView requestScoreIV = mView.findViewById(R.id.expandable_supportIV);
        //set agenda texts
        titleTV.setText(agenda.getTitle());
        descriptionTV.setText(agenda.getContent());
        if (mAgendaScore.get(agendaId) != null)
            requestScoreTV.setText(mAgendaScore.get(agendaId).toString());
        else{
            requestScoreTV.setVisibility(View.GONE);
            requestScoreIV.setVisibility(View.GONE);
        }
        VotingUtils.fillUnfoldableUser(agenda.getSuggestedByRef(), mView, mContext);

        //sets time
        mUnfoldableTimer = TimeUtils.clock(timeTV, mCurrentSession.second, mContext);

        //sets the current agenda, ExpandableListAdapter depends on it
        mCurrentAgendaId = agendaId;

        mFragment.changeUnfoldableQuestionsContent(agendaId);


    }

}
