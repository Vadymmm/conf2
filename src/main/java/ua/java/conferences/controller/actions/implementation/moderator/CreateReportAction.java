package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is CreateReportAction class. Accessible by moderator. Allows to create new report. Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class CreateReportAction implements Action {
    private final ReportService reportService;

    /**
     * @param appContext contains ReportService instance to use in action
     */
    public CreateReportAction(AppContext appContext) {
        reportService = appContext.getReportService();
    }

    /**
     * Tries to create new report in conference. Obtains required path and sets error if not successful
     *
     * @param request to get reports fields
     * @return path to redirect to executeGet in SearchEventAction with required parameters
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        ReportDTO report = getReportDTO(request);
        try {
            reportService.addReport(report);
            log.info(String.format("Report %s was created", report.getTopic()));
        } catch (IncorrectFormatException e) {
            log.info(String.format("Couldn't create report %s because of %s", report.getTopic(), e.getMessage()));
            request.getSession().setAttribute(ERROR, e.getMessage());
        }
        return getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, String.valueOf(report.getEventId()));
    }

    private ReportDTO getReportDTO(HttpServletRequest request) {
        return ReportDTO.builder()
                .topic(request.getParameter(TOPIC))
                .eventId(Long.parseLong(request.getParameter(EVENT_ID)))
                .build();
    }
}