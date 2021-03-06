package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{

    private String question;
    private String info;
    private ArrayList<QuestionOptions> options;
    private String winningOption;

    private DocumentReference sessionRef;
    private DocumentReference agendaRef;

    //--- Constructor

    public Question(
            String question,
            String info,
            ArrayList<QuestionOptions> options,
            String winningOption,
            DocumentReference sessionRef,
            DocumentReference agendaRef
    ) {
        this.question = question;
        this.info = info;
        this.options = options;
        this.winningOption = winningOption;
        this.sessionRef = sessionRef;
        this.agendaRef = agendaRef;
    }

    public Question() {
    }

    //--- Getters


    public String getQuestion() {
        return question;
    }

    public String getInfo() {
        return info;
    }

    public ArrayList<QuestionOptions> getOptions() {
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
    

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setOptions(ArrayList<QuestionOptions> options) {
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
