package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is ViewSpeakersReportsAction class. Accessible by speaker. Allows to return list of speakers reports.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ViewSpeakersReportsAction implements Action {
    private final ReportService reportService;

    /**
     * @param appContext contains ReportService instance to use in action
     */
    public ViewSpeakersReportsAction(AppContext appContext) {
        reportService = appContext.getReportService();
    }

    /**
     * Sets speakers reports to request.
     *
     * @param request to get speaker id and put reports list in request
     * @return view speakers reports page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        long speakerId = ((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId();
        request.setAttribute(REPORTS, reportService.viewSpeakersReports(speakerId));
        log.info("List of reports was successfully returned");
        return VIEW_SPEAKERS_REPORTS_PAGE;
    }
}