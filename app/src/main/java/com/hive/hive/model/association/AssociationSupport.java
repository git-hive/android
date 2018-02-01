package com.hive.hive.model.association;

/**
 * Created by naraujo on 1/28/18.
 */

public class AssociationSupport extends AssociationAction {

    private String targetActionId;

    //--- Constructor
    public AssociationSupport(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String associationId, String targetActionId)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.targetActionId = targetActionId;
    }

    //--- Getters

    public String getTargetActionId() {
        return targetActionId;
    }

    //--- Setters

    public void setTargetActionId(String targetActionId) {
        this.targetActionId = targetActionId;
    }
}
