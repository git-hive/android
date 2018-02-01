package com.hive.hive.model.user;

import com.hive.hive.model.association.Association;
import com.hive.hive.model.user.Role;

/**
 * Created by naraujo on 1/28/18.
 */

public class Associate extends Role {

    private Association association;


    //--- Constructor

    public Associate(String id, String name, Association association) {
        super(id, name);
        this.association = association;
    }

    //--- Getters

    public Association getAssociation() {
        return association;
    }

    //--- Setters

    public void setAssociation(Association association) {
        this.association = association;
    }
}
