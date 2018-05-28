package com.hive.hive.model.forum;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public abstract class ForumAction extends UserAction {

    //--- Constructors


    public ForumAction() {
    }

    public ForumAction(long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId) {
        super(createdAt, updatedAt, authorId, pointsTransactionId);
    }

}
