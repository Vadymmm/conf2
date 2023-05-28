package ua.java.conferences.utils;

import org.junit.jupiter.api.Test;
import ua.java.conferences.dto.*;
import ua.java.conferences.model.entities.Event;
import ua.java.conferences.model.entities.Report;
import ua.java.conferences.model.entities.User;

import static org.junit.jupiter.api.Assertions.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.utils.ConvertorUtil.*;

class ConvertorUtilTest {

    @Test
    void testConvertDTOToUser() {
        User testUser = getTestUser();
        User dtoToUser = convertDTOToUser(getTestUserDTO());
        assertEquals(testUser, dtoToUser);
        assertNull(dtoToUser.getPassword());
        assertNotEquals(testUser.getPassword(), dtoToUser.getPassword());
    }

    @Test
    void testConvertUserToDTO() {
        UserDTO testDTO = getTestUserDTO();
        UserDTO userToDTO = convertUserToDTO(getTestUser());
        assertEquals(testDTO, userToDTO);
        assertEquals(testDTO.getId(), userToDTO.getId());
        assertEquals(testDTO.getRole(), userToDTO.getRole());
    }

    @Test
    void testConvertDTOToReport() {
        Report testReport = getTestReport();
        Report dtoToReport = convertDTOToReport(getTestReportDTO());
        assertEquals(testReport, dtoToReport);
        assertEquals(testReport.getEvent().getId(), dtoToReport.getEvent().getId());
        assertEquals(testReport.getSpeaker().getId(), dtoToReport.getSpeaker().getId());
    }

    @Test
    void testConvertReportToDTO() {
        ReportDTO testDTO = getTestReportDTO();
        ReportDTO reportToDTO = convertReportToDTO(getTestReport());
        assertEquals(testDTO, reportToDTO);
        assertEquals(testDTO.getEventId(), reportToDTO.getEventId());
        assertEquals(testDTO.getTitle(), reportToDTO.getTitle());
        assertEquals(testDTO.getDate(), reportToDTO.getDate());
        assertEquals(testDTO.getLocation(), reportToDTO.getLocation());
        assertEquals(testDTO.getSpeakerId(), reportToDTO.getSpeakerId());
        assertEquals(testDTO.getSpeakerEmail(), reportToDTO.getSpeakerEmail());
        assertEquals(testDTO.getSpeakerName(), reportToDTO.getSpeakerName());
    }

    @Test
    void testConvertDTOToEvent() {
        Event testEvent = getTestEvent();
        Event dtoToEvent = convertDTOToEvent(getTestEventDTO());
        assertEquals(testEvent, dtoToEvent);
        assertEquals(testEvent.getDate(), dtoToEvent.getDate());
        assertEquals(testEvent.getLocation(), dtoToEvent.getLocation());
        assertEquals(testEvent.getDescription(), dtoToEvent.getDescription());
        assertNotEquals(testEvent.getReports(), dtoToEvent.getReports());
        assertNotEquals(testEvent.getVisitors(), dtoToEvent.getVisitors());
        assertNotEquals(testEvent.getRegistrations(), dtoToEvent.getRegistrations());
    }

    @Test
    void  testConvertEventToDTO() {
        EventDTO testDTO = getTestEventDTO();
        EventDTO eventToDTO = convertEventToDTO(getTestEvent());
        assertEquals(testDTO, eventToDTO);
        assertEquals(testDTO.getId(), eventToDTO.getId());
        assertEquals(testDTO.getDescription(), eventToDTO.getDescription());
        assertEquals(testDTO.getReports(), eventToDTO.getReports());
        assertEquals(testDTO.getVisitors(), eventToDTO.getVisitors());
        assertEquals(testDTO.getRegistrations(), eventToDTO.getRegistrations());
    }

    private UserDTO getTestUserDTO() {
        return UserDTO.builder()
                .id(ID_VALUE)
                .email(EMAIL_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .role(ROLE_VISITOR)
                .build();
    }

    private ReportDTO getTestReportDTO() {
        return ReportDTO.builder()
                .id(ID_VALUE)
                .topic(TOPIC_VALUE)
                .speakerId(ID_VALUE)
                .speakerEmail(EMAIL_VALUE)
                .speakerName(SPEAKER_NAME)
                .eventId(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_NAME)
                .location(LOCATION_VALUE)
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

    private User getTestUser() {
        return User.builder()
                .id(ID_VALUE)
                .email(EMAIL_VALUE)
                .password(PASSWORD_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .roleId(ROLE_ID_VALUE)
                .build();
    }

    private Event getTestEvent() {
        return Event.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_VALUE)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .registrations(REGISTRATIONS_VALUE)
                .visitors(VISITORS_VALUE)
                .reports(REPORTS_VALUE)
                .build();
    }

    private Report getTestReport() {
        return Report.builder()
                .id(ID_VALUE)
                .topic(TOPIC_VALUE)
                .speaker(getTestUser())
                .event(getTestEvent())
                .build();
    }
}