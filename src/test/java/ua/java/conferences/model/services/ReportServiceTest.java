package ua.java.conferences.model.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.java.conferences.model.dao.ReportDAO;
import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.*;
import ua.java.conferences.model.services.implementation.ReportServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.exceptions.constants.Message.*;

class ReportServiceTest {

    private final ReportDAO reportDAO = mock(ReportDAO.class);

    private final ReportService reportService = new ReportServiceImpl(reportDAO);

    @Test
    void testCreateReport() throws DAOException {
        doNothing().when(reportDAO).add(isA(Report.class));
        assertDoesNotThrow(() -> reportService.addReport(getReportDTO()));
    }

    @Test
    void testSQLErrorCreateReport() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).add(isA(Report.class));
        assertThrows(ServiceException.class, () -> reportService.addReport(getReportDTO()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "ё", "11111111111111111111111111111111111111111111111111111111111111111111111"})
    void testCreateIncorrectTopic(String topic) throws DAOException {
        doNothing().when(reportDAO).add(isA(Report.class));
        ReportDTO reportDTO = getReportDTO();
        reportDTO.setTopic(topic);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class, () -> reportService.addReport(reportDTO));
        assertEquals(ENTER_CORRECT_TOPIC, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testCreateNullTopic(String topic) throws DAOException {
        doNothing().when(reportDAO).add(isA(Report.class));
        ReportDTO reportDTO = getReportDTO();
        reportDTO.setTopic(topic);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class, () -> reportService.addReport(reportDTO));
        assertEquals(ENTER_CORRECT_TOPIC, e.getMessage());
    }

    @Test
    void testViewReport() throws DAOException, ServiceException {
        when(reportDAO.getById(ID_VALUE)).thenReturn(Optional.of(getTestReport()));
        assertEquals(getReportDTO(), reportService.getById(String.valueOf(ID_VALUE)));
    }

    @Test
    void testSQLErrorViewReport() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).getById(isA(long.class));
        assertThrows(ServiceException.class, () -> reportService.getById(String.valueOf(ID_VALUE)));
    }

    @Test
    void testViewNoReport() throws DAOException {
        when(reportDAO.getById(ID_VALUE)).thenReturn(Optional.empty());
        assertThrows(NoSuchReportException.class, () -> reportService.getById(String.valueOf(ID_VALUE)));
    }

    @Test
    void testGetAll() throws DAOException, ServiceException {
        List<Report> reports = new ArrayList<>();
        List<ReportDTO> reportDTOs = new ArrayList<>();
        reports.add(getTestReport());
        reportDTOs.add(getReportDTO());
        when(reportDAO.getAll()).thenReturn(reports);
        assertIterableEquals(reportDTOs, reportService.getAll());
    }

    @Test
    void testSQLErrorGetAll() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).getAll();
        assertThrows(ServiceException.class, reportService::getAll);
    }

    @Test
    void testViewEventsReports() throws DAOException, ServiceException {
        List<Report> reports = new ArrayList<>();
        List<ReportDTO> reportDTOs = new ArrayList<>();
        reports.add(getTestReport());
        reportDTOs.add(getReportDTO());
        when(reportDAO.getEventsReports(ID_VALUE)).thenReturn(reports);
        assertIterableEquals(reportDTOs, reportService.viewEventsReports(String.valueOf(ID_VALUE)));
    }

    @Test
    void testSQLErrorViewEventsReports() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).getEventsReports(isA(long.class));
        assertThrows(ServiceException.class, () -> reportService.viewEventsReports(String.valueOf(ID_VALUE)));
    }

    @Test
    void testViewSpeakersReports() throws DAOException, ServiceException {
        List<Report> reports = new ArrayList<>();
        List<ReportDTO> reportDTOs = new ArrayList<>();
        reports.add(getTestReport());
        reportDTOs.add(getReportDTO());
        when(reportDAO.getSpeakersReports(ID_VALUE)).thenReturn(reports);
        assertIterableEquals(reportDTOs, reportService.viewSpeakersReports(ID_VALUE));
    }

    @Test
    void testSQLErrorViewSpeakersReports() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).getSpeakersReports(isA(long.class));
        assertThrows(ServiceException.class, () -> reportService.viewSpeakersReports(ID_VALUE));
    }

    @Test
    void testUpdateReport() throws DAOException {
        doNothing().when(reportDAO).update(isA(Report.class));
        assertDoesNotThrow(() -> reportService.update(getReportDTO()));
    }

    @Test
    void testSQLErrorUpdateReport() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).update(isA(Report.class));
        assertThrows(ServiceException.class, () -> reportService.update(getReportDTO()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "ё", "11111111111111111111111111111111111111111111111111111111111111111111111"})
    void testUpdateReportIncorrectTopic(String topic) throws DAOException {
        doNothing().when(reportDAO).update(isA(Report.class));
        ReportDTO reportDTO = getReportDTO();
        reportDTO.setTopic(topic);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class, () -> reportService.update(reportDTO));
        assertEquals(ENTER_CORRECT_TOPIC, e.getMessage());
    }

    @Test
    void testSetSpeaker() throws DAOException, ServiceException {
        when(reportDAO.setSpeaker(isA(long.class), isA(long.class))).thenReturn(true);
        assertTrue(reportService.setSpeaker(ID_VALUE, ID_VALUE));
    }

    @Test
    void testSQLErrorSetSpeaker() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).setSpeaker(isA(long.class), isA(long.class));
        assertThrows(ServiceException.class, () -> reportService.setSpeaker(ID_VALUE, ID_VALUE));
    }

    @Test
    void testDeleteSpeaker() throws DAOException {
        doNothing().when(reportDAO).deleteSpeaker(isA(long.class));
        assertDoesNotThrow(() -> reportService.deleteSpeaker(ID_VALUE));
    }

    @Test
    void testSQLErrorDeleteSpeaker() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).deleteSpeaker(isA(long.class));
        assertThrows(ServiceException.class, () -> reportService.deleteSpeaker(ID_VALUE));
    }

    @Test
    void testDeleteReport() throws DAOException {
        doNothing().when(reportDAO).delete(isA(long.class));
        assertDoesNotThrow(() -> reportService.delete(String.valueOf(ID_VALUE)));
    }

    @Test
    void testSQLErrorDeleteReport() throws DAOException {
        doThrow(DAOException.class).when(reportDAO).delete(isA(long.class));
        assertThrows(ServiceException.class, () -> reportService.delete(String.valueOf(ID_VALUE)));
    }

    private ReportDTO getReportDTO() {
        return ReportDTO.builder()
                .id(ID_VALUE)
                .topic(TOPIC_VALUE)
                .eventId(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_NAME)
                .location(LOCATION_VALUE)
                .build();
    }

    private Report getTestReport() {
        User speaker = User.builder()
                .id(ID_VALUE)
                .email(EMAIL_VALUE)
                .password(PASSWORD_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .build();
        Event event = Event.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_VALUE)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .build();
        return Report.builder()
                .id(ID_VALUE)
                .topic(TOPIC_VALUE)
                .event(event)
                .speaker(speaker)
                .build();
    }
}