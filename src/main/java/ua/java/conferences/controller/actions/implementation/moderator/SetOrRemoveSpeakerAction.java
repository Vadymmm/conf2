package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_REPORT_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is SetOrRemoveSpeakerAction class. Accessible by moderator. Allows to change report's speaker.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class SetOrRemoveSpeakerAction implements Action {
    private final ReportService reportService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains ReportService and EmailSender instances to use in action
     */
    public SetOrRemoveSpeakerAction(AppContext appContext) {
        reportService = appContext.getReportService();
        emailSender = appContext.getEmailSender();
    }

    /**
     * Obtains required path and change report's speaker. Sends email to report speakers email if it's present.
     *
     * @param request to get report id and speakers email
     * @return path to redirect to executeGet method in ViewReportAction through front-controller with required
     * parameters to find report.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String reportId = request.getParameter(REPORT_ID);
        setOrRemove(request, Long.parseLong(reportId));
        return getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, reportId);
    }

    private void setOrRemove(HttpServletRequest request, long reportId) throws ServiceException {
        String todo = request.getParameter(TODO);
        if (todo.equals(SET)) {
            if (reportService.setSpeaker(reportId, Long.parseLong(request.getParameter(USER_ID)))) {
                sendEmail(MESSAGE_SET_SPEAKER, String.valueOf(reportId), null);
                log.info(String.format("For report with id=%d speaker was set",reportId));
            } else {
                request.getSession().setAttribute(ERROR, FAIL_SET_SPEAKER);
                log.info(String.format("Couldn't set speaker for report with id=%d",reportId));
                return;
            }
        } else if (todo.equals(REMOVE)) {
            reportService.deleteSpeaker(reportId);
            log.info(String.format("Deleted speaker for report with id=%d",reportId));
            sendEmail(MESSAGE_REMOVE_SPEAKER, String.valueOf(reportId), request);
        }
        request.getSession().setAttribute(MESSAGE, SUCCEED_UPDATE);
    }

    private void sendEmail(String message, String reportId, HttpServletRequest request) throws ServiceException {
        ReportDTO report = reportService.getById(reportId);
        String email = request != null ? request.getParameter(EMAIL) : report.getSpeakerEmail();
        String name = request != null ? request.getParameter(NAME) : report.getSpeakerName();
        String topic = report.getTopic();
        String title = report.getTitle();
        String date = report.getDate();
        String location = report.getLocation();
        String body = String.format(message, name, topic, title, location, date);
        new Thread(() -> emailSender.send(SUBJECT_NOTIFICATION, body, email)).start();
    }
}