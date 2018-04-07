package com.hive.hive.model.user;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class UserAction {

    private long createdAt;
    private long updatedAt;
    private DocumentReference authorRef;
    private DocumentReference pointsTransactionRef;



    //--- Constructor


    public UserAction() {}

    public UserAction(
            long createdAt,
            long updatedAt,
            DocumentReference authorRef,
            DocumentReference pointsTransactionRef
    ) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorRef = authorRef;
        this.pointsTransactionRef = pointsTransactionRef;
    }


    //--- Getters


    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public DocumentReference getAuthorRef() {
        return authorRef;
    }

    public DocumentReference getPointsTransactionRef() {
        return pointsTransactionRef;
    }


    //--- Setters


    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAuthorRef(DocumentReference authorRef) {
        this.authorRef = authorRef;
    }

    public void setPointsTransactionRef(DocumentReference pointsTransactionRef) {
        this.pointsTransactionRef = pointsTransactionRef;
    }
}
