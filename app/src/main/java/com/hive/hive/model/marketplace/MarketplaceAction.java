package com.hive.hive.model.marketplace;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public class MarketplaceAction extends UserAction {

    private String saleId;

    //---  Constructors

    public MarketplaceAction(String saleId) {
        this.saleId = saleId;
    }

    public MarketplaceAction(String id, long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId, String saleId) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId);
        this.saleId = saleId;
    }

    //--- Getters

    public String getSaleId() {
        return saleId;
    }

    //--- Setters


    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }
}
