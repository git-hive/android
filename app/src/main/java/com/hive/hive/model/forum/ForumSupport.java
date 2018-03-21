package com.hive.hive.model.forum;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumSupport extends ForumAction {

    private ForumAction targetAction;

    //--- Constructor

    public ForumSupport(
            String id, long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId,
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

