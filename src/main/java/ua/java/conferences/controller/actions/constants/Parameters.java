package ua.java.conferences.controller.actions.constants;

import lombok.*;

/**
 * This is Parameters class. It contains required parameters and attributes names
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Parameters {
    /** To store logged user in session */
    public static final String LOGGED_USER = "loggedUser";

    /** Common field */
    public static final String ID = "id";

    /** Parameters and attributes to work with UserDTO */
    public static final String USER_ID = "user-id";
    public static final String USER = "user";
    public static final String USERS = "users";
    public static final String SPEAKERS = "speakers";
    public static final String ROLE = "role";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CONFIRM_PASSWORD = "confirm-password";
    public static final String OLD_PASSWORD = "old-password";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String IS_REGISTERED = "isRegistered";

    /** Parameters and attributes to work with EventDTO */
    public static final String IS_COMING = "isComing";
    public static final String EVENT_ID = "event-id";
    public static final String EVENT = "event";
    public static final String EVENT_NEW = "eventNew";
    public static final String EVENTS = "events";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String LOCATION = "location";
    public static final String DESCRIPTION = "description";
    public static final String REGISTRATIONS = "registrations";
    public static final String VISITORS = "visitors";

    /** Parameters and attributes to work with ReportDTO */
    public static final String REPORT_ID = "report-id";
    public static final String REPORT = "report";
    public static final String REPORTS = "reports";
    public static final String TOPIC = "topic";

    /** Parameters and attributes to work with sorting, ordering and pagination */
    public static final String SORT = "sort";
    public static final String ORDER = "order";
    public static final String OFFSET = "offset";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PAGES = "pages";
    public static final String START = "start";
    public static final String END = "end";
    public static final String RECORDS = "records";

    /** Parameters and attributes to send error or message to view */
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    /** Parameter to get action name */
    public static final String ACTION = "action";

    /** Parameter to get what exactly to execute inside action */
    public static final String TODO = "todo";

    /** Parameter to get captcha value */
    public static final String CAPTCHA = "g-recaptcha-response";

    /** Parameter and attribute to get or set locale */
    public static final String LOCALE = "locale";
}