package com.hive.hive.model.forum;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumPost extends ForumAction {

    private String title;
    private String content;
    private int supportScore;

    private HashMap<String, ForumCategory> categories;
    private HashMap<String, ForumComment> comments;
    private HashMap<String, ForumSupport> supports;

    //--- Constructors


    public ForumPost() {
    }

    public ForumPost(
            long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId,
            String forumId, String title, String content, int supportScore,
            HashMap<String, ForumCategory> categories, HashMap<String, ForumComment> comments,
            HashMap<String, ForumSupport> supports)
    {
        super(createdAt, updatedAt, authorId, pointsTransactionId, forumId);
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

    public HashMap<String, ForumCategory> getCategories() {
        return categories;
    }

    public HashMap<String, ForumComment> getComments() {
        return comments;
    }

    public HashMap<String, ForumSupport> getSupports() {
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

    public void setCategories(HashMap<String, ForumCategory> categories) {
        this.categories = categories;
    }

    public void setComments(HashMap<String, ForumComment> comments) {
        this.comments = comments;
    }

    public void setSupports(HashMap<String, ForumSupport> supports) {
        this.supports = supports;
    }
}
