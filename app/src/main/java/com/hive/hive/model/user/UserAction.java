package com.hive.hive.model.user;

import com.hive.hive.model.marketplace.PointsTransaction;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class UserAction {

    private String id;
    private PointsTransaction pointsTransaction;


    //--- Constructor
    public UserAction(String id, PointsTransaction pointsTransaction) {
        this.id = id;
        this.pointsTransaction = pointsTransaction;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public PointsTransaction getPointsTransaction() {
        return pointsTransaction;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setPointsTransaction(PointsTransaction pointsTransaction) {
        this.pointsTransaction = pointsTransaction;
    }
}
