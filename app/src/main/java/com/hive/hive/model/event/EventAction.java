package com.hive.hive.model.event;

import com.hive.hive.model.user.UserAction;

/**
 * Created by naraujo on 2/2/18.
 */

public abstract class EventAction extends UserAction {

    private Event event;

    //--- Cosntructor

    public EventAction(String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId, Event event) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId);
        this.event = event;
    }

    //--- Getters

    public Event getEvent() {
        return event;
    }


    //--- Setters

    public void setEvent(Event event) {
        this.event = event;
    }

}
