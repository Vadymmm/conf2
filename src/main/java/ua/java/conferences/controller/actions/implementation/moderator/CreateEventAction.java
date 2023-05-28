package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is CreateEventAction class. Accessible by moderator. Allows to create new conference. Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class CreateEventAction implements Action {
    private final EventService eventService;

    /**
     * @param appContext contains EventService instance to use in action
     */
    public CreateEventAction(AppContext appContext) {
        eventService = appContext.getEventService();
    }

    /**
     * Checks method and calls required implementation
     *
     * @param request  to get method, session and set all required attributes
     * @return path to redirect or forward by front-controller
     * @throws ServiceException to call error page in front-controller
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        return isPostMethod(request) ? executePost(request) : executeGet(request);
    }

    /**
     * Called from doGet method in front-controller. Obtains required path and transfer attributes from session
     * to request. Executes if only error happens.
     *
     * @param request to get error and event attribute from session and put it in request
     * @return create event page after failing to create new conference
     */
    private String executeGet(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, ERROR);
        transferEventDTOFromSessionToRequest(request, EVENT);
        return CREATE_EVENT_PAGE;
    }

    /**
     * Called from doPost method in front-controller. Tries to create new conference.
     * Logs error in case if not able
     *
     * @param request to get events fields and sets some attributes to session
     * @return path to redirect to executeGet in SearchEventAction if successful or path to redirect to executeGet in
     * CreateEventAction if not through front-controller
     */
    private String executePost(HttpServletRequest request) throws ServiceException {
        EventDTO event = getEventDTO(request);
        try {
            eventService.addEvent(event);
            long eventId = eventService.getByTitle(event.getTitle()).getId();
            log.info(String.format("Event %s was created", event.getTitle()));
            return getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, String.valueOf(eventId));
        } catch (IncorrectFormatException | DuplicateTitleException e) {
            log.info(String.format(" Couldn't creat event %s because of %s", event.getTitle(), e.getMessage()));
            request.getSession().setAttribute(EVENT, event);
            request.getSession().setAttribute(ERROR, e.getMessage());
        }
        return getActionToRedirect(CREATE_EVENT_ACTION);
    }

    private EventDTO getEventDTO(HttpServletRequest request) {
        return EventDTO.builder()
                .title(request.getParameter(TITLE))
                .date(request.getParameter(DATE))
                .location(request.getParameter(LOCATION))
                .description(request.getParameter(DESCRIPTION))
                .build();
    }
}