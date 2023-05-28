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
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is SearchEventAction class. Accessible by moderator. Allows to find event from database by title.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class SearchEventAction implements Action {
    private final EventService eventService;
    private final ReportService reportService;

    /**
     * @param appContext contains EventService and ReportService instances to use in action
     */
    public SearchEventAction(AppContext appContext) {
        eventService = appContext.getEventService();
        reportService = appContext.getReportService();
    }

    /**
     * Obtains required path and sets event and it's reports to request if it finds
     *
     * @param request to get event title or id and put event in request or error if it can't find event
     * @return view event page if it finds or search event page if not
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        transferStringFromSessionToRequest(request, MESSAGE);
        String path = VIEW_EVENT_PAGE;
        try {
            EventDTO event = getEventDTO(request);
            request.setAttribute(EVENT, event);
            request.setAttribute(REPORTS, reportService.viewEventsReports(String.valueOf(event.getId())));
            log.info(String.format("Event %s was successfully found", event.getTitle()));
        } catch (NoSuchEventException | IncorrectFormatException e) {
            request.setAttribute(ERROR, e.getMessage());
            request.setAttribute(TITLE, request.getParameter(TITLE));
            log.info("Couldn't find an event");
            path = SEARCH_EVENT_PAGE;
        }
        return path;
    }

    private EventDTO getEventDTO(HttpServletRequest request) throws ServiceException {
        String title = request.getParameter(TITLE);
        String id = request.getParameter(EVENT_ID);
        return title != null ? eventService.getByTitle(title) : eventService.getById(id);
    }
}