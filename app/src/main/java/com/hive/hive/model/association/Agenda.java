package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class Agenda {

    //--- SubCollections
    public static String QUESTIONS_COLLECTION = "questions";

    private String id;
    private String title;
    private String content;

    private DocumentReference sessionRef;
    private DocumentReference requisitionRef;
    private DocumentReference forumPostRef;
    private AgendaStatus status;

    //--- Constructors

    public Agenda(
            String id,
            String title,
            String content,
            DocumentReference sessionRef,
            DocumentReference requisitionRef,
            DocumentReference forumPostRef,
            AgendaStatus status
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sessionRef = sessionRef;
        this.requisitionRef = requisitionRef;
        this.forumPostRef = forumPostRef;
        this.status = status;
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

    public DocumentReference getSessionRef() {
        return sessionRef;
    }

    public DocumentReference getRequisitionRef() {
        return requisitionRef;
    }

    public DocumentReference getForumPostRef() {
        return forumPostRef;
    }

    public AgendaStatus getStatus() {
        return status;
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

    public void setSessionRef(DocumentReference sessionRef) {
        this.sessionRef = sessionRef;
    }

    public void setRequisitionRef(DocumentReference requisitionRef) {
        this.requisitionRef = requisitionRef;
    }

    public void setForumPostRef(DocumentReference forumPostRef) {
        this.forumPostRef = forumPostRef;
    }

    public void setStatus(AgendaStatus status) {
        this.status = status;
    }

}
