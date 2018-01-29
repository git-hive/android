package com.hive.hive.model.forum;

import java.util.HashSet;

/**
 * Created by naraujo on 1/28/18.
 */

public class Forum {

    private String id;
    private String name;
    private String association;
    private HashSet<String> allowedRoles;


    //--- Constructor

    public Forum(String id, String name, String association, HashSet<String> allowedRoles) {
        this.id = id;
        this.name = name;
        this.association = association;
        this.allowedRoles = allowedRoles;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAssociation() {
        return association;
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

    public void setAssociation(String association) {
        this.association = association;
    }

    public void setAllowedRoles(HashSet<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }
}
