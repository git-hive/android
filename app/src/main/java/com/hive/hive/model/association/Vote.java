package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;
import com.hive.hive.model.user.Role;

import java.util.HashMap;

/**
 * Created by naraujo on 1/28/18.
 */

public class Vote extends AssociationAction {

    private String sessionId;
    private String agendaId;
    private String votingOptionId;

    private float weight;
    private HashMap<String, Role> roles;

    //--- Constructor

    public Vote(String id, long createdAt, long updatedAt, DocumentReference authorId, DocumentReference pointsTransactionId, DocumentReference associationId, String sessionId, String agendaId, String votingOptionId, float weight, HashMap<String, Role> roles) {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.sessionId = sessionId;
        this.agendaId = agendaId;
        this.votingOptionId = votingOptionId;
        this.weight = weight;
        this.roles = roles;
    }


    //--- Getters

    public String getSessionId() {
        return sessionId;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public String getVotingOptionId() {
        return votingOptionId;
    }

    public float getWeight() {
        return weight;
    }

    public HashMap<String, Role> getRoles() {
        return roles;
    }


    //--- Setters

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public void setVotingOptionId(String votingOptionId) {
        this.votingOptionId = votingOptionId;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setRoles(HashMap<String, Role> roles) {
        this.roles = roles;
    }
}
