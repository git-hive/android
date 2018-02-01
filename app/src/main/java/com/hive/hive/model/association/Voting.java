package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Voting {

    private String id;
    private String agendaId;
    private String question;

    private ArrayList<VotingOption> options;

    //--- Constructor

    public Voting(String id, String agendaId, String question, ArrayList<VotingOption> options) {
        this.id = id;
        this.agendaId = agendaId;
        this.question = question;
        this.options = options;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<VotingOption> getOptions() {
        return options;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(ArrayList<VotingOption> options) {
        this.options = options;
    }
}
