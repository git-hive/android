package com.hive.hive.model.user;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class UserAction {

    private String id;
    private long createdAt;
    private long updatedAt;
    private String authorId;
    private String pointsTransactionId;



    //--- Constructor

    public UserAction(String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.pointsTransactionId = pointsTransactionId;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getPointsTransactionId() {
        return pointsTransactionId;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setPointsTransactionId(String pointsTransactionId) {
        this.pointsTransactionId = pointsTransactionId;
    }
}
