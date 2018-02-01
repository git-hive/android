package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Request extends AssociationAction {

    private String title;
    private String content;
    private int supportScore;

    private ArrayList<String> categoriesIds;
    private ArrayList<String> commentsIds;
    private ArrayList<String> supportsIds;


    //--- Constructor


    public Request() {
    }

    public Request(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String associationId, String title, String content, int supportScore,
            ArrayList<String> categoriesIds, ArrayList<String> commentsIds,
            ArrayList<String> supportsIds)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.title = title;
        this.content = content;
        this.supportScore = supportScore;
        this.categoriesIds = categoriesIds;
        this.commentsIds = commentsIds;
        this.supportsIds = supportsIds;
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

    public ArrayList<String> getCategoriesIds() {
        return categoriesIds;
    }

    public ArrayList<String> getCommentsIds() {
        return commentsIds;
    }

    public ArrayList<String> getSupportsIds() {
        return supportsIds;
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

    public void setCategoriesIds(ArrayList<String> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }

    public void setCommentsIds(ArrayList<String> commentsIds) {
        this.commentsIds = commentsIds;
    }

    public void setSupportsIds(ArrayList<String> supportsIds) {
        this.supportsIds = supportsIds;
    }
}
