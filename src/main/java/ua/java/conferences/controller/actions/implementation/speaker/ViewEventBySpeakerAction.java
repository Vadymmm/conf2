package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import java.time.LocalDate;

import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ACCESS_DENIED;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.model.entities.role.Role.SPEAKER;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

/**
 * This is ViewEventBySpeakerAction class. Accessible by speaker. Allows to view event with all it's reports.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewEventBySpeakerAction implements Action {
    private final EventService eventService;
    private final ReportService reportService;

    /**
     * @param appContext contains ReportService and EventService instances to use in action
     */
    public ViewEventBySpeakerAction(AppContext appContext) {
        eventService = appContext.getEventService();
        reportService = appContext.getReportService();
    }

    /**
     * Gets event by id, check if it is future event. Set to request EventDTO and all report there. Checks if speaker
     * participates. If not - throws exceptions.
     *
     * @param request to get event id
     * @return view event by speaker page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            setAttributes(request);
            log.info("Event was successfully found");
        } catch (NoSuchEventException e) {
            request.setAttribute(ERROR, ACCESS_DENIED);
            log.info("Couldn't find an event");
        }
        return VIEW_EVENT_BY_SPEAKER_PAGE;
    }

    private void setAttributes(HttpServletRequest request) throws ServiceException {
        long speakerId = ((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId();
        String parameterEventId = request.getParameter(EVENT_ID);
        EventDTO event = getEvent(parameterEventId, speakerId);
        request.setAttribute(EVENT, event);
        request.setAttribute(REPORTS, reportService.viewEventsReports(parameterEventId));
        request.setAttribute(IS_COMING, isFutureEvent(event));
    }

    private EventDTO getEvent(String parameterEventId, long userId) throws ServiceException {
        String query = eventQueryBuilder()
                .setUserIdFilter(userId)
                .setLimits("0", String.valueOf(Integer.MAX_VALUE))
                .getQuery();
        return eventService.getSortedByUser(query, SPEAKER).stream()
                .filter(e -> String.valueOf(e.getId()).equals(parameterEventId))
                .findAny()
                .orElseThrow(NoSuchEventException::new);
    }

    private static boolean isFutureEvent(EventDTO event) {
        return LocalDate.now().isBefore(LocalDate.parse(event.getDate()));
    }
}