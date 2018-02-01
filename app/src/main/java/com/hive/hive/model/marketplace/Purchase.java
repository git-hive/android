package com.hive.hive.model.marketplace;

/**
 * Created by naraujo on 1/28/18.
 */

public class Purchase extends MarketplaceAction {

    private String saleId;

    //--- Constructors

    public Purchase(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String saleId)
    {
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
