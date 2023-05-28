package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.EventService;
import ua.java.conferences.utils.PdfUtil;
import ua.java.conferences.utils.query.QueryBuilder;

import java.io.*;
import java.util.List;

import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_EVENTS_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.QueryBuilderUtil.eventQueryBuilder;

/**
 * This is EventsToPdfAction class. Accessible by moderator. Allows to download list of all events that match demands
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class EventsToPdfAction implements Action {
    private final EventService eventService;
    private final PdfUtil pdfUtil;

    /**
     * @param appContext contains EventService and PdfUtil instances to use in action
     */
    public EventsToPdfAction(AppContext appContext) {
        eventService = appContext.getEventService();
        pdfUtil = appContext.getPdfUtil();
    }

    /**
     * Builds required query for service, sets events list in response to download. Checks for locale to set up
     * locale for pdf document
     *
     * @param request to get queries parameters
     * @param response to set events list there
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        QueryBuilder queryBuilder = getQueryBuilder(request);
        List<EventDTO> events = eventService.getSorted(queryBuilder.getQuery());
        String locale = (String) request.getSession().getAttribute(LOCALE);
        ByteArrayOutputStream usersPdf = pdfUtil.createEventsPdf(events, locale);
        setResponse(response, usersPdf);
        return getActionToRedirect(VIEW_EVENTS_ACTION);
    }

    private QueryBuilder getQueryBuilder(HttpServletRequest request) {
        String zero = "0";
        String max = String.valueOf(Integer.MAX_VALUE);
        return eventQueryBuilder()
                .setDateFilter(request.getParameter(DATE))
                .setSortField(request.getParameter(SORT))
                .setOrder(request.getParameter(ORDER))
                .setLimits(zero, max);
    }

    /**
     * Sets events list in response to download. Configure response to download pdf document
     *
     * @param response to set events list there
     * @param output - output stream that contains pdf document
     */
    private void setResponse(HttpServletResponse response, ByteArrayOutputStream output) {
        response.setContentType("application/pdf");
        response.setContentLength(output.size());
        response.setHeader("Content-Disposition", "attachment; filename=\"events.pdf\"");
        try (OutputStream outputStream = response.getOutputStream()) {
            output.writeTo(outputStream);
            outputStream.flush();
            log.info("Event list was downloaded");
        } catch (IOException e) {
            log.error(String.format("Couldn't set event list to download because of %s", e.getMessage()));
        }
    }
}
