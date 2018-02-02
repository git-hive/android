package com.hive.hive.model.association;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class BudgetTransaction {

    private String id;
    private long created_at;
    private long updated_at;
    private long date;

    private String authorId;
    private HashMap<String, BudgetTransactionCategories> budgetTransactionCategoriesIds;

    //--- Constructor
    public BudgetTransaction(String id, long created_at, long updated_at, long date, String authorId, HashMap<String, BudgetTransactionCategories> budgetTransactionCategoriesIds) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.date = date;
        this.authorId = authorId;
        this.budgetTransactionCategoriesIds = budgetTransactionCategoriesIds;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public long getDate() {
        return date;
    }

    public String getAuthorId() {
        return authorId;
    }

    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setBudgetTransactionCategoriesIds(HashMap<String, BudgetTransactionCategories> budgetTransactionCategoriesIds) {
        this.budgetTransactionCategoriesIds = budgetTransactionCategoriesIds;
    }

    public HashMap<String, BudgetTransactionCategories> getBudgetTransactionCategoriesIds() {
        return budgetTransactionCategoriesIds;
    }
}
