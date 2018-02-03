package com.hive.hive.model.association;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Request extends AssociationAction {

    private String title;
    private String content;
    private int supportScore;

    private HashMap<String, RequestCategory> categories;
    private HashMap<String, AssociationComment> comments;
    private HashMap<String, AssociationSupport> supports;


    //--- Constructor


    public Request() {
    }

    public Request(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String associationId, String title, String content, int supportScore,
            HashMap<String, RequestCategory> categories, HashMap<String, AssociationComment> comments,
            HashMap<String, AssociationSupport> supports)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.title = title;
        this.content = content;
        this.supportScore = supportScore;
        this.categories = categories;
        this.comments = comments;
        this.supports = supports;
    }

    //--- Getters

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getSupportScore() {
        return supportScore;
    }

    public HashMap<String, RequestCategory> getCategories() {
        return categories;
    }

    public HashMap<String, AssociationComment> getComments() {
        return comments;
    }

    public HashMap<String, AssociationSupport> getSupports() {
        return supports;
    }

    //--- Setters


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSupportScore(int supportScore) {
        this.supportScore = supportScore;
    }

    public void setCategories(HashMap<String, RequestCategory> categories) {
        this.categories = categories;
    }

    public void setComments(HashMap<String, AssociationComment> comments) {
        this.comments = comments;
    }

    public void setSupports(HashMap<String, AssociationSupport> supports) {
        this.supports = supports;
    }
}
