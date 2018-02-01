package com.hive.hive.model.event;

import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 1/28/18.
 */

public class Checkin extends UserAction {

    private String eventId;

    //--- Constructors

    public Checkin(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String eventId)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId);
        this.eventId = eventId;
    }

    //--- Getters

    public String getEventId() {
        return eventId;
    }

    //--- Setters

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
