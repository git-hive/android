package com.hive.hive.model.forum;

import com.hive.hive.model.marketplace.PointsTransaction;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumComment extends ForumAction {

    private String content;
    private int supportScore;

    private String postId;

    private ArrayList<String> supportIds;

    //--- Constructor

    public ForumComment(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String forumId, String content, int supportScore, String postId, ArrayList<String> supportIds)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, forumId);
        this.content = content;
        this.supportScore = supportScore;
        this.postId = postId;
        this.supportIds = supportIds;
    }


    //--- Getters

    public String getContent() {
        return content;
    }

    public String getPostId() {
        return postId;
    }

    public int getSupportScore() {


        return supportScore;
    }

    public ArrayList<String> getSupportIds() {
        return supportIds;
    }

    //--- Setters

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setSupportScore(int supportScore) {
        this.supportScore = supportScore;
    }

    public void setSupportIds(ArrayList<String> supportIds) {
        this.supportIds = supportIds;
    }
}
