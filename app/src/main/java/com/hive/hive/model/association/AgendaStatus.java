package com.hive.hive.model.association;

/**
 * Created by naraujo on 1/28/18.
 */

public class AgendaStatus {

    private String id;
    private String title;
    private String description;


    //--- Constructor
    public AgendaStatus(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
