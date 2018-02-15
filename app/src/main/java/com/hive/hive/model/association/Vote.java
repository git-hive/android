package com.hive.hive.model.association;

import android.view.View;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class Vote extends AssociationAction {

    private String votingOption;
    private float weight;

    private ArrayList<DocumentReference> rolesRefs;

    private DocumentReference agendaRef;
    private DocumentReference sessionRef;

    private View.OnClickListener requestBtnClickListener;

    //--- Constructor
    public  Vote(){
        // EMPTY
    }

    public Vote(
            String id,
            long createdAt,
            long updatedAt,
            DocumentReference authorRef,
            DocumentReference pointsTransactionRef,
            DocumentReference associationRef,
            String votingOption,
            float weight,
            ArrayList<DocumentReference> rolesRefs,
            DocumentReference agendaRef,
            DocumentReference sessionRef
    ) {
        super(id, createdAt, updatedAt, authorRef, pointsTransactionRef, associationRef);
        this.votingOption = votingOption;
        this.weight = weight;
        this.rolesRefs = rolesRefs;
        this.agendaRef = agendaRef;
        this.sessionRef = sessionRef;
    }


    //--- Getters

    public String getVotingOption() {
        return votingOption;
    }

    public float getWeight() {
        return weight;
    }

    public ArrayList<DocumentReference> getRolesRefs() {
        return rolesRefs;
    }

    public DocumentReference getAgendaRef() {
        return agendaRef;
    }

    public DocumentReference getSessionRef() {
        return sessionRef;
    }


    //--- Setters


    public void setVotingOption(String votingOption) {
        this.votingOption = votingOption;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setRolesRefs(ArrayList<DocumentReference> rolesRefs) {
        this.rolesRefs = rolesRefs;
    }

    public void setAgendaRef(DocumentReference agendaRef) {
        this.agendaRef = agendaRef;
    }

    public void setSessionRef(DocumentReference sessionRef) {
        this.sessionRef = sessionRef;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
}
