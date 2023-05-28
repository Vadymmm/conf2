package ua.java.conferences.controller.actions.implementation.visitor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.query.QueryBuilder;

import static ua.java.conferences.controller.actions.constants.Pages.VIEW_VISITORS_EVENTS_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.model.entities.role.Role.VISITOR;
import static ua.java.conferences.utils.PaginationUtil.*;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

/**
 * This is ViewVisitorsEventsAction class. Accessible by visitor. Allows to return list of sorted events where visitor
 * registered.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewVisitorsEventsAction implements Action {
    private final EventService eventService;

    /**
     * @param appContext contains EventService instance to use in action
     */
    public ViewVisitorsEventsAction(AppContext appContext) {
        eventService = appContext.getEventService();
    }

    /**
     * Builds required query for service, sets visitors events list in request and obtains required path. Also sets all
     * required for pagination attributes
     *
     * @param request to get queries parameters and put events list in request
     * @return view visitors events page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        QueryBuilder queryBuilder = getQueryBuilder(request);
        request.setAttribute(EVENTS, eventService.getSortedByUser(queryBuilder.getQuery(), VISITOR));
        int numberOfRecords = eventService.getNumberOfRecords(queryBuilder.getRecordQuery(), VISITOR);
        paginate(numberOfRecords, request);
        log.info("List of events for user was successfully returned");
        return VIEW_VISITORS_EVENTS_PAGE;
    }

    private QueryBuilder getQueryBuilder(HttpServletRequest request) {
        return visitorEventQueryBuilder()
                .setUserIdFilter(((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId())
                .setDateFilter(request.getParameter(DATE))
                .setSortField(request.getParameter(SORT))
                .setOrder(request.getParameter(ORDER))
                .setLimits(request.getParameter(OFFSET), request.getParameter(RECORDS));
    }
}