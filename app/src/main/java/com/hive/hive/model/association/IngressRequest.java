package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class IngressRequest {
    DocumentReference userRef;
    Long date;

    public IngressRequest(DocumentReference userRef, Long date) {
        this.userRef = userRef;
        this.date = date;
    }

    public IngressRequest(){}

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
