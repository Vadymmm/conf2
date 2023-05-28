package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.ReportService;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_REPORT_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_UPDATE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.ENTER_CORRECT_TOPIC;

class ChangeTopicActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecute() throws ServiceException {
        setRequest();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(reportService).update(isA(ReportDTO.class));
        when(reportService.getById(ONE)).thenReturn(getReportDTO());
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        String path = new ChangeTopicAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, ONE), path);
        assertEquals(SUCCEED_UPDATE, myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testBadExecute() throws ServiceException {
        setRequest();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getReportService()).thenReturn(reportService);
        doThrow(new IncorrectFormatException(ENTER_CORRECT_TOPIC)).when(reportService).update(isA(ReportDTO.class));
        String path = new ChangeTopicAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, ONE), path);
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertEquals(ENTER_CORRECT_TOPIC, myRequest.getSession().getAttribute(ERROR));
    }

    private void setRequest(){
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
        when(request.getParameter(TOPIC)).thenReturn(TOPIC_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
    }
}