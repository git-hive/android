package com.hive.hive.home;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.association.votes.AgendasViewHolder;
import com.hive.hive.home.db_files.CurrentAgendasForHomeFirebaseHandle;
import com.hive.hive.model.association.Agenda;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.Session;
import com.hive.hive.model.forum.ForumPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vplentz on 11/01/18.
 */

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final String TAG = this.getClass().getName().toString();
    Pair<ArrayList<String>, HashMap<String, Agenda>> mAgendas;
    private Pair<String, Session> mCurrentSession;


    private List<Object> items;
    private final int REQUEST = 0, ASSOCIATIONPOST = 1;

    public RecyclerViewHomeAdapter(Pair<ArrayList<String>, HashMap<String, Agenda>> agendas, HashMap<String, Integer> mAgendasScores, List<Object> items) {
        this.items = items;
        mAgendas = agendas;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Log.d(TAG, String.valueOf(mAgendas.second.size())+"++++++++++++++++++++++++++++++++++++++++++ THIS IS M GUY");
        populateItems();

        switch (viewHolder.getItemViewType()) {
            case REQUEST:
                RequestViewHolderOld requestViewHolderOld = (RequestViewHolderOld) viewHolder;
                configureViewHolder1(requestViewHolderOld, position);
                break;
            case ASSOCIATIONPOST:
                AgendasViewHolder agendasViewHolder = (AgendasViewHolder) viewHolder;
                configureViewHolder2(agendasViewHolder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(items.get(position) instanceof Request)
            return REQUEST;
        else if(items.get(position) instanceof ForumPost)
            return ASSOCIATIONPOST;
        else if(items.get(position) instanceof Agenda)
            return ASSOCIATIONPOST;
        return -1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Log.d(TAG, String.valueOf(mAgendas.second.size())+"++++++++++++++++++++++++++++++++++++++++++ THIS IS M GUY");

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder =  null;
        switch (viewType) {
            case REQUEST:
                View viewRequisition = inflater.inflate(R.layout.item_request, viewGroup, false);
                viewHolder = new RequestViewHolderOld(viewRequisition);
                break;
            case ASSOCIATIONPOST:
                View viewAPost = inflater.inflate(R.layout.item_association, viewGroup, false);
                viewHolder = new AgendasViewHolder(viewAPost);
                break;
        }
        return viewHolder;
    }

    private void configureViewHolder1(RequestViewHolderOld associationViewHolder, int position) {
        Request request = (Request) items.get(position);
        if (request != null) {
            //vh1.getLabel1().setText("Name: " + user.name);
            //vh1.getLabel2().setText("Hometown: " + user.hometown);
        }
    }

    public void setmCurrentSession(Pair<String, Session> mCurrentSession) {
        this.mCurrentSession = mCurrentSession;
    }

    private void configureViewHolder2(AgendasViewHolder agendasViewHolder, int position) {

        if(mAgendas.first.size() > position) {

            //get current agenda
            final String[] agendaId = {mAgendas.first.get(position)};

            final Agenda agenda = mAgendas.second.get(agendaId[0]);

            agendasViewHolder.getmTitle().setText(agenda.getTitle());
        }

    }

    public void populateItems(){
        if(mAgendas.first.size() == 0)
            return;
        items = new ArrayList<>();
        for (Object key:
                mAgendas.first) {
            items.add(mAgendas.second.get(key));
            Log.d(TAG, String.valueOf(key.toString())+" ITERATING IN HERE");
        }
    }
}
