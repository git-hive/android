package com.hive.hive.model.association;

import com.hive.hive.model.event.Location;
import com.hive.hive.model.forum.Forum;
import com.hive.hive.model.user.Role;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Association {

    //--- SubCollections
    public static String REQUESTS_COLLECTION = "requests";

    //--- Base attributes
    private String id;
    private String name;

    //--- User Related
    private HashMap<String, Role> roles;

    //--- Forum Related
    private HashMap<String, Forum> forums;

    //--- Association related
    private int totalNumberOfAssociates;
        //--- Question
    private HashMap<String, Session> sessions;
        //--- Transparency
    private HashMap<String, BudgetTransaction> budgetTransactions;

    //--- Event Related
    private HashMap<String, Location> locations;

    //--- Constructor

    public Association(
            String id, String name, HashMap<String, Role> roles, HashMap<String, Forum> forums,
            int totalNumberOfAssociates, HashMap<String, Session> sessions, HashMap<String,
            BudgetTransaction> budgetTransactions, HashMap<String, Location> locations)
    {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.forums = forums;
        this.totalNumberOfAssociates = totalNumberOfAssociates;
        this.sessions = sessions;
        this.budgetTransactions = budgetTransactions;
        this.locations = locations;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Role> getRoles() {
        return roles;
    }

    public HashMap<String, Forum> getForums() {
        return forums;
    }

    public int getTotalNumberOfAssociates() {
        return totalNumberOfAssociates;
    }

    public HashMap<String, Session> getSessions() {
        return sessions;
    }

    public HashMap<String, BudgetTransaction> getBudgetTransactions() {
        return budgetTransactions;
    }

    public HashMap<String, Location> getLocations() {
        return locations;
    }


    //--- Setters


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoles(HashMap<String, Role> roles) {
        this.roles = roles;
    }

    public void setForums(HashMap<String, Forum> forums) {
        this.forums = forums;
    }

    public void setTotalNumberOfAssociates(int totalNumberOfAssociates) {
        this.totalNumberOfAssociates = totalNumberOfAssociates;
    }

    public void setSessions(HashMap<String, Session> sessions) {
        this.sessions = sessions;
    }

    public void setBudgetTransactions(HashMap<String, BudgetTransaction> budgetTransactions) {
        this.budgetTransactions = budgetTransactions;
    }

    public void setLocations(HashMap<String, Location> locations) {
        this.locations = locations;
    }
}
