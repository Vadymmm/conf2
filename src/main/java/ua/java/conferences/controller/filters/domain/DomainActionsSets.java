package ua.java.conferences.controller.filters.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

import static ua.java.conferences.controller.actions.constants.ActionNames.*;

/**
 * Contains action sets for anonymous user and different roles logged user. Defines if user has access to the action
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainActionsSets {
    @Getter private static final Set<String> anonymousUserActions = new HashSet<>();
    private static final Set<String> loggedUserActions = new HashSet<>();
    @Getter private static final Set<String> visitorActions = new HashSet<>();
    @Getter private static final Set<String> speakerActions = new HashSet<>();
    @Getter private static final Set<String> moderatorActions = new HashSet<>();
    @Getter private static final Set<String> adminActions = new HashSet<>();

    static {
        anonymousUserActions.add(DEFAULT_ACTION);
        anonymousUserActions.add(SIGN_IN_ACTION);
        anonymousUserActions.add(SIGN_UP_ACTION);
        anonymousUserActions.add(PASSWORD_RESET_ACTION);
        anonymousUserActions.add(ERROR_ACTION);
        anonymousUserActions.add(SIGN_OUT_ACTION);
    }

    static {
        loggedUserActions.addAll(anonymousUserActions);
        loggedUserActions.add(EDIT_PROFILE_ACTION);
        loggedUserActions.add(CHANGE_PASSWORD_ACTION);
    }

    static {
        visitorActions.addAll(loggedUserActions);
        visitorActions.add(VIEW_UPCOMING_EVENTS_ACTION);
        visitorActions.add(VIEW_VISITORS_EVENTS_ACTION);
        visitorActions.add(VIEW_EVENT_BY_VISITOR_ACTION);
        visitorActions.add(REGISTER_OR_CANCEL_ACTION);
    }

    static {
        speakerActions.addAll(loggedUserActions);
        speakerActions.add(VIEW_SPEAKERS_EVENTS_ACTION);
        speakerActions.add(VIEW_SPEAKERS_REPORTS_ACTION);
        speakerActions.add(VIEW_EVENT_BY_SPEAKER_ACTION);
        speakerActions.add(OFFER_REPORT_ACTION);
        speakerActions.add(SET_OR_REMOVE_SPEAKER_BY_SPEAKER_ACTION);
    }

    static {
        moderatorActions.addAll(loggedUserActions);
        moderatorActions.add(VIEW_EVENTS_ACTION);
        moderatorActions.add(EVENTS_PDF_ACTION);
        moderatorActions.add(CREATE_EVENT_ACTION);
        moderatorActions.add(SEARCH_EVENT_ACTION);
        moderatorActions.add(DELETE_EVENT_ACTION);
        moderatorActions.add(EDIT_EVENT_ACTION);
        moderatorActions.add(SET_VISITORS_ACTION);
        moderatorActions.add(CREATE_REPORT_ACTION);
        moderatorActions.add(VIEW_REPORT_ACTION);
        moderatorActions.add(CHANGE_TOPIC_ACTION);
        moderatorActions.add(DELETE_REPORT_ACTION);
        moderatorActions.add(SET_OR_REMOVE_SPEAKER_ACTION);
    }

    static {
        adminActions.addAll(loggedUserActions);
        adminActions.add(SEARCH_USER_ACTION);
        adminActions.add(DELETE_USER_ACTION);
        adminActions.add(SET_ROLE_ACTION);
        adminActions.add(VIEW_USERS_ACTION);
        adminActions.add(USERS_PDF_ACTION);
    }
}