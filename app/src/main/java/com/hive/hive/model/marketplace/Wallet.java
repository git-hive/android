package com.hive.hive.model.marketplace;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Wallet {

    private String id;
    private int balance;

    private String userId;

    //HashMap with ID->Transaction for all Transactions in this wallet
    private HashMap<String, PointsTransaction> transactionsIds;


    //--- Constructors

    public Wallet(String id, int balance, String userId, HashMap<String, PointsTransaction> transactionsIds) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
        this.transactionsIds = transactionsIds;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public String getUserId() {
        return userId;
    }

    public HashMap<String, PointsTransaction> getTransactionsIds() {
        return transactionsIds;
    }

    //--- Setters


    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTransactionsIds(HashMap<String, PointsTransaction> transactionsIds) {
        this.transactionsIds = transactionsIds;
    }
}
