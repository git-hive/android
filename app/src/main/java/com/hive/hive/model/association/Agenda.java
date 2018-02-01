package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Agenda {

    private String id;
    private String title;
    private String content;

    private String sessionId;
    private String requisitionId;   //May be null
    private AgendaStatus status;

    private ArrayList<Voting> votings;

    //--- Constructors

    public Agenda(
            String id, String title, String content, String sessionId, String requisitionId,
            AgendaStatus status, ArrayList<Voting> votings)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sessionId = sessionId;
        this.requisitionId = requisitionId;
        this.status = status;
        this.votings = votings;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getRequisitionId() {
        return requisitionId;
    }

    public AgendaStatus getStatus() {
        return status;
    }

    public ArrayList<Voting> getVotings() {
        return votings;
    }

    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setRequisitionId(String requisitionId) {
        this.requisitionId = requisitionId;
    }

    public void setStatus(AgendaStatus status) {
        this.status = status;
    }

    public void setVotings(ArrayList<Voting> votings) {
        this.votings = votings;
    }
}
