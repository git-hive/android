package com.hive.hive.model.user;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class Role {

   private String id;
   private String name;
   private String associationId;

    // --- Constructors

    public Role(String id, String name, String associationId) {
        this.id = id;
        this.name = name;
        this.associationId = associationId;
    }


    // --- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAssociationId() {
        return associationId;
    }
// --- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }
}
