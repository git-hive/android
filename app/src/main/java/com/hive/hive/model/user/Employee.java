package com.hive.hive.model.user;

import com.hive.hive.model.association.Contract;

import java.util.LinkedHashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Employee extends Role {

    private LinkedHashMap<String, Contract> contracts;

    //-- Constructor

    public Employee(String id, String name, String associationId, LinkedHashMap<String, Contract> contracts) {
        super(id, name, associationId);
        this.contracts = contracts;
    }


    //--- Getters

    public LinkedHashMap<String, Contract> getContracts() {
        return contracts;
    }

    //--- Setters

    public void setContracts(LinkedHashMap<String, Contract> contracts) {
        this.contracts = contracts;
    }
}
