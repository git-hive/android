package com.hive.hive.model.event;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Event {

    private String name;
    private long createdAt;
    private long updatedAt;
    private long startAt;
    private long finishAt;

    private String authorId;
    private String locationId;

    private ArrayList<String> checkInIds;   //Check-ins made at the event

    //--- Constructor

    public Event(String name, long createdAt, long updatedAt, long startAt, long finishAt, String authorId, String locationId, ArrayList<String> checkInIds) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.authorId = authorId;
        this.locationId = locationId;
        this.checkInIds = checkInIds;
    }

    //--- Getters

    public String getName() {
        return name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public long getStartAt() {
        return startAt;
    }

    public long getFinishAt() {
        return finishAt;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getLocationId() {
        return locationId;
    }

    public ArrayList<String> getCheckInIds() {
        return checkInIds;
    }


    //--- Setters


    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public void setFinishAt(long finishAt) {
        this.finishAt = finishAt;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setCheckInIds(ArrayList<String> checkInIds) {
        this.checkInIds = checkInIds;
    }
}
