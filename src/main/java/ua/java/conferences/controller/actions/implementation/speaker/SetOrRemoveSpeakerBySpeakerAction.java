package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is SetOrRemoveSpeakerBySpeakerAction class. Accessible by speaker. Allows to change report's speaker.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class SetOrRemoveSpeakerBySpeakerAction implements Action {
    private final ReportService reportService;
    private final UserService userService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains ReportService, UserService and EmailSender instances to use in action
     */
    public SetOrRemoveSpeakerBySpeakerAction(AppContext appContext) {
        reportService = appContext.getReportService();
        userService = appContext.getUserService();
        emailSender = appContext.getEmailSender();
    }

    /**
     * Obtains required path and change report's speaker. Sends email to all moderators. Sends email using
     * multithreading to make it faster.
     *
     * @param request to get report and event ids
     * @return path to redirect to executeGet method in ViewEventBYSpeakerAction through front-controller with required
     * parameters or ViewSpeakersReportsAction (if it comes from speakers report page).
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        long reportId = Long.parseLong(request.getParameter(REPORT_ID));
        String eventId = request.getParameter(EVENT_ID);
        setOrRemove(request, reportId);
        return eventId != null ? getActionToRedirect(VIEW_EVENT_BY_SPEAKER_ACTION, EVENT_ID, eventId)
                : getActionToRedirect(VIEW_SPEAKERS_REPORTS_ACTION);
    }

    private void setOrRemove(HttpServletRequest request, long reportId) throws ServiceException {
        String todo = request.getParameter(TODO);
        if(todo.equals(SET)) {
            long speakerId = ((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId();
            if (reportService.setSpeaker(reportId, speakerId)) {
                log.info(String.format("For report with id=%d speaker was set",reportId));
                sendEmail(request, MESSAGE_SET_SPEAKER_BY_SPEAKER);
            }
        } else if (todo.equals(REMOVE)) {
            reportService.deleteSpeaker(reportId);
            log.info(String.format("Deleted speaker for report with id=%d",reportId));
            sendEmail(request, MESSAGE_REMOVE_SPEAKER_BY_SPEAKER);
        }
    }

    private void sendEmail(HttpServletRequest request, String message) throws ServiceException {
        UserDTO speaker = (UserDTO) request.getSession().getAttribute(LOGGED_USER);
        String speakerName = speaker.getName() + " " + speaker.getSurname();
        String topic = request.getParameter(TOPIC);
        String title = request.getParameter(TITLE);
        String reportId = request.getParameter(REPORT_ID);
        for (UserDTO moderator : userService.getModerators()) {
            new Thread(
                    () -> {
                        String body = String.format(message,
                                moderator.getName(), speakerName, topic, title, getURL(request), reportId);
                        emailSender.send(SUBJECT_NOTIFICATION, body, moderator.getEmail());})
                    .start();
        }
    }
}