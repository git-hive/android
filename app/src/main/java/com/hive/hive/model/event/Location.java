package com.hive.hive.model.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Location {

    private String id;
    private String name;
    private LatLng coords;

    private ArrayList<String> eventsIds;

    //--- Construtor

    public Location(String id, String name, LatLng coords, ArrayList<String> eventsIds) {
        this.id = id;
        this.name = name;
        this.coords = coords;
        this.eventsIds = eventsIds;
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

    public ArrayList<String> getEventsIds() {
        return eventsIds;
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

    public void setEventsIds(ArrayList<String> eventsIds) {
        this.eventsIds = eventsIds;
    }
}
