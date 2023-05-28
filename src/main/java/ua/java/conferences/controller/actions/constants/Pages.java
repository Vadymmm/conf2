package ua.java.conferences.controller.actions.constants;

import lombok.*;

/**
 * This is Pages class. It contains all web app pages
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Pages {
    /** Any user's pages */
    public static final String CONTROLLER_PAGE = "controller";
    public static final String INDEX_PAGE = "index.jsp";
    public static final String ABOUT_PAGE = "about.jsp";
    public static final String CONTACTS_PAGE = "contacts.jsp";
    public static final String SIGN_IN_PAGE = "signIn.jsp";
    public static final String SIGN_UP_PAGE = "signUp.jsp";
    public static final String ERROR_PAGE = "error.jsp";

    /** Logged user's pages */
    public static final String PROFILE_PAGE = "profile.jsp";
    public static final String EDIT_PROFILE_PAGE = "editProfile.jsp";
    public static final String CHANGE_PASSWORD_PAGE = "changePassword.jsp";
    public static final String RESET_PASSWORD_PAGE = "resetPassword.jsp";

    /** Admins pages */
    public static final String VIEW_USERS_PAGE = "viewUsers.jsp";
    public static final String SEARCH_USER_PAGE = "searchUser.jsp";
    public static final String USER_BY_EMAIL_PAGE = "userByAdmin.jsp";

    /** Visitors pages */
    public static final String VIEW_UP_EVENTS_BY_VISITOR_PAGE = "viewUpcomingEventsByVisitor.jsp";
    public static final String VIEW_VISITORS_EVENTS_PAGE = "viewVisitorsEvents.jsp";
    public static final String VIEW_EVENT_BY_VISITOR_PAGE = "viewEventByVisitor.jsp";

    /** Speakers pages */
    public static final String VIEW_SPEAKERS_EVENTS_PAGE = "viewSpeakersEvents.jsp";
    public static final String VIEW_SPEAKERS_REPORTS_PAGE = "viewSpeakersReports.jsp";
    public static final String VIEW_EVENT_BY_SPEAKER_PAGE = "viewEventBySpeaker.jsp";
    public static final String OFFER_REPORT_PAGE = "offerReportBySpeaker.jsp";

    /** Moderators pages */
    public static final String VIEW_EVENTS_PAGE = "viewEvents.jsp";
    public static final String CREATE_EVENT_PAGE = "createEvent.jsp";
    public static final String SEARCH_EVENT_PAGE = "searchEvent.jsp";
    public static final String VIEW_EVENT_PAGE = "viewEvent.jsp";
    public static final String EDIT_EVENT_PAGE = "editEvent.jsp";
    public static final String VIEW_REPORT_PAGE = "viewReport.jsp";
}