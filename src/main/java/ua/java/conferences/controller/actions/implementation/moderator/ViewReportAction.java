package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import java.time.LocalDate;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.Pages.VIEW_REPORT_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is ViewReportAction class. Accessible by moderator. Allows to view report.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewReportAction implements Action {
    private final ReportService reportService;
    private final UserService userService;

    /**
     * @param appContext contains ReportService and UserService instances to use in action
     */
    public ViewReportAction(AppContext appContext) {
        reportService = appContext.getReportService();
        userService = appContext.getUserService();
    }

    /**
     * Gets report by id, check if it is future report. Set to request reportDTO and all available speakers
     *
     * @param request to get report id
     * @return view report page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        transferAttributes(request);
        try {
            ReportDTO report = reportService.getById(request.getParameter(REPORT_ID));
            request.setAttribute(IS_COMING, isFutureReport(report));
            request.setAttribute(REPORT, report);
            request.setAttribute(SPEAKERS, userService.getSpeakers());
            log.info(String.format("Report %s was successfully returned", report.getTopic()));
        } catch (NoSuchReportException e) {
            log.info(String.format("Couldn't find report because of %s",e.getMessage()));
            request.setAttribute(ERROR, e.getMessage());
        }
        return VIEW_REPORT_PAGE;
    }

    private static boolean isFutureReport(ReportDTO report) {
        return LocalDate.now().isBefore(LocalDate.parse(report.getDate()));
    }

    private void transferAttributes(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, MESSAGE);
        transferStringFromSessionToRequest(request, ERROR);
    }
}