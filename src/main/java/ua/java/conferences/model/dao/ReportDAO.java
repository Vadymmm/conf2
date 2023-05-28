package ua.java.conferences.model.dao;

import ua.java.conferences.exceptions.DAOException;
import ua.java.conferences.model.entities.Report;

import java.util.*;

/**
 * Report DAO interface.
 * Implement methods due to database type
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public interface ReportDAO extends EntityDAO<Report> {

    /**
     * Obtains list of reports by concrete speakers
     * @param speakerId - value of user id
     * @return reports list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    List<Report> getSpeakersReports(long speakerId) throws DAOException;

    /**
     * Obtains list of reports by concrete event
     * @param eventId - value of event id
     * @return reports list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    List<Report> getEventsReports(long eventId) throws DAOException;

    /**
     * Updates user id for report
     * @param reportId - value of report id
     * @param speakerId - value of user id
     * @return true if it sets new speaker
     * @throws DAOException is wrapper for SQLException
     */
    boolean setSpeaker(long reportId, long speakerId) throws DAOException;

    /**
     * Set user id as null for report
     * @param reportId - value of report id
     * @throws DAOException is wrapper for SQLException
     */
    void deleteSpeaker(long reportId) throws DAOException;
}