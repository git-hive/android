package com.hive.hive.model.association;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Question {

    private String id;
    private String sessionId;
    private String agendaId;
    private String question;

    private HashMap<String, QuestionOptions> options;

    //--- Constructor

    public Question(String id, String sessionId, String agendaId, String question, HashMap<String, QuestionOptions> options) {
        this.id = id;
        this.agendaId = agendaId;
        this.sessionId = sessionId;
        this.question = question;
        this.options = options;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public String getQuestion() {
        return question;
    }

    public HashMap<String, QuestionOptions> getOptions() {
        return options;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(HashMap<String, QuestionOptions> options) {
        this.options = options;
    }
}
