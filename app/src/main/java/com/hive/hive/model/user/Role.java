package com.hive.hive.model.user;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class Role {
   private String id;
   private String name;

    // --- Constructors

    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // --- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // --- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
