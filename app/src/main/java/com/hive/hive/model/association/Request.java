package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

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
    private int numComments;

    private DocumentReference categoryRef;
    private RequestCategory category;

//--- Constructors

    public Request() {}


    public Request(
            String id,
            long createdAt,
            long updatedAt,
            DocumentReference authorRef,
            DocumentReference pointsTransactionRef,
            DocumentReference associationRef,
            String title,
            String content,
            int score,
            int numComments,
            RequestCategory category
    ) {
        super(id, createdAt, updatedAt, authorRef, pointsTransactionRef, associationRef);
        this.title = title;
        this.content = content;
        this.score = score;
        this.numComments = numComments;
        this.category = category;
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

    public int getNumComments() { return numComments;}

    public DocumentReference getCategoryRef() {
        return categoryRef;
    }

    public RequestCategory getCategory() {
        return category;
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

    public void incrementScore() {
        this.setScore(this.score + 1);
    }

    public void decrementScore() {
        this.setScore(this.score - 1);
    }

    public void setNumComments(int numComments) { this.numComments = numComments; }

    public void setCategoryRef(DocumentReference categoryRef) {
        this.categoryRef = categoryRef;
    }

    public void setCategory(RequestCategory category) {
        this.category = category;
    }

}
