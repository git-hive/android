package com.hive.hive.model.association;

/**
 * Created by naraujo on 2/2/18.
 */

public class RequestCategory {

    private String id;
    private String name;

    //--- Constructors

    public RequestCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
