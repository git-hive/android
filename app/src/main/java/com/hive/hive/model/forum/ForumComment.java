package com.hive.hive.model.forum;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumComment extends ForumAction {

    private String content;
    private int supportScore;

    private String postId;

    private HashMap<String, ForumSupport> supports;

    //--- Constructor

    public ForumComment(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String forumId, String content, int supportScore, String postId, HashMap<String, ForumSupport> supports)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, forumId);
        this.content = content;
        this.supportScore = supportScore;
        this.postId = postId;
        this.supports = supports;
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

    public HashMap<String, ForumSupport> getSupports() {
        return supports;
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

    public void setSupports(HashMap<String, ForumSupport> supports) {
        this.supports = supports;
    }
}