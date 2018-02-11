package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class AssociationComment extends AssociationAction {

    //--- SubCollections
    public static String SUPPORTS_COLLECTION = "supports";

    private String content;
    private int supportScore;

    private Request targetRequest;

    private ArrayList<String> supportIds;

    //--- Constructor

    public AssociationComment(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String associationId, String content, int supportScore, Request targetRequest,
            ArrayList<String> supportIds)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.content = content;
        this.supportScore = supportScore;
        this.targetRequest = targetRequest;
        this.supportIds = supportIds;
    }

    //--- Getters

    public String getContent() {
        return content;
    }

    public int getSupportScore() {
        return supportScore;
    }

    public Request getTargetRequest() {
        return targetRequest;
    }

    public ArrayList<String> getSupportIds() {
        return supportIds;
    }

    //--- Setters

    public void setContent(String content) {
        this.content = content;
    }

    public void setSupportScore(int supportScore) {
        this.supportScore = supportScore;
    }

    public void setTargetRequest(Request targetRequest) {
        this.targetRequest = targetRequest;
    }

    public void setSupportIds(ArrayList<String> supportIds) {
        this.supportIds = supportIds;
    }
}
