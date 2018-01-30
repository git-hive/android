package com.hive.hive.model.marketplace;

/**
 * Created by naraujo on 1/28/18.
 */

public class PartnershipPan {

    private String id;
    private long createdAt;
    private long updatedAt;
    private long validUntil;
    private String name;
    private String monthlyCost;
    private String yearlyCost;

    //--- Constructor

    public PartnershipPan(String id, long createdAt, long updatedAt, long validUntil, String name, String monthlyCost, String yearlyCost) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.validUntil = validUntil;
        this.name = name;
        this.monthlyCost = monthlyCost;
        this.yearlyCost = yearlyCost;
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

    public long getValidUntil() {
        return validUntil;
    }

    public String getName() {
        return name;
    }

    public String getMonthlyCost() {
        return monthlyCost;
    }

    public String getYearlyCost() {
        return yearlyCost;
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

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMonthlyCost(String monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public void setYearlyCost(String yearlyCost) {
        this.yearlyCost = yearlyCost;
    }
}
