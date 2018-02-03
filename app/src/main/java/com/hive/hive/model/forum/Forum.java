package com.hive.hive.model.forum;

import java.util.HashSet;

/**
 * Created by naraujo on 1/28/18.
 */

public class Forum {

    private String id;
    private String name;
    private String associationId;
    private HashSet<String> allowedRoles;


    //--- Constructor

    public Forum(String id, String name, String associationId, HashSet<String> allowedRoles) {
        this.id = id;
        this.name = name;
        this.associationId = associationId;
        this.allowedRoles = allowedRoles;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAssociationId() {
        return associationId;
    }

    public HashSet<String> getAllowedRoles() {
        return allowedRoles;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }

    public void setAllowedRoles(HashSet<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }
}
