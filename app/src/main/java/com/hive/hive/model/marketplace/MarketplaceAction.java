package com.hive.hive.model.marketplace;

import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public class MarketplaceAction extends UserAction {

    public MarketplaceAction(String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId);
    }
}
