package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

public class Question {

    private String id;
    private String question;
    private String info;
    private HashMap<String, QuestionOptions> options;
    private String winningOption;

    private DocumentReference sessionRef;
    private DocumentReference agendaRef;

    //--- Constructor

    public Question(
            String id,
            String question,
            String info,
            HashMap<String, QuestionOptions> options,
            String winningOption,
            DocumentReference sessionRef,
            DocumentReference agendaRef
    ) {
        this.id = id;
        this.question = question;
        this.info = info;
        this.options = options;
        this.winningOption = winningOption;
        this.sessionRef = sessionRef;
        this.agendaRef = agendaRef;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getInfo() {
        return info;
    }

    public HashMap<String, QuestionOptions> getOptions() {
        return options;
    }

    public String getWinningOption() {
        return winningOption;
    }

    public DocumentReference getSessionRef() {
        return sessionRef;
    }

    public DocumentReference getAgendaRef() {
        return agendaRef;
    }


    //--- Setters
    
    public void setId(String id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setOptions(HashMap<String, QuestionOptions> options) {
        this.options = options;
    }

    public void setWinningOption(String winningOption) {
        this.winningOption = winningOption;
    }

    public void setSessionRef(DocumentReference sessionRef) {
        this.sessionRef = sessionRef;
    }

    public void setAgendaRef(DocumentReference agendaRef) {
        this.agendaRef = agendaRef;
    }
}
