package com.hive.hive.model.association;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Session {

    private String id;
    private long startsAt;
    private long endsAt;
    private boolean isOrdinary; //Ordinaria/Estrordinaria
    private String isGeneral;    //Geral/Setorial

    private String associationId;
    private ArrayList<String> agendaIds;

    public Session(String id, long startsAt, long endsAt, boolean isOrdinary, String isGeneral, String associationId, ArrayList<String> agendaIds) {
        this.id = id;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.isOrdinary = isOrdinary;
        this.isGeneral = isGeneral;
        this.associationId = associationId;
        this.agendaIds = agendaIds;
    }

    //--- Getters

    public String getId() {
        return id;
    }

    public long getStartsAt() {
        return startsAt;
    }

    public long getEndsAt() {
        return endsAt;
    }

    public boolean isOrdinary() {
        return isOrdinary;
    }

    public String getIsGeneral() {
        return isGeneral;
    }

    public String getAssociationId() {
        return associationId;
    }

    public ArrayList<String> getAgendaIds() {
        return agendaIds;
    }
}
