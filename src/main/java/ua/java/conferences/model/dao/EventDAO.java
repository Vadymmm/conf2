package ua.java.conferences.model.dao;

import ua.java.conferences.model.entities.Event;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.DAOException;

import java.util.*;

/**
 * Event DAO interface.
 * Implement methods due to database type
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public interface EventDAO extends EntityDAO<Event> {

    /**
     * Obtains instance of event from database
     * @param title - value of title
     * @return Optional.ofNullable - event is null if there is no event
     * @throws DAOException is wrapper for SQLException
     */
    Optional<Event> getByTitle(String title) throws DAOException;

    /**
     * Obtains sorted and limited list of events from database
     * @param query should contain filters, order, limits for pagination
     * @return events list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    List<Event> getSorted(String query) throws DAOException;

    /**
     * Obtains sorted and limited list of events from database
     * @param query should contain filters, order, limits for pagination
     * @param role should be either VISITOR or SPEAKER
     * @return events list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    List<Event> getSortedByUser(String query, Role role) throws DAOException;

    /**
     * Obtains number of all records matching filter
     * @param filter should contain 'where' to specify query
     * @param role can be VISITOR, SPEAKER or any other
     * @return number of records
     * @throws DAOException is wrapper for SQLException
     */
    int getNumberOfRecords(String filter, Role role) throws DAOException;

    /**
     * Updates event's visitors field
     * @param eventId - value of id field
     * @param visitorsCount - value of visitor field in database
     * @throws DAOException is wrapper for SQLException
     */
    void setVisitorsCount(long eventId, int visitorsCount) throws DAOException;
}