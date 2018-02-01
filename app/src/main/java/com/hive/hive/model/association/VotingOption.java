package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class VotingOption {

    private String id;
    private String title;
    private String contet;
    private int score;

    private ArrayList<String> votesIds;

    //--- Constructor

    public VotingOption(String id, String title, String contet, int score, ArrayList<String> votesIds) {
        this.id = id;
        this.title = title;
        this.contet = contet;
        this.score = score;
        this.votesIds = votesIds;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContet() {
        return contet;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<String> getVotesIds() {
        return votesIds;
    }

    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContet(String contet) {
        this.contet = contet;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setVotesIds(ArrayList<String> votesIds) {
        this.votesIds = votesIds;
    }
}
