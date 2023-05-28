package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.query.QueryBuilder;

import static ua.java.conferences.controller.actions.constants.Pages.VIEW_SPEAKERS_EVENTS_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.model.entities.role.Role.SPEAKER;
import static ua.java.conferences.utils.PaginationUtil.paginate;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

/**
 * This is ViewSpeakersEventsAction class. Accessible by speaker. Allows to return list of sorted events.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewSpeakersEventsAction implements Action {
    private final EventService eventService;

    /**
     * @param appContext contains EventService instance to use in action
     */
    public ViewSpeakersEventsAction(AppContext appContext) {
        eventService = appContext.getEventService();
    }

    /**
     * Builds required query for service, sets events list in request and obtains required path. Also sets all required
     * for pagination attributes
     *
     * @param request to get queries parameters and put events list in request
     * @return view speakers events page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        QueryBuilder queryBuilder = getQueryBuilder(request);
        request.setAttribute(EVENTS, eventService.getSortedByUser(queryBuilder.getQuery(), SPEAKER));
        int numberOfRecords = eventService.getNumberOfRecords(queryBuilder.getRecordQuery(), SPEAKER);
        paginate(numberOfRecords, request);
        log.info("List of events was successfully returned");
        return VIEW_SPEAKERS_EVENTS_PAGE;
    }

    private QueryBuilder getQueryBuilder(HttpServletRequest request) {
        return eventQueryBuilder()
                .setUserIdFilter(((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId())
                .setDateFilter(request.getParameter(DATE))
                .setSortField(request.getParameter(SORT))
                .setOrder(request.getParameter(ORDER))
                .setLimits(request.getParameter(OFFSET), request.getParameter(RECORDS));
    }
}