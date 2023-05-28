package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_REPORT_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_UPDATE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is ChangeTopicAction class. Accessible by moderator. Allows to change report's topic.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ChangeTopicAction implements Action {
    private final ReportService reportService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains ReportService and EmailSender instances to use in action
     */
    public ChangeTopicAction(AppContext appContext) {
        reportService = appContext.getReportService();
        emailSender = appContext.getEmailSender();
    }

    /**
     * Obtains required path and change report's topic. Sends email to report speakers email if it's present.
     * Sets error if topic format is invalid.
     *
     * @param request to get report fields
     * @return path to redirect to executeGet method in ViewReportAction through front-controller with required
     * parameters to find report.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        ReportDTO report = getReportDTO(request);
        try {
            reportService.update(report);
            request.getSession().setAttribute(MESSAGE, SUCCEED_UPDATE);
            log.info(String.format("Report changed topic to %s", report.getTopic()));
            sendEmail(report, getURL(request));
        } catch (IncorrectFormatException e) {
            request.getSession().setAttribute(ERROR, e.getMessage());
            log.info(String.format("Couldn't change report's topic to %s because of %s", report.getTopic(), e.getMessage()));
        }
        return getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, String.valueOf(report.getId()));
    }

    private void sendEmail(ReportDTO reportDTO, String url) throws ServiceException {
        ReportDTO report = reportService.getById(String.valueOf(reportDTO.getId()));
        if (report.getSpeakerEmail() != null) {
            String body = String.format(MESSAGE_TOPIC_CHANGED, report.getSpeakerName(), report.getTitle(),
                    report.getLocation(), report.getDate(), report.getTopic(), url, report.getEventId());
            new Thread(() -> emailSender.send(SUBJECT_NOTIFICATION, body, report.getSpeakerEmail())).start();
        }
    }

    private ReportDTO getReportDTO(HttpServletRequest request) {
        return ReportDTO.builder()
                .id(Long.parseLong(request.getParameter(REPORT_ID)))
                .topic(request.getParameter(TOPIC))
                .build();
    }
}