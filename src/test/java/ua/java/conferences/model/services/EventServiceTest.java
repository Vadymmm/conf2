package ua.java.conferences.model.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.java.conferences.model.dao.EventDAO;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.model.entities.Event;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.implementation.EventServiceImpl;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.exceptions.constants.Message.*;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

class EventServiceTest {

    private final EventDAO eventDAO = mock(EventDAO.class);

    private final EventService eventService = new EventServiceImpl(eventDAO);

    @Test
    void testAddEvent() throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        assertDoesNotThrow(() -> eventService.addEvent(getTestEventDTO()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "ё", "11111111111111111111111111111111111111111111111111111111111111111111111"})
    void testAddIncorrectTitle(String title) throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setTitle(title);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.addEvent(eventDTO));
        assertEquals(ENTER_CORRECT_TITLE, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testAddNullEmptyTitle(String title) throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setTitle(title);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.addEvent(eventDTO));
        assertEquals(ENTER_CORRECT_TITLE, e.getMessage());
    }

    @Test
    void testAddIncorrectDate() throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setDate(INCORRECT_DATE_NAME);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.addEvent(eventDTO));
        assertEquals(ENTER_VALID_DATE, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "ё", "11111111111111111111111111111111111111111111111111111111111111111111111"})
    void testAddIncorrectLocation(String location) throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setLocation(location);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.addEvent(eventDTO));
        assertEquals(ENTER_CORRECT_LOCATION, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testAddNullEmptyLocation(String location) throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setLocation(location);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.addEvent(eventDTO));
        assertEquals(ENTER_CORRECT_LOCATION, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testAddIncorrectDescription(String description) throws DAOException {
        doNothing().when(eventDAO).add(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setDescription(description);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.addEvent(eventDTO));
        assertEquals(ENTER_CORRECT_DESCRIPTION, e.getMessage());
    }

    @Test
    void testAddDuplicateTitle() throws DAOException {
        doThrow(new DAOException(new SQLException("Duplicate entry"))).when(eventDAO).add(isA(Event.class));
        assertThrows(DuplicateTitleException.class, () -> eventService.addEvent(getTestEventDTO()));
    }

    @Test
    void testAddDbIsDown() throws DAOException {
        doThrow(new DAOException(new SQLException())).when(eventDAO).add(isA(Event.class));
        ServiceException e = assertThrows(ServiceException.class , () -> eventService.addEvent(getTestEventDTO()));
        assertFalse(e.getMessage().contains("Duplicate entry"));
    }

    @Test
    void testGetById() throws DAOException, ServiceException {
        when(eventDAO.getById(ID_VALUE)).thenReturn(Optional.of(getTestEvent()));
        assertEquals(getTestEventDTO(), eventService.getById(String.valueOf(ID_VALUE)));
    }

    @Test
    void testSQLErrorGetById() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).getById(isA(long.class));
        assertThrows(ServiceException.class, () -> eventService.getById(String.valueOf(ID_VALUE)));
    }

    @Test
    void testGetByIdNoEvent() throws DAOException {
        when(eventDAO.getById(ID_VALUE)).thenReturn(Optional.empty());
        assertThrows(NoSuchEventException.class,() -> eventService.getById(String.valueOf(ID_VALUE)));
    }

    @Test
    void testGetByTitle() throws DAOException, ServiceException {
        when(eventDAO.getByTitle(TITLE_VALUE)).thenReturn(Optional.of(getTestEvent()));
        assertEquals(getTestEventDTO(), eventService.getByTitle(TITLE_VALUE));
    }

    @Test
    void testSQLErrorGetByTitle() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).getByTitle(isA(String.class));
        assertThrows(ServiceException.class, () -> eventService.getByTitle(TITLE_VALUE));
    }

    @Test
    void testGetByTitleNoEvent() throws DAOException {
        when(eventDAO.getByTitle(TITLE_VALUE)).thenReturn(Optional.empty());
        assertThrows(NoSuchEventException.class,() -> eventService.getByTitle(TITLE_VALUE));
    }

    @Test
    void testGetAll() throws DAOException, ServiceException {
        when(eventDAO.getAll()).thenReturn(List.of(getTestEvent()));
        assertIterableEquals(List.of(getTestEventDTO()), eventService.getAll());
    }

    @Test
    void testSQLErrorGetAll() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).getAll();
        assertThrows(ServiceException.class, eventService::getAll);
    }

    @Test
    void testGetSorted() throws DAOException, ServiceException {
        List<Event> events = new ArrayList<>();
        List<EventDTO> eventDTOs = new ArrayList<>();
        events.add(getTestEvent());
        eventDTOs.add(getTestEventDTO());
        String query = eventQueryBuilder().getQuery();
        when(eventDAO.getSorted(query)).thenReturn(events);
        assertIterableEquals(eventDTOs, eventService.getSorted(query));
    }

    @Test
    void testSQLErrorGetSorted() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).getSorted(isA(String.class));
        assertThrows(ServiceException.class, () -> eventService.getSorted(TITLE_VALUE));
    }

    @Test
    void testGetSortedByUser() throws DAOException, ServiceException {
        List<Event> events = new ArrayList<>();
        List<EventDTO> eventDTOs = new ArrayList<>();
        events.add(getTestEvent());
        eventDTOs.add(getTestShortEventDTO());
        String query = visitorEventQueryBuilder().getQuery();
        when(eventDAO.getSortedByUser(query, Role.VISITOR)).thenReturn(events);
        assertIterableEquals(eventDTOs, eventService.getSortedByUser(query, Role.VISITOR));
    }

    @Test
    void testSQLErrorGetSortedByUser() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).getSortedByUser(isA(String.class), isA(Role.class));
        assertThrows(ServiceException.class, () -> eventService.getSortedByUser("query", Role.VISITOR));
    }

    @Test
    void testGetNumberOfRecords() throws DAOException, ServiceException {
        String filter = visitorEventQueryBuilder().getRecordQuery();
        when(eventDAO.getNumberOfRecords(filter,  Role.MODERATOR)).thenReturn(TEN);
        assertEquals(TEN, eventService.getNumberOfRecords(filter, Role.MODERATOR));
    }

    @Test
    void testSQLErrorGetNumberOfRecords() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).getNumberOfRecords(isA(String.class), isA(Role.class));
        assertThrows(ServiceException.class, () -> eventService.getNumberOfRecords("filter", Role.MODERATOR));
    }

    @Test
    void testGetNumberOfRecordsByUser() throws DAOException, ServiceException {
        String filter = visitorEventQueryBuilder().getRecordQuery();
        when(eventDAO.getNumberOfRecords(filter,  Role.VISITOR)).thenReturn(THREE);
        assertEquals(THREE, eventService.getNumberOfRecords(filter,  Role.VISITOR));
    }

    @Test
    void testUpdate() throws DAOException {
        doNothing().when(eventDAO).update(isA(Event.class));
        assertDoesNotThrow(() -> eventService.update(getTestEventDTO()));
    }

    @Test
    void testSQLErrorUpdate() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).update(isA(Event.class));
        assertThrows(ServiceException.class, () -> eventService.update(getTestEventDTO()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "ё", "11111111111111111111111111111111111111111111111111111111111111111111111"})
    void testUpdateIncorrectTitle(String title) throws DAOException {
        doNothing().when(eventDAO).update(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setTitle(title);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.update(eventDTO));
        assertEquals(ENTER_CORRECT_TITLE, e.getMessage());
    }

    @Test
    void testUpdateIncorrectDate() throws DAOException {
        doNothing().when(eventDAO).update(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setDate(INCORRECT_DATE_NAME);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.update(eventDTO));
        assertEquals(ENTER_VALID_DATE, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "ё", "11111111111111111111111111111111111111111111111111111111111111111111111"})
    void testUpdateIncorrectLocation(String location) throws DAOException {
        doNothing().when(eventDAO).update(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setLocation(location);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.update(eventDTO));
        assertEquals(ENTER_CORRECT_LOCATION, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ё"})
    void testUpdateIncorrectDescription(String description) throws DAOException {
        doNothing().when(eventDAO).update(isA(Event.class));
        EventDTO eventDTO = getTestEventDTO();
        eventDTO.setDescription(description);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class , () -> eventService.update(eventDTO));
        assertEquals(ENTER_CORRECT_DESCRIPTION, e.getMessage());
    }

    @Test
    void testUpdateDuplicateTitle() throws DAOException {
        doThrow(new DAOException(new SQLException("Duplicate entry"))).when(eventDAO).update(isA(Event.class));
        assertThrows(DuplicateTitleException.class, () -> eventService.update(getTestEventDTO()));
    }

    @Test
    void testSetVisitorsCount() throws DAOException {
        doNothing().when(eventDAO).setVisitorsCount(isA(long.class), isA(int.class));
        assertDoesNotThrow(() -> eventService.setVisitorsCount(String.valueOf(ID_VALUE), String.valueOf(VISITORS_VALUE)));
    }

    @Test
    void testSQLErrorSetVisitorsCount() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).setVisitorsCount(isA(long.class), isA(int.class));
        assertThrows(ServiceException.class,
                () -> eventService.setVisitorsCount(String.valueOf(ID_VALUE), String.valueOf(VISITORS_VALUE)));
    }

    @Test
    void testSetVisitorsCountWrongEventId() throws DAOException {
        doNothing().when(eventDAO).setVisitorsCount(isA(long.class), isA(int.class));
        assertThrows(NoSuchEventException.class, () -> eventService.setVisitorsCount(NAME_VALUE, String.valueOf(VISITORS_VALUE)));
    }

    @Test
    void testSetVisitorsCountWrongNumber() throws DAOException {
        doNothing().when(eventDAO).setVisitorsCount(isA(long.class), isA(int.class));
        assertThrows(ServiceException.class, () -> eventService.setVisitorsCount(String.valueOf(ID_VALUE), NAME_VALUE));
    }

    @Test
    void testDeleteEvent() throws DAOException {
        doNothing().when(eventDAO).delete(isA(long.class));
        assertDoesNotThrow(() -> eventService.delete(String.valueOf(ID_VALUE)));
    }

    @Test
    void testSQLErrorDeleteEvent() throws DAOException {
        doThrow(DAOException.class).when(eventDAO).delete(isA(long.class));
        assertThrows(ServiceException.class, () -> eventService.delete(String.valueOf(ID_VALUE)));
    }

    private EventDTO getTestShortEventDTO() {
        return EventDTO.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_NAME)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .build();
    }

    private EventDTO getTestEventDTO() {
        return EventDTO.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_NAME)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .reports(REPORTS_VALUE)
                .registrations(REGISTRATIONS_VALUE)
                .visitors(VISITORS_VALUE)
                .build();
    }

    private Event getTestEvent() {
        return Event.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_VALUE)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .reports(REPORTS_VALUE)
                .registrations(REGISTRATIONS_VALUE)
                .visitors(VISITORS_VALUE)
                .build();
    }
}