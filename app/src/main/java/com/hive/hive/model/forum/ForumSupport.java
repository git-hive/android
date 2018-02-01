package com.hive.hive.model.forum;

import com.hive.hive.model.marketplace.PointsTransaction;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumSupport extends ForumAction {

    private String targetActionId;

    //--- Constructor

    public ForumSupport(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String forumId, String targetActionId)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, forumId);
        this.targetActionId = targetActionId;
    }


    //-- Getters

    public String getTargetActionId() {
        return targetActionId;
    }


    //-- Setters

    public void setTargetActionId(String targetActionId) {
        this.targetActionId = targetActionId;
    }
}

