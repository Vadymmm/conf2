package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.ReportService;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_DELETE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class DeleteReportActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest();
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(reportService).delete(isA(String.class));
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        String path = new DeleteReportAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, ONE), path);
        assertEquals(SUCCEED_DELETE, myRequest.getSession().getAttribute(MESSAGE));
    }

    private void setRequest() {
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(request.getParameter(EMAIL)).thenReturn(EMAIL_VALUE);
        when(request.getParameter(TOPIC)).thenReturn(TOPIC_VALUE);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(request.getParameter(DATE)).thenReturn(FUTURE_DATE);
        when(request.getParameter(LOCATION)).thenReturn(LOCATION_VALUE);
        when(request.getParameter(NAME)).thenReturn(NAME_VALUE);
    }
}