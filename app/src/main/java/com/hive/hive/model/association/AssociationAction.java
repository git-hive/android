package com.hive.hive.model.association;

import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public class AssociationAction extends UserAction {

    private String associationId;

    //--- Constructors

    public AssociationAction(String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId, String associationId) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId);
        this.associationId = associationId;
    }

    //--- Getters

    public String getAssociationId() {
        return associationId;
    }

    //--- Setters

    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }
}
