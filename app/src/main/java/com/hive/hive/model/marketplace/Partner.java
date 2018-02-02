package com.hive.hive.model.marketplace;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Partner {

    private String id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String site;

    private String currentPlanId;
    private ArrayList<String> previousPlansIds;

    private HashMap<String, Product> products;

    //--- Constructor

    public Partner(
            String id, String name, String phone, String address, String email, String site,
            String currentPlanId, ArrayList<String> previousPlansIds, HashMap<String, Product> products)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.site = site;
        this.currentPlanId = currentPlanId;
        this.previousPlansIds = previousPlansIds;
        this.products = products;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getSite() {
        return site;
    }

    public String getCurrentPlanId() {
        return currentPlanId;
    }

    public ArrayList<String> getPreviousPlansIds() {
        return previousPlansIds;
    }

    public HashMap<String, Product> getProducts() {
        return products;
    }


    //--- Setters


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setCurrentPlanId(String currentPlanId) {
        this.currentPlanId = currentPlanId;
    }

    public void setPreviousPlansIds(ArrayList<String> previousPlansIds) {
        this.previousPlansIds = previousPlansIds;
    }

    public void setProducts(HashMap<String, Product> products) {
        this.products = products;
    }
}
