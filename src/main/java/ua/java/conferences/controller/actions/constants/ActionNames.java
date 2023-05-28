package ua.java.conferences.controller.actions.constants;

import lombok.*;

/**
 * This is ActionNames class. It contains all action names
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionNames {
    /** Any user's actions */
    public static final String DEFAULT_ACTION = "default";
    public static final String SIGN_IN_ACTION = "sign-in";
    public static final String SIGN_UP_ACTION ="sign-up";
    public static final String PASSWORD_RESET_ACTION = "password-reset";
    public static final String ERROR_ACTION = "error";

    /** Logged user's actions */
    public static final String SIGN_OUT_ACTION = "sign-out";
    public static final String EDIT_PROFILE_ACTION = "edit-profile";
    public static final String CHANGE_PASSWORD_ACTION = "change-password";

    /** Admins actions */
    public static final String SEARCH_USER_ACTION = "search-user";
    public static final String VIEW_USERS_ACTION = "view-users";
    public static final String USERS_PDF_ACTION = "users-pdf";
    public static final String DELETE_USER_ACTION = "delete-user";
    public static final String SET_ROLE_ACTION = "set-role";

    /** Visitors actions */
    public static final String VIEW_UPCOMING_EVENTS_ACTION = "view-upcoming-events";
    public static final String VIEW_VISITORS_EVENTS_ACTION = "view-visitors-events";
    public static final String VIEW_EVENT_BY_VISITOR_ACTION = "view-event-by-visitor";
    public static final String REGISTER_OR_CANCEL_ACTION = "register-or-cancel";

    /** Speakers actions */
    public static final String VIEW_SPEAKERS_EVENTS_ACTION = "view-speakers-events";
    public static final String VIEW_SPEAKERS_REPORTS_ACTION = "view-speakers-reports";
    public static final String VIEW_EVENT_BY_SPEAKER_ACTION = "view-event-by-speaker";
    public static final String OFFER_REPORT_ACTION = "offer-report";
    public static final String SET_OR_REMOVE_SPEAKER_BY_SPEAKER_ACTION = "set-or-remove-speaker-by-speaker";

    /** Moderators actions */
    public static final String VIEW_EVENTS_ACTION = "view-events";
    public static final String EVENTS_PDF_ACTION = "events-pdf";
    public static final String CREATE_EVENT_ACTION = "create-event";
    public static final String SEARCH_EVENT_ACTION = "search-event";
    public static final String DELETE_EVENT_ACTION = "delete-event";
    public static final String EDIT_EVENT_ACTION = "edit-event";
    public static final String SET_VISITORS_ACTION = "set-visitors";
    public static final String CREATE_REPORT_ACTION = "create-report";
    public static final String VIEW_REPORT_ACTION = "view-report";
    public static final String CHANGE_TOPIC_ACTION = "change-topic";
    public static final String DELETE_REPORT_ACTION = "delete-report";
    public static final String SET_OR_REMOVE_SPEAKER_ACTION = "set-or-remove-speaker";
}