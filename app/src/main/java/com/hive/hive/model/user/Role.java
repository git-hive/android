package com.hive.hive.model.user;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class Role {

    private String description;
    //   private String key;
    private String name;

    // --- Constructors

    public Role(String description, String name) {
        this.description = description;
        this.name = name;
    }


    // --- Getters

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

// --- Setters

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
