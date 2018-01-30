package com.hive.hive.model.forum;

import com.hive.hive.model.marketplace.PointsTransaction;
import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class ForumAction extends UserAction {

    private String forumId;

    //--- Constructors


    public ForumAction() {
    }

    public ForumAction(String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId, String forumId) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId);
        this.forumId = forumId;
    }


    //--- Getters

    public String getForumId() {
        return forumId;
    }

    //--- Setters

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }
}
