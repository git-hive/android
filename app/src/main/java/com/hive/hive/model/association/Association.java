package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Association {

    private String name;
    private Integer totalNumberOfAssociates;
    private String cnpj;
    private String type; // condominio// bairros planejados// bairros abertos// empresa // encubadora


    //--- Constructor

    public Association(String name, Integer totalNumberOfAssociates, String cnpj, String type) {
        this.name = name;
        this.totalNumberOfAssociates = totalNumberOfAssociates;
        this.cnpj = cnpj;
        this.type = type;
    }

    public Association() {
    }


    //--- Getters

    public String getName() {
        return name;
    }

    public Integer getTotalNumberOfAssociates() {
        return totalNumberOfAssociates;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getType() {
        return type;
    }

    //--- Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalNumberOfAssociates(Integer totalNumberOfAssociates) {
        this.totalNumberOfAssociates = totalNumberOfAssociates;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setType(String type) {
        this.type = type;
    }
}
