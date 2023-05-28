package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_DELETE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is DeleteReportAction class. Accessible by moderator. Allows to delete report from database.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class DeleteReportAction implements Action {
    private final ReportService reportService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains ReportService and EmailSender instances to use in action
     */
    public DeleteReportAction(AppContext appContext) {
        reportService = appContext.getReportService();
        emailSender = appContext.getEmailSender();
    }

    /**
     * Obtains required path and deletes report. Sends email to report speakers email if it's present.
     * Sets succeed message if everything fine.
     *
     * @param request to get reports id and set message
     * @return path to redirect to executeGet method in SearchEventAction through front-controller with required
     * parameters to find event.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        reportService.delete(request.getParameter(REPORT_ID));
        request.getSession().setAttribute(MESSAGE, SUCCEED_DELETE);
        log.info(String.format("Report %s was deleted", request.getParameter(TOPIC)));
        sendEmail(request);
        return getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, request.getParameter(EVENT_ID));
    }

    private void sendEmail(HttpServletRequest request) {
        String email = request.getParameter(EMAIL);
        if (email != null) {
            String topic = request.getParameter(TOPIC);
            String title = request.getParameter(TITLE);
            String date = request.getParameter(DATE);
            String location = request.getParameter(LOCATION);
            String name = request.getParameter(NAME);
            String body = String.format(MESSAGE_REPORT_DELETED, name, topic, title, location, date);
            new Thread(() -> emailSender.send(SUBJECT_NOTIFICATION, body, email)).start();
        }
    }
}