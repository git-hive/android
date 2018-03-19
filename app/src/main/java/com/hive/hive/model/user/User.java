package com.hive.hive.model.user;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by naraujo on 1/28/18.
 */

public class User {
    private String birthday, cpf, email, name, photoUrl;
    private HashSet<DocumentReference> roles;
    private ArrayList<CollectionReference> actions;


// --- Constructors

    public User(String birthday, String cpf, String email, String name, HashSet roles, ArrayList actions, String photoUrl) {
        this.birthday = birthday;
        this.cpf = cpf;
        this.email = email;
        this.name = name;
        this.roles = roles;
        this.actions = actions;
        this.photoUrl = photoUrl;
    }

    public User() {
    }

    //--- Getters
    public HashSet<DocumentReference> getRoles() {
        return roles;
    }

    public ArrayList<CollectionReference> getActions() {
        return actions;
    }

    public String getBirthday() { return birthday; }

    public String getCpf() { return cpf; }

    public String getEmail() { return email; }

    public String getName() { return name; }

    public String getPhotoUrl(){ return photoUrl; }
    //--- Setters

    public void setActions(ArrayList<CollectionReference> actions) {
        this.actions = actions;
    }

    public void setRoles(HashSet<DocumentReference> roles) { this.roles = roles; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
