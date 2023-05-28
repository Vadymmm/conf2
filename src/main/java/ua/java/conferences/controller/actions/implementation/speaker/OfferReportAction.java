package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import java.time.LocalDate;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.model.entities.role.Role.SPEAKER;
import static ua.java.conferences.utils.QueryBuilderUtil.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is OfferReportAction class. Accessible by speaker. Allows to create new report. Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class OfferReportAction implements Action {
    private final ReportService reportService;
    private final EventService eventService;
    private final UserService userService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains all services and EmailSender instances to use in action
     */
    public OfferReportAction(AppContext appContext) {
        reportService = appContext.getReportService();
        eventService = appContext.getEventService();
        userService = appContext.getUserService();
        emailSender = appContext.getEmailSender();
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
     * to request. Gets event from EventService
     *
     * @param request to get message and error attribute from session and put it in request
     * @return offer report page
     */
    private String executeGet(HttpServletRequest request) throws ServiceException {
        transferAttributes(request);
        long speakerId = ((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId();
        try {
            EventDTO event = getEvent(request.getParameter(EVENT_ID), speakerId);
            request.setAttribute(EVENT, event);
            log.info(String.format("Report was created by speaker with id=%d", speakerId));
        } catch (NoSuchEventException e) {
            log.info(String.format("Couldn't create report because of %s", e.getMessage()));
            request.setAttribute(ERROR, OFFER_FORBIDDEN);
        }
        return OFFER_REPORT_PAGE;
    }

    private EventDTO getEvent(String parameterEventId, long userId) throws ServiceException {
        String query = eventQueryBuilder()
                .setUserIdFilter(userId)
                .setLimits("0", String.valueOf(Integer.MAX_VALUE))
                .getQuery();
        return eventService.getSortedByUser(query, SPEAKER).stream()
                .filter(e -> String.valueOf(e.getId()).equals(parameterEventId))
                .filter(e -> LocalDate.now().isBefore(LocalDate.parse(e.getDate())))
                .findAny()
                .orElseThrow(NoSuchEventException::new);
    }

    private void transferAttributes(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, MESSAGE);
        transferStringFromSessionToRequest(request, ERROR);
    }

    /**
     * Called from doPost method in front-controller. Tries to create report for conference. Sends email to all
     * moderators. Sends email using multithreading to make it faster.
     *
     * @param request to get event id and set message in case of successful deleting
     * @return path to redirect to executeGet method through front-controller with all required parameters
     */
    private String executePost(HttpServletRequest request) throws ServiceException {
        ReportDTO reportDTO = getReportDTO(request);
        try {
            reportService.addReport(reportDTO);
            request.getSession().setAttribute(MESSAGE, SUCCEED_ADD);
            sendEmail(reportDTO, getURL(request));
            log.info(String.format("Report %s was created by speaker", reportDTO.getTopic()));
        } catch (IncorrectFormatException e) {
            log.info(String.format("Couldn't create report because of %s", e.getMessage()));
            request.getSession().setAttribute(ERROR, e.getMessage());
        }
        return getActionToRedirect(OFFER_REPORT_ACTION, EVENT_ID, String.valueOf(reportDTO.getEventId()));
    }

    private ReportDTO getReportDTO(HttpServletRequest request) {
        UserDTO speaker = (UserDTO) request.getSession().getAttribute(LOGGED_USER);
        return  ReportDTO.builder()
                .topic(request.getParameter(TOPIC))
                .speakerId(speaker.getId())
                .eventId(Long.parseLong(request.getParameter(EVENT_ID)))
                .title(request.getParameter(TITLE))
                .speakerName(speaker.getName() + " " + speaker.getSurname())
                .build();
    }

    private void sendEmail(ReportDTO report, String url) throws ServiceException {
        for (UserDTO moderator : userService.getModerators()) {
            new Thread(
                    () -> {
                        String body = String.format(MESSAGE_OFFER_REPORT, moderator.getName(), report.getSpeakerName(),
                                report.getTopic(), report.getTitle(), url, report.getEventId());
                        emailSender.send(SUBJECT_NOTIFICATION, body, moderator.getEmail());})
                    .start();
        }
    }
}