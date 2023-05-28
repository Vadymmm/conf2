package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is SetVisitorsAction class. Accessible by moderator. Allows to update conferences visitors number
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class SetVisitorsAction implements Action {
    private final EventService eventService;

    /**
     * @param appContext contains EventService instance to use in action
     */
    public SetVisitorsAction(AppContext appContext) {
        eventService = appContext.getEventService();
    }

    /**
     * Obtains required path and sets visitors number to conference
     *
     * @param request to get event id and visitors number
     * @return search event page with required parameters
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String eventId = request.getParameter(EVENT_ID);
        String visitors = request.getParameter(VISITORS);
        eventService.setVisitorsCount(eventId, visitors);
        log.info(String.format("Event with id=%s updated with visitors number",eventId));
        return getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, eventId);
    }
}