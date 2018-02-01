package com.hive.hive.model.user;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class User {

    private String id;
    private HashMap<String, Role> roles;
    private LinkedHashMap<String, UserAction> actions;


    // --- Constructors

    public User(String id, HashMap<String, Role> roles, LinkedHashMap<String, UserAction> actions) {
        this.id = id;
        this.roles = roles;
        this.actions = actions;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public HashMap<String, Role> getRoles() {
        return roles;
    }

    public LinkedHashMap<String, UserAction> getActions() {
        return actions;
    }


    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setRoles(HashMap<String, Role> roles) {
        this.roles = roles;
    }

    public void setActions(LinkedHashMap<String, UserAction> actions) {
        this.actions = actions;
    }
}
