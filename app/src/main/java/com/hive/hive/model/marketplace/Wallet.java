package com.hive.hive.model.marketplace;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Wallet {

    private String id;
    private int balance;

    private String userId;
    private ArrayList<String> transactionsIds;

    //--- Constructors

    public Wallet(String id, int balance, String userId, ArrayList<String> transactionsIds) {
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

    public ArrayList<String> getTransactionsIds() {
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

    public void setTransactionsIds(ArrayList<String> transactionsIds) {
        this.transactionsIds = transactionsIds;
    }
}
