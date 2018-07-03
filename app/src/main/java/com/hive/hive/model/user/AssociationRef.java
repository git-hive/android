package com.hive.hive.model.user;

import com.google.firebase.firestore.DocumentReference;

public class AssociationRef {

    private DocumentReference associationRef;
    private DocumentReference roleRef;

    public AssociationRef(DocumentReference associationRef, DocumentReference roleRef) {
        this.associationRef = associationRef;
        this.roleRef = roleRef;
    }

    public AssociationRef() {
    }

    public DocumentReference getAssociationRef() {
        return associationRef;
    }

    public void setAssociationRef(DocumentReference associationRef) {
        this.associationRef = associationRef;
    }

    public DocumentReference getRoleRef() {
        return roleRef;
    }

    public void setRoleRef(DocumentReference roleRef) {
        this.roleRef = roleRef;
    }
}
