package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class Report {
    private DocumentReference associationRef;
    private String description;
    private Long startsAt, endsAt;
    private Boolean showing;

    public Report(DocumentReference associationRef, String description, Long startsAt, Long endsAt, Boolean showing) {
        this.associationRef = associationRef;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.showing = showing;
    }

    public Report() {
    }

    public DocumentReference getAssociationRef() {
        return associationRef;
    }

    public void setAssociationRef(DocumentReference associationRef) {
        this.associationRef = associationRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Long startsAt) {
        this.startsAt = startsAt;
    }

    public Long getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Long endsAt) {
        this.endsAt = endsAt;
    }

    public Boolean getShowing() {
        return showing;
    }

    public void setShowing(Boolean showing) {
        this.showing = showing;
    }
}
