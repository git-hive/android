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
    private int numComments;


    private HashMap<String, ForumCategory> categories;
    private HashMap<String, ForumComment> comments;

    //--- Constructors


    public ForumPost() {
    }

    public ForumPost(
            long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId,
            String title, String content,
            HashMap<String, ForumCategory> categories, HashMap<String, ForumComment> comments)
    {
        super(createdAt, updatedAt, authorId, pointsTransactionId);
        this.title = title;
        this.content = content;
        this.supportScore = 0;
        this.numComments = 0; //TODO:Verify constructor
        this.categories = categories;
        this.comments = comments;
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

    public int getNumComments() { return numComments;}

    public HashMap<String, ForumCategory> getCategories() {
        return categories;
    }

    public HashMap<String, ForumComment> getComments() {
        return comments;
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

    public void incrementScore() {
        this.setSupportScore(this.supportScore + 1);
    }

    public void decrementScore() {
        this.setSupportScore(this.supportScore - 1);
    }

    public void setNumComments(int numComments) { this.numComments = numComments; }

    public void setCategories(HashMap<String, ForumCategory> categories) {
        this.categories = categories;
    }

    public void setComments(HashMap<String, ForumComment> comments) {
        this.comments = comments;
    }

}
