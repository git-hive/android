package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class AssociationSupport extends AssociationAction {

    public static int SUPPORT_ACTION_VALUE = 1;

    private DocumentReference targetActionRef;

    //--- Constructor
    public AssociationSupport(
            long
            createdAt, long
            updatedAt,
            DocumentReference authorRef,
            DocumentReference pointsTransactionRef,
            DocumentReference associationRef,
            DocumentReference targetActionRef
    ) {
        super(createdAt, updatedAt, authorRef, pointsTransactionRef, associationRef);
        this.targetActionRef = targetActionRef;
    }

    //--- Getters

    public DocumentReference getTargetActionRef() {
        return targetActionRef;
    }

    //--- Setters

    public void setTargetActionRef(DocumentReference targetActionRef) {
        this.targetActionRef = targetActionRef;
    }
}
