package ua.java.conferences.model.dao;

import ua.java.conferences.model.entities.User;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.DAOException;

import java.util.*;

/**
 * User DAO interface.
 * Implement methods due to database type
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public interface UserDAO extends EntityDAO<User> {

    /**
     * Obtains instance of User from database by email
     * @param email - value of email
     * @return Optional.ofNullable - user is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    Optional<User> getByEmail(String email) throws DAOException;

    /**
     * Obtains sorted and limited list of users from database
     * @param query should contain filters, order, limits for pagination
     * @return users list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    List<User> getSorted(String query) throws DAOException;

    /**
     * Obtains list of users that registered for the event from database
     * @param eventId - value of event id
     * @param role can be either VISITOR or SPEAKER
     * @return users list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    List<User> getParticipants(long eventId, Role role) throws DAOException;

    /**
     * Obtains number of all records matching filter
     * @param filter should contain 'where' to specify query
     * @return number of records
     * @throws DAOException is wrapper for SQLException
     */
    int getNumberOfRecords(String filter) throws DAOException;

    /**
     * Updates user's password
     * @param user should contain user id and new password
     * @throws DAOException is wrapper for SQLException
     */
    void updatePassword(User user) throws DAOException;

    /**
     * Sets new user's role
     * @param userEmail - value of email field in database
     * @param role - new role for user
     * @throws DAOException is wrapper for SQLException
     */
    void setUserRole(String userEmail, Role role) throws DAOException;

    /**
     * Inserts new record for user_has_event table
     * @param userId - value of user id
     * @param eventId - value of event id
     * @throws DAOException is wrapper for SQLException
     */
    void registerForEvent(long userId, long eventId) throws DAOException;

    /**
     * Deletes record in user_has_event table
     * @param userId - value of user id
     * @param eventId - value of event id
     * @throws DAOException is wrapper for SQLException
     */
    void cancelRegistration(long userId, long eventId) throws DAOException;

    /**
     * Checks if record exists in user_has_event table
     * @param userId - value of user id
     * @param eventId - value of event id
     * @return true id user registered for event
     * @throws DAOException is wrapper for SQLException
     */
    boolean isRegistered(long userId, long eventId) throws DAOException;
}