package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class IngressRequest {
    DocumentReference userRef;
    Long date;
    String cpf, name, email, photoUrl;

    public IngressRequest(DocumentReference userRef, Long date, String cpf, String name, String email, String photoUrl) {
        this.userRef = userRef;
        this.date = date;
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public IngressRequest(){}

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DocumentReference userRef) {
        this.userRef = userRef;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
