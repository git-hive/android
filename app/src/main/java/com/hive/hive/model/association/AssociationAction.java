package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.model.user.UserAction;

public class AssociationAction extends UserAction {

    private DocumentReference associationRef;

    //--- Constructors


    public AssociationAction() {}

    public AssociationAction(
            String id,
            long createdAt,
            long updatedAt,
            DocumentReference authorRef,
            DocumentReference pointsTransactionRef,
            DocumentReference associationRef
    ) {
        super(id, createdAt, updatedAt, authorRef, pointsTransactionRef);
        this.associationRef = associationRef;
    }

    //--- Getters

    public DocumentReference getAssociationRef() {
        return associationRef;
    }

    //--- Setters

    public void setAssociationRef(DocumentReference associationRef) {
        this.associationRef = associationRef;
    }
}
