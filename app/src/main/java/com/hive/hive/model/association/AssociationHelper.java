package com.hive.hive.model.association;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class AssociationHelper {
    public static String ASSOCIATION_COLLECTION = "associations";

    //--- Request

    /**
     * Fetches all requests
     *
     * @param db            Database reference
     * @param associationID Document ID where to get the requests from
     * @return Task that resolves in all request documents
     */
    public static Task<QuerySnapshot> getAllRequests(FirebaseFirestore db, String associationID) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single request document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID to be fetched
     * @return Task that resolves in the request document
     */
    public static Task<DocumentSnapshot> getRequest(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .get();
    }

    /**
     * Deletes a request document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID to be deleted
     * @return Empty task that resolves successfully if the document was deleted
     */
    public static Task<Void> deleteRequest(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .delete();
    }

    /**
     * Sets an association request document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to set the request to
     * @param requestID     Request document ID to be set
     * @param request       Document to be set under the provided id
     * @return Empty task that resolves successfully if the document was set
     */
    public static Task<Void> setRequest(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            Request request
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .set(request);
    }

    //--- Request Categories

    /**
     * Fetches all request categories from an association
     *
     * @param db Database reference
     * @param associationID Association document ID where to set the categories from
     * @return Task that resolves in all request categories documents
     */
    public static Task<QuerySnapshot> getAllRequestCategories(
            FirebaseFirestore db,
            String associationID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUEST_CATEGORIES_COLLECTION)
                .get();
    }

    //--- Request Support

    /**
     * Fetches all request support documents
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request ID where to get the supports from
     * @return Task that resolves in all request support documents
     */
    public static Task<QuerySnapshot> getAllRequestSupports(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.SUPPORTS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single request support document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request ID where to get the support from
     * @param supportID     Support document ID to be fetched
     * @return Task that resolves in the request document
     */
    public static Task<DocumentSnapshot> getRequestSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    /**
     * Sets a request support document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID to be supported
     * @param supportID     Support document ID to be set
     * @param support       Support document to be set under the support ID provided
     * @return Empty task that resolves successfully if the document was set
     */
    public static Task<Void> setRequestSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID,
            final AssociationSupport support
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID);

        final DocumentReference supportRef = requestRef
                .collection(Request.SUPPORTS_COLLECTION)
                .document(supportID);

        // Set the request support and increment request score
        return db.runTransaction(transaction -> {
            setRequestSupportWithinTransaction(
                    transaction,
                    requestRef,
                    supportRef,
                    support
            );
            return null;
        });
    }

    /**
     * Deletes a request support
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to
     * @param supportID     Support document ID to deleted
     * @return Empty task that resolves successfully if the document was deleted
     */
    public static Task<Void> deleteRequestSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String supportID
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID);

        final DocumentReference supportRef = requestRef
                .collection(Request.SUPPORTS_COLLECTION)
                .document(supportID);

        // Delete request support and decrement request score
        return db.runTransaction(transaction -> {
            removeRequestSupportWithinTransaction(
                    transaction,
                    requestRef,
                    supportRef
            );
            return null;
        });
    }

    /**
     * Sets a request support document using the provided transaction
     *
     * @param transaction Transaction used to set the support
     * @param requestRef Request being supported
     * @param supportRef New support reference
     * @param support Support document to be set under the provided support ID
     * @throws FirebaseFirestoreException Thrown when the request document doesn't exist
     */
    public static void setRequestSupportWithinTransaction(
            Transaction transaction,
            DocumentReference requestRef,
            DocumentReference supportRef,
            AssociationSupport support
    ) throws FirebaseFirestoreException {
        // Get and update request score
        DocumentSnapshot requestSnap = transaction.get(requestRef);
        Double newScore = requestSnap.getDouble(Request.SCORE_FIELD);
        newScore += Request.SUPPORT_ACTION_VALUE;
        transaction.update(requestRef, Request.SCORE_FIELD, newScore);

        // Set request support
        transaction.set(supportRef, support);
    }

    /**
     * Deletes a request support using the provided transaction
     *
     * @param transaction Transaction used to set the support
     * @param requestRef Request being unsupported
     * @param supportRef Support to be removed
     * @throws FirebaseFirestoreException Thrown when the request document doesn't exist
     */
    public static void removeRequestSupportWithinTransaction(
            Transaction transaction,
            DocumentReference requestRef,
            DocumentReference supportRef
    ) throws FirebaseFirestoreException {
        // Get and update request score
        DocumentSnapshot requestSnap = transaction.get(requestRef);
        Double newScore = requestSnap.getDouble(Request.SCORE_FIELD);
        newScore -= Request.SUPPORT_ACTION_VALUE;
        transaction.update(requestRef, Request.SCORE_FIELD, newScore);

        // Delete request support
        transaction.delete(supportRef);
    }

    /**
     * Adds/Subtracts points from the request score
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID who score will be changed
     * @param points        Points to be added to the request score
     * @return Empty task that resolves successfully if the score was changed
     */
    public static Task<Void> addToRequestScore(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            final int points
    ) {
        final DocumentReference requestRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID);

        // Get and update request score
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot requestSnap = transaction.get(requestRef);
                Double newScore = requestSnap.getDouble(Request.SCORE_FIELD) + points;
                transaction.update(requestRef, Request.SCORE_FIELD, newScore);
                return null;
            }
        });
    }

    /**
     * Adds points to the request score
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID who score will be incremented
     * @param incrementBy   Increment size
     * @return Empty task that resolves successfully if the score was incremented
     */
    public static Task<Void> incrementRequestScore(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            final int incrementBy
    ) {
        return addToRequestScore(db, associationID, requestID, incrementBy);
    }


    /**
     * Removes points from the request score
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID who score will be decremented
     * @param decrementBy   Decrement size
     * @return Empty task that resolves successfully if the score was decremented
     */
    public static Task<Void> decrementRequestScore(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            final int decrementBy
    ) {
        return addToRequestScore(db, associationID, requestID, decrementBy * -1);
    }

    //--- Request Comment

    /**
     * Fetches all request comments
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comments from
     * @return Task that resolves in all request comment documents
     */
    public static Task<QuerySnapshot> getAllRequestComments(
            FirebaseFirestore db,
            String associationID,
            String requestID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID to be fetched
     * @return Task that resolves in the request comment document
     */
    public static Task<DocumentSnapshot> getRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .get();
    }

    /**
     * Sets a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID
     * @param comment       Comment document to be set under the comment ID provided
     * @return Empty task that resolves successfully if the document was set
     */
    public static Task<Void> setRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            AssociationComment comment
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .set(comment);
    }

    /**
     * Deletes a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID to be deleted
     * @return Empty task that resolves successfully if the document was deleted
     */
    public static Task<Void> deleteRequestComment(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .delete();
    }

    //--- Support Request Comment

    /**
     * Gets a support from a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID where to get the supports from
     * @param supportID     Support document ID where to get the document
     * @return Task that resolves in all support documents from a request comment
     */
    public static Task<DocumentSnapshot> getRequestCommentSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID)
                .get();
    }

    /**
     * Gets all supports from a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID where to get the supports from
     * @return Task that resolves in all support documents from a request comment
     */
    public static Task<QuerySnapshot> getAllRequestCommentSupports(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID)
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .get();
    }

    /**
     * Sets a support on a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID where to set the support to
     * @param supportID     Support document ID to be set
     * @param support       Document to be set under the provided support ID
     * @return Empty task that resolves successfully if document was set
     */
    public static Task<Void> setRequestCommentSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID,
            final AssociationSupport support
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID);

        // Set the comment support and increment the comment score
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update comment score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                Double newScore = commentSnap.getDouble(AssociationComment.SCORE_FIELD);
                newScore += AssociationSupport.SUPPORT_ACTION_VALUE;
                transaction.update(commentRef, AssociationComment.SCORE_FIELD, newScore);

                // Set comment support
                transaction.set(supportRef, support);

                return null;
            }
        });
    }

    /**
     * Deletes a support from a request comment
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the request from
     * @param requestID     Request document ID where to get the comment from
     * @param commentID     Comment document ID where to set the support to
     * @param supportID     Support document ID to be deleted
     * @return Empty task that resolves successfully if document was deleted
     */
    public static Task<Void> deleteRequestCommentSupport(
            FirebaseFirestore db,
            String associationID,
            String requestID,
            String commentID,
            String supportID
    ) {
        final DocumentReference commentRef = db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.REQUESTS_COLLECTION)
                .document(requestID)
                .collection(Request.COMMENTS_COLLECTION)
                .document(commentID);
        final DocumentReference supportRef = commentRef
                .collection(AssociationComment.SUPPORTS_COLLECTION)
                .document(supportID);

        // Delete the comment support and decrement the comment score
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // Get and update comment score
                DocumentSnapshot commentSnap = transaction.get(commentRef);
                Double newScore = commentSnap.getDouble(AssociationComment.SCORE_FIELD);
                newScore -= AssociationSupport.SUPPORT_ACTION_VALUE;
                transaction.update(commentRef, AssociationComment.SCORE_FIELD, newScore);

                // Remove request comment support
                transaction.delete(supportRef);

                return null;
            }
        });
    }

    //--- Session

    /**
     * Fetches all sessions
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the sessions from
     * @return Task that resolves in all session documents
     */
    public static Task<QuerySnapshot> getAllSessions(
            FirebaseFirestore db,
            String associationID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single session document
     *
     * @param db            Database reference
     * @param associationID Association document ID where the get the session from
     * @param sessionID     Session document ID to be fetched
     * @return Tasks that resolves in the session document
     */
    public static Task<DocumentSnapshot> getSession(
            FirebaseFirestore db,
            String associationID,
            String sessionID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .get();
    }

    /**
     * Sets a session
     *
     * @param db            Database reference
     * @param associationID Association document ID where to set the session to
     * @param sessionID     Session document ID to be set
     * @param session       Document to be set under the provided ID
     * @return Empty task that resolves successfully if the document was set
     */
    public static Task<Void> setSession(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            Session session
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .set(session);
    }

    /**
     * Deletes a session
     *
     * @param db            Database reference
     * @param associationID Association document ID where to delete the session from
     * @param sessionID     Session document ID to be deleted
     * @return Empty task that resolves successfully if the document was deleted
     */
    public static Task<Void> deleteSession(
            FirebaseFirestore db,
            String associationID,
            String sessionID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .delete();
    }

    //--- Agenda

    /**
     * Fetches all agendas
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document id where to get the agendas from
     * @return Task that resolves in all agenda documents
     */
    public static Task<QuerySnapshot> getAllAgendas(
            FirebaseFirestore db,
            String associationID,
            String sessionID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single agenda document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document ID where to get the agenda from
     * @param agendaID      Agenda document ID to be fetched
     * @return Task that resolves in the agenda document
     */
    public static Task<DocumentSnapshot> getAgenda(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .get();
    }

    /**
     * Sets an agenda document
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document ID where to set the agenda to
     * @param agendaID      Agenda document ID to be set
     * @param agenda        Document to be set under the provided ID
     * @return Empty task that resolves successfully if the document was set
     */
    public static Task<Void> setAgenda(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID,
            Agenda agenda
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .set(agenda);
    }

    /**
     * Deletes an agenda
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document ID where to delete the agenda from
     * @param agendaID      Agenda document ID to be deleted
     * @return Empty task that resolves successfully if the document was deleted
     */
    public static Task<Void> deleteAgenda(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .delete();
    }

    /**
     * Fetches all the questions from the agenda
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document ID where to get the agenda from
     * @param agendaID      Agenda document ID where to get the questions from
     * @return Task that resolves in all the question documents
     */
    public static Task<QuerySnapshot> getAllAgendaQuestions(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .collection(Agenda.QUESTIONS_COLLECTION)
                .get();
    }

    /**
     * Fetches a single question document from the agenda
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document ID where to get the agenda from
     * @param agendaID      Agenda document ID where to get the question from
     * @param questionID    Question document ID to be fetched
     * @return Task that resolves in the question document
     */
    public static Task<DocumentSnapshot> getAgendaQuestion(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID,
            String questionID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .collection(Agenda.QUESTIONS_COLLECTION)
                .document(questionID)
                .get();
    }

    /**
     * Sets a question to an agenda
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get the session from
     * @param sessionID     Session document ID where to get the agenda from
     * @param agendaID      Agenda document ID where to set de question to
     * @param questionID    Question document ID to be set
     * @param question      Question document to be set under the provided ID
     * @return Empty task that resolves successfully if the document was set
     */
    public static Task<Void> setAgendaQuestion(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID,
            String questionID,
            Question question
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .collection(Agenda.QUESTIONS_COLLECTION)
                .document(questionID)
                .set(question);
    }

    /**
     * Deletes a question from an agenda
     *
     * @param db            Database reference
     * @param associationID Association document ID where to get que session from
     * @param sessionID     Session document ID where to get the agenda from
     * @param agendaID      Agenda document ID where to delete the question from
     * @param questionID    Question document ID to be deleted
     * @return Empty task that resolves successfully if the document was delete
     */
    public static Task<Void> deleteAgendaQuestion(
            FirebaseFirestore db,
            String associationID,
            String sessionID,
            String agendaID,
            String questionID
    ) {
        return db
                .collection(ASSOCIATION_COLLECTION)
                .document(associationID)
                .collection(Association.SESSIONS_COLLECTION)
                .document(sessionID)
                .collection(Session.AGENDAS_COLLECTION)
                .document(agendaID)
                .collection(Agenda.QUESTIONS_COLLECTION)
                .document(questionID)
                .delete();
    }
}
