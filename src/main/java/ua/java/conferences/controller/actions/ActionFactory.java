package ua.java.conferences.controller.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.implementation.admin.*;
import ua.java.conferences.controller.actions.implementation.base.*;
import ua.java.conferences.controller.actions.implementation.moderator.*;
import ua.java.conferences.controller.actions.implementation.speaker.*;
import ua.java.conferences.controller.actions.implementation.visitor.*;

import java.util.*;

import static ua.java.conferences.controller.actions.constants.ActionNames.*;

/**
 * ActionFactory class. Contains all available actions and method to get any of them.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionFactory {
    /** Action factory instance. Singleton */
    @Getter private static final ActionFactory actionFactory = new ActionFactory();

    /** Map of all available actions, name as key and class instance as value */
    private static final Map<String, Action> ACTION_MAP = new HashMap<>();

    /** Application context instance. Contains all required services and utils */
    private static final AppContext APP_CONTEXT = AppContext.getAppContext();

    static {
        ACTION_MAP.put(DEFAULT_ACTION, new DefaultAction());
        ACTION_MAP.put(SIGN_IN_ACTION, new SignInAction(APP_CONTEXT));
        ACTION_MAP.put(SIGN_UP_ACTION, new SignUpAction(APP_CONTEXT));
        ACTION_MAP.put(PASSWORD_RESET_ACTION, new ResetPasswordAction(APP_CONTEXT));
        ACTION_MAP.put(ERROR_ACTION, new ErrorAction());

        ACTION_MAP.put(SIGN_OUT_ACTION, new SignOutAction());
        ACTION_MAP.put(EDIT_PROFILE_ACTION, new EditProfileAction(APP_CONTEXT));
        ACTION_MAP.put(CHANGE_PASSWORD_ACTION, new ChangePasswordAction(APP_CONTEXT));

        ACTION_MAP.put(VIEW_USERS_ACTION, new ViewUsersAction(APP_CONTEXT));
        ACTION_MAP.put(USERS_PDF_ACTION, new UsersToPdfAction(APP_CONTEXT));
        ACTION_MAP.put(SEARCH_USER_ACTION, new SearchUserAction(APP_CONTEXT));
        ACTION_MAP.put(DELETE_USER_ACTION, new DeleteUserAction(APP_CONTEXT));
        ACTION_MAP.put(SET_ROLE_ACTION, new SetRoleAction(APP_CONTEXT));

        ACTION_MAP.put(VIEW_UPCOMING_EVENTS_ACTION, new ViewUpEventsByVisitorAction(APP_CONTEXT));
        ACTION_MAP.put(VIEW_VISITORS_EVENTS_ACTION, new ViewVisitorsEventsAction(APP_CONTEXT));
        ACTION_MAP.put(VIEW_EVENT_BY_VISITOR_ACTION, new ViewEventByVisitorAction(APP_CONTEXT));
        ACTION_MAP.put(REGISTER_OR_CANCEL_ACTION, new RegisterOrCancelAction(APP_CONTEXT));

        ACTION_MAP.put(VIEW_SPEAKERS_EVENTS_ACTION, new ViewSpeakersEventsAction(APP_CONTEXT));
        ACTION_MAP.put(VIEW_SPEAKERS_REPORTS_ACTION, new ViewSpeakersReportsAction(APP_CONTEXT));
        ACTION_MAP.put(VIEW_EVENT_BY_SPEAKER_ACTION, new ViewEventBySpeakerAction(APP_CONTEXT));
        ACTION_MAP.put(OFFER_REPORT_ACTION, new OfferReportAction(APP_CONTEXT));
        ACTION_MAP.put(SET_OR_REMOVE_SPEAKER_BY_SPEAKER_ACTION, new SetOrRemoveSpeakerBySpeakerAction(APP_CONTEXT));

        ACTION_MAP.put(VIEW_EVENTS_ACTION, new ViewEventsAction(APP_CONTEXT));
        ACTION_MAP.put(EVENTS_PDF_ACTION, new EventsToPdfAction(APP_CONTEXT));
        ACTION_MAP.put(CREATE_EVENT_ACTION, new CreateEventAction(APP_CONTEXT));
        ACTION_MAP.put(SEARCH_EVENT_ACTION, new SearchEventAction(APP_CONTEXT));
        ACTION_MAP.put(DELETE_EVENT_ACTION, new DeleteEventAction(APP_CONTEXT));
        ACTION_MAP.put(EDIT_EVENT_ACTION, new EditEventAction(APP_CONTEXT));
        ACTION_MAP.put(SET_VISITORS_ACTION, new SetVisitorsAction(APP_CONTEXT));
        ACTION_MAP.put(VIEW_REPORT_ACTION, new ViewReportAction(APP_CONTEXT));
        ACTION_MAP.put(CREATE_REPORT_ACTION, new CreateReportAction(APP_CONTEXT));
        ACTION_MAP.put(CHANGE_TOPIC_ACTION, new ChangeTopicAction(APP_CONTEXT));
        ACTION_MAP.put(DELETE_REPORT_ACTION, new DeleteReportAction(APP_CONTEXT));
        ACTION_MAP.put(SET_OR_REMOVE_SPEAKER_ACTION, new SetOrRemoveSpeakerAction(APP_CONTEXT));
    }

    /**
     * Obtains action by its name
     *
     * @param actionName - to search in map
     * @return required action implementation or DefaultAction if there is no such action
     */
    public Action createAction(String actionName) {
        return ACTION_MAP.getOrDefault(actionName, new DefaultAction());
    }
}