package com.hive.hive.model.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Location {

    private String id;
    private String name;
    private LatLng coords;

    private String associationId;

    private HashMap<String, Event> events;

    //--- Construtor

    public Location(
            String id, String name, LatLng coords,
            String associationId, HashMap<String, Event> events)
    {
        this.id = id;
        this.name = name;
        this.coords = coords;
        this.associationId = associationId;
        this.events = events;
    }


    //--- Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getCoords() {
        return coords;
    }

    public String getAssociationId() {
        return associationId;
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    //--- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }

    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }

    public void setEvents(HashMap<String, Event> events) {
        this.events = events;
    }
}
