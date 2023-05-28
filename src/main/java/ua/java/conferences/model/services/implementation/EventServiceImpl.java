package ua.java.conferences.model.services.implementation;

import lombok.RequiredArgsConstructor;
import ua.java.conferences.model.dao.EventDAO;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.model.entities.Event;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.EventService;

import java.time.LocalDate;
import java.util.*;

import static ua.java.conferences.exceptions.constants.Message.*;
import static ua.java.conferences.utils.ConvertorUtil.*;
import static ua.java.conferences.utils.ValidatorUtil.*;

/**
 * Implementation of EventService interface.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    /** Contains eventDAO field to work with EventDAO */
    private final EventDAO eventDAO;

    /**
     * Gets eventDTO from action and calls DAO to add relevant entity. Validate event's fields.
     * Converts EventDTO to Event
     * @param eventDTO - DTO to be added as Event to database
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException with specific message,
     * DuplicateTitleException.
     */
    @Override
    public void addEvent(EventDTO eventDTO) throws ServiceException {
        validateEvent(eventDTO);
        Event event = convertDTOToEvent(eventDTO);
        try {
            eventDAO.add(event);
        } catch (DAOException e) {
            checkExceptionType(e);
        }
    }

    /**
     * Obtains instance of Event from DAO by id. Checks if id valid. Converts Event to EventDTO
     * @param eventIdString - id as a String
     * @return EventDTO instance
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchEventException
     */
    @Override
    public EventDTO getById(String eventIdString) throws ServiceException {
        long eventId = getEventId(eventIdString);
        EventDTO eventDTO;
        try {
            Event event = eventDAO.getById(eventId).orElseThrow(NoSuchEventException::new);
            eventDTO = convertEventToDTO(event);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return eventDTO;
    }

    /**
     * Obtains instance of Event from DAO by title. Checks if id valid. Converts Event to EventDTO
     * @param title - Event title
     * @return EventDTO instance
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchEventException
     */
    @Override
    public EventDTO getByTitle(String title) throws ServiceException {
        EventDTO eventDTO;
        try {
            Event event = eventDAO.getByTitle(title).orElseThrow(NoSuchEventException::new);
            eventDTO = convertEventToDTO(event);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return eventDTO;
    }

    /**
     * Obtains list of all instances of Event from DAO. Converts Events to  EventDTOs
     * @return List of EventDTOs
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<EventDTO> getAll() throws ServiceException {
        List<EventDTO> eventDTOS = new ArrayList<>();
        try {
            List<Event> events = eventDAO.getAll();
            events.forEach(event -> eventDTOS.add(convertEventToDTO(event)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return eventDTOS;
    }

    /**
     * Calls DAO to get sorted, filtered and limited list of DTOs. Converts Events to  EventDTOs
     * @param query - to obtain necessary DTOs
     * @return List of EventDTOs that match demands
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<EventDTO> getSorted(String query) throws ServiceException {
        List<EventDTO> eventDTOS = new ArrayList<>();
        try {
            List<Event> events = eventDAO.getSorted(query);
            events.forEach(event -> eventDTOS.add(convertEventToDTO(event)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return eventDTOS;
    }

    /**
     * Calls DAO to get sorted, filtered and limited list of DTOs where Visitor or Speaker participate
     * @param query - to obtain necessary DTOs
     * @param role - can be VISITOR or SPEAKER
     * @return List of EventDTOs that match demands
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<EventDTO> getSortedByUser(String query, Role role)
            throws ServiceException {
        List<EventDTO> eventDTOS = new ArrayList<>();
        try {
            List<Event> events = eventDAO.getSortedByUser(query, role);
            events.forEach(event -> eventDTOS.add(convertEventToDTO(event)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return eventDTOS;
    }

    /**
     * Calls DAO to get number of all records match filter
     * @param filter - conditions for such Events
     * @param role - can be VISITOR or SPEAKER or any other
     * @return number of records that match demands
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public int getNumberOfRecords(String filter, Role role) throws ServiceException {
        int records;
        try {
            records = eventDAO.getNumberOfRecords(filter, role);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return records;
    }

    /**
     * Updates Event's title, date, location, description. Validate EventDTO. Converts EventDTO to Event
     * @param dto - EventDTO that contains Event's id, title, date, location and description.
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException or
     * DuplicateTitleException
     */
    @Override
    public void update(EventDTO dto) throws ServiceException {
        validateEvent(dto);
        Event event = convertDTOToEvent(dto);
        try {
            eventDAO.update(event);
        } catch (DAOException e) {
            checkExceptionType(e);
        }
    }

    /**
     * Calls DAO to update Event visitor number. Check if id and visitors number are valid
     * @param eventIdString - id as a String
     * @param visitorsCountString - visitors as a String
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public void setVisitorsCount(String eventIdString, String visitorsCountString) throws ServiceException {
        long eventId = getEventId(eventIdString);
        int visitorsCount = getInt(visitorsCountString);
        try {
            eventDAO.setVisitorsCount(eventId, visitorsCount);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Deletes Event entity from database. Validate id.
     * @param eventIdString - id as a String
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchUserException
     */
    @Override
    public void delete(String eventIdString) throws ServiceException {
        long eventId = getEventId(eventIdString);
        try {
            eventDAO.delete(eventId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
    /**
     * Specify Service Exception
     * @param e - exception thrown by DAO
     * @throws ServiceException in case of general SQLException and DuplicateTitleException if title is already in use
     */
    private void checkExceptionType(DAOException e) throws ServiceException {
        if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
            throw new DuplicateTitleException();
        } else {
            throw new ServiceException(e);
        }
    }

    private void validateEvent(ua.java.conferences.dto.EventDTO eventDTO) throws IncorrectFormatException {
        validateComplexName(eventDTO.getTitle(), ENTER_CORRECT_TITLE);
        validateDate(LocalDate.parse(eventDTO.getDate()));
        validateComplexName(eventDTO.getLocation(), ENTER_CORRECT_LOCATION);
        validateDescription(eventDTO.getDescription());
    }
}