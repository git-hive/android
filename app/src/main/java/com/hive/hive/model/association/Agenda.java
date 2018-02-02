package com.hive.hive.model.association;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Agenda {

    private String id;
    private String title;
    private String content;

    private String sessionId;
    private String requisitionId;   //May be null
    private String forumPostId;     //May be null
    private AgendaStatus status;

    private HashMap<String, Question> questions;

    //--- Constructors

    public Agenda(
            String id, String title, String content, String sessionId, String requisitionId,
            String forumPostId, AgendaStatus status, HashMap<String, Question> questions)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sessionId = sessionId;
        this.requisitionId = requisitionId;
        this.forumPostId = forumPostId;
        this.status = status;
        this.questions = questions;
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

    public String getForumPostId() {
        return forumPostId;
    }

    public AgendaStatus getStatus() {
        return status;
    }

    public HashMap<String, Question> getQuestions() {
        return questions;
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

    public void setForumPostId(String forumPostId) {
        this.forumPostId = forumPostId;
    }

    public void setStatus(AgendaStatus status) {
        this.status = status;
    }

    public void setQuestions(HashMap<String, Question> questions) {
        this.questions = questions;
    }
}
