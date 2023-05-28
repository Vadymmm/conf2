package ua.java.conferences.controller.actions.implementation.visitor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import java.time.LocalDate;

import static ua.java.conferences.controller.actions.constants.Pages.VIEW_EVENT_BY_VISITOR_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is ViewEventByVisitorAction class. Accessible by visitor. Allows to view event with all it's reports.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewEventByVisitorAction implements Action {
    private final EventService eventService;
    private final ReportService reportService;
    private final UserService userService;

    /**
     * @param appContext contains UserService, ReportService and EventService instances to use in action
     */
    public ViewEventByVisitorAction(AppContext appContext) {
        eventService = appContext.getEventService();
        reportService = appContext.getReportService();
        userService = appContext.getUserService();
    }

    /**
     * Gets event by id, check if it is future event and if user registered for event. Set to request EventDTO and
     * all report there.
     *
     * @param request to get event id
     * @return view event by visitor page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            setAttributes(request);
            log.info("Event was successfully found");
        } catch (NoSuchEventException e) {
            request.setAttribute(ERROR, e.getMessage());
            log.info("Couldn't find an event");
        }
        return VIEW_EVENT_BY_VISITOR_PAGE;
    }

    private void setAttributes(HttpServletRequest request) throws ServiceException {
        long userId =  ((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId();
        String parameterEventId = request.getParameter(EVENT_ID);
        EventDTO event = eventService.getById(parameterEventId);
        request.setAttribute(EVENT, event);
        request.setAttribute(IS_REGISTERED, userService.isRegistered(userId, parameterEventId));
        request.setAttribute(REPORTS, reportService.viewEventsReports(parameterEventId));
        request.setAttribute(IS_COMING, isFutureEvent(event));
    }

    private static boolean isFutureEvent(EventDTO event) {
        return LocalDate.now().isBefore(LocalDate.parse(event.getDate()));
    }
}