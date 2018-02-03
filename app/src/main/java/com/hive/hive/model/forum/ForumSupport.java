package com.hive.hive.model.forum;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumSupport extends ForumAction {

    private ForumAction targetAction;

    //--- Constructor

    public ForumSupport(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String forumId, ForumAction targetAction)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, forumId);
        this.targetAction = targetAction;
    }


    //-- Getters

    public ForumAction getTargetAction() {
        return targetAction;
    }


    //-- Setters

    public void setTargetAction(ForumAction targetAction) {
        this.targetAction = targetAction;
    }
}

