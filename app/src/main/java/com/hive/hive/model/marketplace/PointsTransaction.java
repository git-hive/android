package com.hive.hive.model.marketplace;

import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public class PointsTransaction {

    private String id;
    private long createdAt;
    private long updatedAt;
    private int value;

    private String userActionId;    //Associated user action that is the reason of the transaction
    private String walletId;        //Associated wallet where the transactions acts


    //--- Constructors

    public PointsTransaction(String id, long createdAt, long updatedAt, int value, String walletId, String userActionId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.value = value;
        this.walletId = walletId;
        this.userActionId = userActionId;
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

    public int getValue() {
        return value;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getUserActionId() {
        return userActionId;
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

    public void setValue(int value) {
        this.value = value;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public void setUserActionId(String userActionId) {
        this.userActionId = userActionId;
    }
}
