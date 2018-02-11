package com.hive.hive.model.association;

/**
 * Created by naraujo on 1/28/18.
 */

public class AssociationSupport extends AssociationAction {

    public static int SUPPORT_ACTION_VALUE = 1;

    private AssociationAction targetActionId;

    //--- Constructor
    public AssociationSupport(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String associationId, AssociationAction targetActionId)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.targetActionId = targetActionId;
    }

    //--- Getters

    public AssociationAction getTargetActionId() {
        return targetActionId;
    }

    //--- Setters

    public void setTargetActionId(AssociationAction targetActionId) {
        this.targetActionId = targetActionId;
    }
}
