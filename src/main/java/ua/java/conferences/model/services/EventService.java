package ua.java.conferences.model.services;

import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.ServiceException;

import java.util.List;

/**
 * EventService interface.
 * Implement all methods in concrete EventService
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public interface EventService extends Service<EventDTO> {

    /**
     * Calls DAO to add relevant entity
     * @param eventDTO - DTO to be added as entity to database
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    void addEvent(EventDTO eventDTO) throws ServiceException;

    /**
     * Calls DAO to get relevant entity by title
     * @param title - event title to find
     * @return EventDTO relevant to Event entity
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    EventDTO getByTitle(String title) throws ServiceException;

    /**
     * Calls DAO to get sorted, filtered and limited list of DTOs
     * @param query - to obtain necessary DTOs
     * @return List of EventDTOs that match demands
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    List<EventDTO>  getSorted(String query) throws ServiceException;

    /**
     * Calls DAO to get sorted, filtered and limited list of DTOs where Visitor or Speaker participate
     * @param query - to obtain necessary DTOs
     * @param role - can be VISITOR or SPEAKER
     * @return List of EventDTOs that match demands
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    List<EventDTO> getSortedByUser(String query, Role role) throws ServiceException;

    /**
     * Calls DAO to get number of all records match filter
     * @param filter - conditions for such Events
     * @param role - can be VISITOR or SPEAKER or any other
     * @return number of records that match demands
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    int getNumberOfRecords(String filter, Role role) throws ServiceException;

    /**
     * Calls DAO to update Event visitor number
     * @param eventIdString - id as a String to validate and convert to long
     * @param visitorsCountString - visitors as a String to validate and convert to int
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    void setVisitorsCount(String eventIdString, String visitorsCountString) throws ServiceException;
}