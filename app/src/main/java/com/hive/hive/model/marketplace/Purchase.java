package com.hive.hive.model.marketplace;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by naraujo on 1/28/18.
 */

public class Purchase extends MarketplaceAction {

    //--- Constructors

    public Purchase(String saleId) {
        super(saleId);
    }

    public Purchase(long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId, String saleId) {
        super(createdAt, updatedAt, authorId, pointsTransactionId, saleId);
    }


    //--- Getters


    //--- Setters

}
