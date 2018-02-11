package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
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

    private ArrayList<DocumentReference> categories;

    //--- Constructors

    public Request() {}

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
            ArrayList<DocumentReference> categories,
            HashMap<String, AssociationComment> comments,
            HashMap<String, AssociationSupport> supports
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
