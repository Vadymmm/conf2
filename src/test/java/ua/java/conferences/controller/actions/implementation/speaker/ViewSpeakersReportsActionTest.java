package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.ReportService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.VIEW_SPEAKERS_REPORTS_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class ViewSpeakersReportsActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
        when(appContext.getReportService()).thenReturn(reportService);
        when(reportService.viewSpeakersReports(ID_ONE)).thenReturn(getReportDTOs());

        assertEquals(VIEW_SPEAKERS_REPORTS_PAGE, new ViewSpeakersReportsAction(appContext).execute(myRequest, response));
        assertEquals(getReportDTOs(), myRequest.getAttribute(REPORTS));
    }
}