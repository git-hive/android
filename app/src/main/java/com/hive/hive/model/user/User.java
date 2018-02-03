package com.hive.hive.model.user;

import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class User {

    private String id;
    private HashSet<String> rolesIds;
    private LinkedHashMap<String, UserAction> actions;


    // --- Constructors

    public User(String id, HashSet rolesIds, LinkedHashMap<String, UserAction> actions) {
        this.id = id;
        this.rolesIds = rolesIds;
        this.actions = actions;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public HashSet<String> getRolesIds() {
        return rolesIds;
    }

    public LinkedHashMap<String, UserAction> getActions() {
        return actions;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setRolesIds(HashSet rolesIds) {
        this.rolesIds = rolesIds;
    }

    public void setActions(LinkedHashMap<String, UserAction> actions) {
        this.actions = actions;
    }
}
