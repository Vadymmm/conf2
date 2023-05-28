package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.query.QueryBuilder;

import static ua.java.conferences.controller.actions.constants.Pages.VIEW_EVENTS_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.model.entities.role.Role.MODERATOR;
import static ua.java.conferences.utils.PaginationUtil.*;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

/**
 * This is ViewEventsAction class. Accessible by moderator. Allows to return list of sorted events.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewEventsAction implements Action {
    private final EventService eventService;

    /**
     * @param appContext contains EventService instance to use in action
     */
    public ViewEventsAction(AppContext appContext) {
        eventService = appContext.getEventService();
    }

    /**
     * Builds required query for service, sets events list in request and obtains required path. Also sets all required
     * for pagination attributes
     *
     * @param request to get queries parameters and put events list in request
     * @return view events page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        QueryBuilder queryBuilder = getQueryBuilder(request);
        request.setAttribute(EVENTS, eventService.getSorted(queryBuilder.getQuery()));
        int numberOfRecords = eventService.getNumberOfRecords(queryBuilder.getRecordQuery(), MODERATOR);
        paginate(numberOfRecords, request);
        log.info("List of events was successfully returned");
        return VIEW_EVENTS_PAGE;
    }

    private QueryBuilder getQueryBuilder(HttpServletRequest request) {
        return eventQueryBuilder()
                .setDateFilter(request.getParameter(DATE))
                .setSortField(request.getParameter(SORT))
                .setOrder(request.getParameter(ORDER))
                .setLimits(request.getParameter(OFFSET), request.getParameter(RECORDS));
    }
}