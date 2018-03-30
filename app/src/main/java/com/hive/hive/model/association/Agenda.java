package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

public class Agenda {

    //--- SubCollections
    public static String QUESTIONS_COLLECTION = "questions";
    public static String VOTES_COLLECTION = "votes";

    private String title;
    private String content;
    private int questionsNum;
    private DocumentReference sessionRef;
    private DocumentReference requestRef;
    private DocumentReference forumPostRef;
    private DocumentReference suggestedByRef;
    private AgendaStatus status;

    //--- Constructors

    public Agenda(
            String title,
            String content,
            int questionsNum,
            DocumentReference sessionRef,
            DocumentReference requestRef,
            DocumentReference forumPostRef,
            DocumentReference suggestedByRef,
            AgendaStatus status
    ) {
        this.title = title;
        this.content = content;
        this.questionsNum = questionsNum;
        this.sessionRef = sessionRef;
        this.requestRef = requestRef;
        this.forumPostRef = forumPostRef;
        this.suggestedByRef = suggestedByRef;
        this.status = status;
    }

    public Agenda() {}

    //--- Getters


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public DocumentReference getSessionRef() {
        return sessionRef;
    }

    public DocumentReference getRequestRef() {
        return requestRef;
    }

    public DocumentReference getForumPostRef() {
        return forumPostRef;
    }

    public DocumentReference getSuggestedByRef() { return suggestedByRef; }

    public AgendaStatus getStatus() {
        return status;
    }

    public int getQuestionsNum() { return questionsNum; }

    //--- Setters


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSessionRef(DocumentReference sessionRef) {
        this.sessionRef = sessionRef;
    }

    public void setRequestRef(DocumentReference requestRef) {
        this.requestRef = requestRef;
    }

    public void setForumPostRef(DocumentReference forumPostRef) { this.forumPostRef = forumPostRef; }

    public void setSuggestedByRef(DocumentReference suggestedByRef) { this.suggestedByRef = suggestedByRef; }

    public void setStatus(AgendaStatus status) {
        this.status = status;
    }

    public void setQuestionsNum(int questionsNum) { this.questionsNum = questionsNum; }
}
