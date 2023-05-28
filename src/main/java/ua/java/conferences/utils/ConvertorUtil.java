package ua.java.conferences.utils;

import lombok.*;
import ua.java.conferences.dto.*;
import ua.java.conferences.model.entities.*;
import ua.java.conferences.model.entities.role.Role;

import java.time.LocalDate;

/**
 * Converts DTO to Entities and vise versa
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConvertorUtil {

    /**
     * Converts UserDTO into User
     * @param userDTO to convert
     * @return User entity
     */
    public static User convertDTOToUser(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .build();
    }

    /**
     * Converts User into UserDTO
     * @param user to convert
     * @return UserDTO
     */
    public static UserDTO convertUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .role(String.valueOf(Role.getRole(user.getRoleId())))
                .build();
    }

    /**
     * Converts ReportDTO into Report. Checks for Speaker presence by checking speakerId
     * @param reportDTO to convert
     * @return Report
     */
    public static Report convertDTOToReport(ReportDTO reportDTO) {
        long speakerId = reportDTO.getSpeakerId();
        return Report.builder()
                .id(reportDTO.getId())
                .topic(reportDTO.getTopic())
                .speaker(speakerId != 0 ? User.builder().id(speakerId).build() : null)
                .event(Event.builder().id(reportDTO.getEventId()).build())
                .build();
    }

    /**
     * Converts Report into ReportDTO. Checks for Speaker's and Event's presences
     * @param report to convert
     * @return ReportDTO
     */
    public static ReportDTO convertReportToDTO(Report report) {
        User speaker = report.getSpeaker();
        Event event = report.getEvent();
        return ReportDTO.builder()
                .id(report.getId())
                .topic(report.getTopic())
                .speakerId(speaker != null ?  speaker.getId() : 0)
                .speakerEmail(speaker != null ?  speaker.getEmail() : "")
                .speakerName(speaker != null ?  speaker.getName() + " " + speaker.getSurname() : "")
                .eventId(event != null ? event.getId() : 0)
                .title(event != null ? event.getTitle() : "")
                .date(event != null ? event.getDate().toString() : "")
                .location(event != null ? event.getLocation() : "")
                .build();
    }

    /**
     * Converts EventDTO into Event
     * @param eventDTO to convert
     * @return Event
     */
    public static Event convertDTOToEvent(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .title(eventDTO.getTitle())
                .date(LocalDate.parse(eventDTO.getDate()))
                .location(eventDTO.getLocation())
                .description(eventDTO.getDescription())
                .build();
    }

    /**
     * Converts Event into EventDTO
     * @param event to convert
     * @return EventDTO
     */
    public static EventDTO convertEventToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .date(event.getDate().toString())
                .location(event.getLocation())
                .description(event.getDescription())
                .registrations(event.getRegistrations())
                .visitors(event.getVisitors())
                .reports(event.getReports())
                .build();
    }
}