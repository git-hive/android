package com.hive.hive.model.marketplace;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Product {

    private String id;
    private String name;
    private String description;

    private String partnerId;
    private ArrayList<String> categoriesIds;

    //--- Constructor

    public Product(String id, String name, String description, String partnerId, ArrayList<String> categoriesIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.partnerId = partnerId;
        this.categoriesIds = categoriesIds;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public ArrayList<String> getCategoriesIds() {
        return categoriesIds;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setCategoriesIds(ArrayList<String> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }
}
