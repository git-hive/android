package com.hive.hive.model.forum;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumComment extends ForumAction {

    //--- SubCollections
    public static String SUPPORTS_COLLECTION = "supports";

    //--- Fields
    public static String SCORE_FIELD = "score";

    private String content;
    private int supportScore;

    //--- Constructor

    public ForumComment(
            long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId,
            String content)
    {
        super(createdAt, updatedAt, authorId, pointsTransactionId);
        this.content = content;
        this.supportScore = 0;
    }

    public ForumComment() {
    }

    public void incrementScore(){
        this.supportScore= this.supportScore + 1;
    }

    public void decrementScore(){
        this.supportScore = this.supportScore - 1;
    }
    //--- Getters

    public String getContent() {
        return content;
    }


    public int getSupportScore() {


        return supportScore;
    }

    //--- Setters

    public void setContent(String content) {
        this.content = content;
    }


    public void setSupportScore(int supportScore) {
        this.supportScore = supportScore;
    }

}
