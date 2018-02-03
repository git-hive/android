package com.hive.hive.model.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Checkin extends EventAction {

    private LatLng checkedPosition;

    //--- Constructors

    public Checkin(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            Event event, LatLng checkedPosition)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, event);
        this.checkedPosition = checkedPosition;
    }


    //--- Getters

    public LatLng getCheckedPosition() {
        return checkedPosition;
    }

    //--- Setters

    public void setCheckedPosition(LatLng checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

}
