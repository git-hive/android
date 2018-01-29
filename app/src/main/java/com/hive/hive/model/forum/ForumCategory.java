package com.hive.hive.model.forum;

/**
 * Created by naraujo on 1/29/18.
 */

public class ForumCategory {

    private String id;
    private String name;

    //--- Constructor

    public ForumCategory(String id, String name) {
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
