package com.hive.hive.model.marketplace;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Sale {

    private String id;
    private long createdAt;
    private long updatedAt;
    private long expireAt;

    private String description;
    private int price;
    private long numberSold;
    private long maxNumberOfSales;

    private ArrayList<String> productsIds; //Ids from products in this sale

    //--- Constructor



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

    public long getExpireAt() {
        return expireAt;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public long getNumberSold() {
        return numberSold;
    }

    public long getMaxNumberOfSales() {
        return maxNumberOfSales;
    }

    public ArrayList<String> getProductsIds() {
        return productsIds;
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

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setNumberSold(long numberSold) {
        this.numberSold = numberSold;
    }

    public void setMaxNumberOfSales(long maxNumberOfSales) {
        this.maxNumberOfSales = maxNumberOfSales;
    }

    public void setProductsIds(ArrayList<String> productsIds) {
        this.productsIds = productsIds;
    }
}
