package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Association {

    //--- Base attributes
    private String id;
    private String name;

    //--- User Related
    private ArrayList<String> rolesIds;

    //--- Forum Related
    private ArrayList<String> forumIds;

    //--- Association related
    private int totalNumberOfAssociates;
        //--- Request
    private ArrayList<String> requestsIds;
        //--- Voting
    private ArrayList<String> sessionIds;
        //--- Transparency
    private ArrayList<String> budgetTransactionsIds;

    //--- Event Related
    private ArrayList<String> locationsIds;

    //--- Constructor

    public Association(
            String id, String name, ArrayList<String> rolesIds, ArrayList<String> forumIds,
            int totalNumberOfAssociates, ArrayList<String> requestsIds, ArrayList<String> sessionIds,
            ArrayList<String> budgetTransactionsIds, ArrayList<String> locationsIds)
    {
        this.id = id;
        this.name = name;
        this.rolesIds = rolesIds;
        this.forumIds = forumIds;
        this.totalNumberOfAssociates = totalNumberOfAssociates;
        this.requestsIds = requestsIds;
        this.sessionIds = sessionIds;
        this.budgetTransactionsIds = budgetTransactionsIds;
        this.locationsIds = locationsIds;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getRolesIds() {
        return rolesIds;
    }

    public ArrayList<String> getForumIds() {
        return forumIds;
    }

    public int getTotalNumberOfAssociates() {
        return totalNumberOfAssociates;
    }

    public ArrayList<String> getRequestsIds() {
        return requestsIds;
    }

    public ArrayList<String> getSessionIds() {
        return sessionIds;
    }

    public ArrayList<String> getBudgetTransactionsIds() {
        return budgetTransactionsIds;
    }

    public ArrayList<String> getLocationsIds() {
        return locationsIds;
    }


    //--- Setters


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRolesIds(ArrayList<String> rolesIds) {
        this.rolesIds = rolesIds;
    }

    public void setForumIds(ArrayList<String> forumIds) {
        this.forumIds = forumIds;
    }

    public void setTotalNumberOfAssociates(int totalNumberOfAssociates) {
        this.totalNumberOfAssociates = totalNumberOfAssociates;
    }

    public void setRequestsIds(ArrayList<String> requestsIds) {
        this.requestsIds = requestsIds;
    }

    public void setSessionIds(ArrayList<String> sessionIds) {
        this.sessionIds = sessionIds;
    }

    public void setBudgetTransactionsIds(ArrayList<String> budgetTransactionsIds) {
        this.budgetTransactionsIds = budgetTransactionsIds;
    }

    public void setLocationsIds(ArrayList<String> locationsIds) {
        this.locationsIds = locationsIds;
    }
}
