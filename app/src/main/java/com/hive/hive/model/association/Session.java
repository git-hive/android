package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

public class Session {

    //--- Collections
    public static String AGENDAS_COLLECTION = "agendas";

    private String id;
    private long startsAt;
    private long endsAt;
    private boolean isOrdinary;  // Ordinaria/Estrordinaria
    private boolean isGeneral;   // Geral/Setorial

    private DocumentReference associationRef;

    public Session(
            String id,
            long startsAt,
            long endsAt,
            boolean isOrdinary,
            boolean isGeneral,
            DocumentReference associationRef
    ) {
        this.id = id;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.isOrdinary = isOrdinary;
        this.isGeneral = isGeneral;
        this.associationRef = associationRef;
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

    public boolean isGeneral() {
        return isGeneral;
    }

    public DocumentReference getAssociationRef() {
        return associationRef;
    }

}
