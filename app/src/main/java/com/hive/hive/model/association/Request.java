package com.hive.hive.model.association;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Request extends AssociationAction {

    //--- SubCollections
    public static String COMMENTS_COLLECTION = "comments";
    public static String SUPPORTS_COLLECTION = "supports";

    //--- Fields
    public static String SCORE_FIELD = "score";


    private String title;
    private String content;
    private int score;

    private HashMap<String, RequestCategory> categories;
    private HashMap<String, AssociationComment> comments;
    private HashMap<String, AssociationSupport> supports;

    //--- Constructors

    public Request() {
    }

    public Request(
            String id,
            long createdAt,
            long updatedAt,
            String authorId,
            String pointsTransactionId,
            String associationId,
            String title,
            String content,
            int score,
            HashMap<String, RequestCategory> categories,
            HashMap<String, AssociationComment> comments,
            HashMap<String, AssociationSupport> supports
    ) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.title = title;
        this.content = content;
        this.score = score;
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

    public int getScore() {
        return score;
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

    public void setScore(int score) {
        this.score = score;
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
