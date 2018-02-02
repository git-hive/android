package com.hive.hive.model.marketplace;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Product {

    private String id;
    private String name;
    private String description;

    private String partnerId;
    private HashMap<String, MarketplaceCategories> categoriesIds;
    private HashMap<String, Sale> sales;

    //--- Constructor

    public Product(
            String id, String name, String description, String partnerId, HashMap<String,
            MarketplaceCategories> categoriesIds, HashMap<String, Sale> sales)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.partnerId = partnerId;
        this.categoriesIds = categoriesIds;
        this.sales = sales;
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

    public HashMap<String, MarketplaceCategories> getCategoriesIds() {
        return categoriesIds;
    }

    public HashMap<String, Sale> getSales() {
        return sales;
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

    public void setCategoriesIds(HashMap<String, MarketplaceCategories> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }

    public void setSales(HashMap<String, Sale> sales) {
        this.sales = sales;
    }

}
