package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Association {

    //--- Collections
    public static String ROLES_COLLECTIONS = "roles";
    public static String BUDGET_TRANSACTIONS_COLLECTION = "budgetTransactions";
    public static String SESSIONS_COLLECTION = "sessions";
    public static String REQUESTS_COLLECTION = "requests";
    public static String REQUEST_CATEGORIES_COLLECTION = "requestCategories";
    public static String LOCATIONS_COLLECTION = "locations";

    //--- Base attributes
    private String id;
    private String name;

    //--- Forum Related
    private ArrayList<DocumentReference> forumsIDs;

    //--- Association related
    private int totalNumberOfAssociates;

    //--- Constructor

    public Association(
            String id,
            String name,
            int totalNumberOfAssociates,
            ArrayList<DocumentReference> forumsIDs
    ) {
        this.id = id;
        this.name = name;
        this.totalNumberOfAssociates = totalNumberOfAssociates;
        this.forumsIDs = forumsIDs;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<DocumentReference> getForumsIDs() {
        return forumsIDs;
    }

    public int getTotalNumberOfAssociates() {
        return totalNumberOfAssociates;
    }


    //--- Setters


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForumsIDs(ArrayList<DocumentReference> forumsIDs) {
        this.forumsIDs = forumsIDs;
    }

    public void setTotalNumberOfAssociates(int totalNumberOfAssociates) {
        this.totalNumberOfAssociates = totalNumberOfAssociates;
    }

}
