package com.hive.hive.model.marketplace;

import java.util.HashMap;

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

    private HashMap<String, Product> products; //Ids from products in this sale
    private HashMap<String, Purchase> purchases;

    //--- Constructor

    public Sale(
            String id, long createdAt, long updatedAt, long expireAt, String description, int price,
            long numberSold, long maxNumberOfSales, HashMap<String, Product> products,
            HashMap<String, Purchase> purchases)
    {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expireAt = expireAt;
        this.description = description;
        this.price = price;
        this.numberSold = numberSold;
        this.maxNumberOfSales = maxNumberOfSales;
        this.products = products;
        this.purchases = purchases;
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

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public HashMap<String, Purchase> getPurchases() {
        return purchases;
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

    public void setProducts(HashMap<String, Product> products) {
        this.products = products;
    }

    public void setPurchases(HashMap<String, Purchase> purchases) {
        this.purchases = purchases;
    }

}
