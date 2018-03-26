package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class Session {

    //--- Collections
    public static String AGENDAS_COLLECTION = "agendas";

    //--- SubCollections
    public static String QUESTIONS_COLLECTION = "question";


    private long startsAt;
    private long endsAt;
    private boolean isOrdinary;  // Ordinaria/Estrordinaria
    private boolean isGeneral;   // Geral/Setorial
    private int agendasNum;
    private DocumentReference associationRef;
    private String status; // Past/ Current/ Future;

    public Session(
            long startsAt,
            long endsAt,
            boolean isOrdinary,
            boolean isGeneral,
            int agendasNum,
            DocumentReference associationRef,
            String status
    ) {
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.isOrdinary = isOrdinary;
        this.isGeneral = isGeneral;
        this.agendasNum = agendasNum;
        this.associationRef = associationRef;
        this.status = status;
    }

    public Session() {
    }

    //--- Getters


    public long getStartsAt() {
        return startsAt;
    }

    public long getEndsAt() {
        return endsAt;
    }

    public boolean isOrdinary() {
        return isOrdinary;
    }

    public boolean isGeneral() {
        return isGeneral;
    }

    public DocumentReference getAssociationRef() {
        return associationRef;
    }

    public int getAgendasNum() { return  agendasNum;}

    public String getStatus() { return status; }

//--- Setters

    public void setStartsAt(long startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(long endsAt) {
        this.endsAt = endsAt;
    }

    public void setOrdinary(boolean ordinary) {
        isOrdinary = ordinary;
    }

    public void setGeneral(boolean general) {
        isGeneral = general;
    }

    public void setAgendasNum(int agendasNum) {
        this.agendasNum = agendasNum;
    }

    public void setAssociationRef(DocumentReference associationRef) { this.associationRef = associationRef; }

    public void setStatus(String status) { this.status = status; }
}
