package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class AssociationComment extends AssociationAction {

    //--- SubCollections
    public static String SUPPORTS_COLLECTION = "supports";

    //--- Fields
    public static String SCORE_FIELD = "score";

    private String content;
    private int score;

    private DocumentReference requestRef;

    //--- Constructors

    public AssociationComment() {
    }

    public AssociationComment(
            long createdAt,
            long updatedAt,
            DocumentReference authorId,
            DocumentReference pointsTransactionId,
            DocumentReference associationId,
            String content,
            int score,
            DocumentReference requestRef
    ) {
        super(createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.content = content;
        this.score = score;
        this.requestRef = requestRef;
    }

    public void decrementScore(){
        this.score = this.score-1;
    }

    public void incrementScore(){
        this.score = this.score+1;
    }
    //--- Getters

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public DocumentReference getRequestRef() {
        return requestRef;
    }

    //--- Setters

    public void setContent(String content) {
        this.content = content;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRequestRef(DocumentReference requestRef) {
        this.requestRef = requestRef;
    }

}
