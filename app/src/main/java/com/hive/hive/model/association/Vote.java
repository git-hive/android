package com.hive.hive.model.association;

import com.hive.hive.model.association.AssociationAction;

/**
 * Created by naraujo on 1/28/18.
 */

public class Vote extends AssociationAction {

    private String sessionId;
    private String agendaId;
    private String votingOptionId;

    //--- Constructor

    public Vote(
            String id, long createdAt, long updatedAt, String authorId, String pointsTransactionId,
            String associationId, String sessionId, String agendaId, String votingOptionId)
    {
        super(id, createdAt, updatedAt, authorId, pointsTransactionId, associationId);
        this.sessionId = sessionId;
        this.agendaId = agendaId;
        this.votingOptionId = votingOptionId;
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
}
