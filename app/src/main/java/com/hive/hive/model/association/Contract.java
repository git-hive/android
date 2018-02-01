package com.hive.hive.model.association;

/**
 * Created by naraujo on 1/28/18.
 */

public class Contract {

    private String id;
    private String employeeName;
    private String employeeCategory;
    private float wage;
    private long startsAt;
    private long endsAt;

    //--- Constructor

    public Contract(String id, String employeeName, String employeeCategory, float wage, long startsAt, long endsAt) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeCategory = employeeCategory;
        this.wage = wage;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeCategory() {
        return employeeCategory;
    }

    public float getWage() {
        return wage;
    }

    public long getStartsAt() {
        return startsAt;
    }

    public long getEndsAt() {
        return endsAt;
    }

    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeCategory(String employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public void setWage(float wage) {
        this.wage = wage;
    }

    public void setStartsAt(long startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(long endsAt) {
        this.endsAt = endsAt;
    }
}
