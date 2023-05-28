package ua.java.conferences.model.dao.mysql.constants;

import lombok.*;

/**
 * Class that contains all My SQL queries for ReportDAO
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportSQLQueries {
    public static final String ADD_REPORT = "INSERT INTO report (topic, event_id, user_id) VALUES (?, ?, ?)";

    /** Will get report, event and speakers fields to create report instance */
    public static final String GET_REPORTS = "SELECT report.id, report.topic, report.user_id, user.email, " +
            "user.name, user.surname, report.event_id, event.title, event.date, event.location FROM report " +
            "LEFT JOIN user ON report.user_id=user.id LEFT JOIN event ON report.event_id=event.id";
    public static final String GET_REPORT_BY_ID = GET_REPORTS +" WHERE report.id = ?";
    public static final String GET_SPEAKERS_REPORTS = GET_REPORTS + " WHERE report.user_id=? AND event.date > now()";
    public static final String GET_EVENTS_REPORTS = GET_REPORTS + " WHERE event_id=?";
    public static final String EDIT_REPORT = "UPDATE report SET topic=? WHERE id=?";
    public static final String SET_SPEAKER = "UPDATE report SET user_id=? WHERE user_id IS NULL AND id=?";
    public static final String DELETE_SPEAKER = "UPDATE report SET user_id=NULL WHERE id=?";
    public static final String DELETE_REPORT = "DELETE FROM report WHERE id=?";
}