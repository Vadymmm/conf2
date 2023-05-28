package ua.java.conferences.model.dao.mysql.constants;

import lombok.*;

/**
 * Class that contains all My SQL queries for EventDAO
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventSQLQueries {
    /** Queries to use in another queries */
    private static final String UTIL_GET_EVENT = "SELECT event.id AS id, title, date, location, description, " +
            "COUNT(DISTINCT user_has_event.user_id) AS registrations, COUNT(DISTINCT report.id) AS reports, " +
            "visitors FROM event LEFT JOIN user_has_event ON event.id=user_has_event.event_id " +
            "LEFT JOIN report ON event.id=report.event_id ";
    private static final String GROUP_BY = " GROUP BY event.id ";

    /** Queries to use in DAO methods */
    public static final String ADD_EVENT = "INSERT INTO event (title, date, location, description) VALUES (?,?,?,?)";
    public static final String GET_EVENT_BY_ID =  UTIL_GET_EVENT +"WHERE event.id=?" + GROUP_BY;
    public static final String GET_EVENT_BY_TITLE =  UTIL_GET_EVENT +"WHERE event.title=?" + GROUP_BY;
    public static final String GET_EVENTS =  UTIL_GET_EVENT + GROUP_BY;
    public static final String GET_SORTED = UTIL_GET_EVENT + " %s";
    public static final String GET_VISITORS_EVENTS = "SELECT * FROM event JOIN user_has_event ON " +
            "id=user_has_event.event_id %s";
    public static final String GET_SPEAKERS_EVENTS = "SELECT * FROM event JOIN report ON event.id=report.event_id  %s";
    public static final String GET_NUMBER_OF_RECORDS = "SELECT COUNT(event.id) AS numberOfRecords FROM event %s";
    public static final String GET_NUMBER_OF_RECORDS_BY_VISITOR = "SELECT COUNT(event.id) AS numberOfRecords " +
            "FROM event JOIN user_has_event ON id=user_has_event.event_id %s";
    public static final String GET_NUMBER_OF_RECORDS_BY_SPEAKER = "SELECT COUNT(DISTINCT event.id) AS numberOfRecords " +
            "FROM event LEFT JOIN report ON event.id=report.event_id %s";
    public static final String EDIT_EVENT = "UPDATE event SET title=?, date=?, location=?, description=? WHERE id=?";
    public static final String SET_VISITORS = "UPDATE event SET visitors=? WHERE id=?";

    /** Will delete records in 3 tables: event, all reports from this event and user's registrations*/
    public static final String DELETE_EVENT = "DELETE event, user_has_event, report FROM event LEFT JOIN" +
            " user_has_event ON event.id=user_has_event.event_id LEFT JOIN report" +
            " ON event.id=report.event_id WHERE event.id=?";
}