package com.hive.hive.model.forum;

import com.hive.hive.model.marketplace.PointsTransaction;
import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class ForumAction extends UserAction {

    private String forumId;

    //--- Constructors

    public ForumAction(String id, String authorId, PointsTransaction pointsTransaction, String forumId) {
        super(id, authorId, pointsTransaction);
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
