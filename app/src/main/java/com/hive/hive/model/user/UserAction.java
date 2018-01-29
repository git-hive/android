package com.hive.hive.model.user;

import com.hive.hive.model.marketplace.PointsTransaction;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class UserAction {

    private String id;
    private long createdAt;
    private long updatedAt;
    private String authorId;
    private PointsTransaction pointsTransaction;



    //--- Constructor
    public UserAction(String id, String authorId, PointsTransaction pointsTransaction) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.authorId = authorId;
        this.pointsTransaction = pointsTransaction;
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

    public PointsTransaction getPointsTransaction() {
        return pointsTransaction;
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

    public void setPointsTransaction(PointsTransaction pointsTransaction) {
        this.pointsTransaction = pointsTransaction;
    }
}
