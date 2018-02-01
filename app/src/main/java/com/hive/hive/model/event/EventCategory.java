package com.hive.hive.model.event;

/**
 * Created by naraujo on 1/29/18.
 */

public class EventCategory {
    private String id;
    private String name;

    //--- Constructor

    public EventCategory(String id, String name) {
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
