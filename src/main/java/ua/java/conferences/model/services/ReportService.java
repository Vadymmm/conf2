package ua.java.conferences.model.services;

import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.exceptions.*;

import java.util.List;

/**
 * ReportService interface.
 * Implement all methods in concrete ReportService
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public interface ReportService extends Service<ReportDTO> {

    /**
     * Calls DAO to add relevant entity
     * @param reportDTO - DTO to be added as entity to database
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    void addReport(ReportDTO reportDTO) throws ServiceException;

    /**
     * Calls DAO to get events reports
     * @param eventIdString - id as a String to validate and convert to long
     * @return List of ReportDTO for this event
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    List<ReportDTO> viewEventsReports(String eventIdString) throws ServiceException;

    /**
     * Calls DAO to get speakers reports
     * @param speakerId - id to get reports
     * @return List of ReportDTO for this speaker
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    List<ReportDTO> viewSpeakersReports(long speakerId) throws ServiceException;

    /**
     * Calls DAO to set speaker to report
     * @param reportId - report id
     * @param speakerId - speaker id
     * @return true if it set new speaker
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    boolean setSpeaker(long reportId, long speakerId) throws ServiceException;

    /**
     * Calls DAO to delete speaker from report
     * @param reportId - report id
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    void deleteSpeaker(long reportId) throws ServiceException;
}