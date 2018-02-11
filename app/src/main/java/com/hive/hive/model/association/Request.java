package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Request extends AssociationAction {

    //--- SubCollections
    public static String COMMENTS_COLLECTION = "comments";
    public static String SUPPORTS_COLLECTION = "supports";

    //--- Fields
    public static String SCORE_FIELD = "score";

    public static int SUPPORT_ACTION_VALUE = 1;

    private String title;
    private String content;
    private int score;

    private ArrayList<DocumentReference> categories;

    //--- Constructors

    public Request() {}

    public Request(
            String id,
            long createdAt,
            long updatedAt,
            DocumentReference authorId,
            DocumentReference pointsTransactionId,
            DocumentReference associationId,
            String title,
            String content,
            int score,
            ArrayList<DocumentReference> categories
    ) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.title = title;
        this.content = content;
        this.score = score;
        this.categories = categories;
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

    public ArrayList<DocumentReference> getCategories() {
        return categories;
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

    public void setCategories(ArrayList<DocumentReference> categories) {
        this.categories = categories;
    }

}
