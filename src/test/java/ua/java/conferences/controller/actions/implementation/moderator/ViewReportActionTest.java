package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.VIEW_REPORT_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.NO_REPORT;

class ViewReportActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest();
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(reportService.getById(ONE)).thenReturn(getReportDTO());
        when(userService.getSpeakers()).thenReturn(getUserDTOs());
        String path = new ViewReportAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_REPORT_PAGE, path);
        assertTrue((Boolean) myRequest.getAttribute(IS_COMING));
        assertEquals(getReportDTO(), myRequest.getAttribute(REPORT));
        assertEquals(getUserDTOs(), myRequest.getAttribute(SPEAKERS));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testBadExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest();
        when(appContext.getReportService()).thenReturn(reportService);
        doThrow(new NoSuchReportException()).when(reportService).getById(isA(String.class));
        String path = new ViewReportAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_REPORT_PAGE, path);
        assertEquals(NO_REPORT, myRequest.getAttribute(ERROR));
        assertNull(myRequest.getAttribute(IS_COMING));
        assertNull(myRequest.getAttribute(REPORT));
        assertNull(myRequest.getAttribute(SPEAKERS));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testTransfer() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest();
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, SUCCEED_ADD);
        session.setAttribute(ERROR, NO_REPORT);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(reportService.getById(ONE)).thenReturn(getReportDTO());
        when(userService.getSpeakers()).thenReturn(getUserDTOs());
        new ViewReportAction(appContext).execute(myRequest, response);

        assertEquals(SUCCEED_ADD, myRequest.getAttribute(MESSAGE));
        assertEquals(NO_REPORT, myRequest.getAttribute(ERROR));
        assertNull(session.getAttribute(MESSAGE));
        assertNull(session.getAttribute(ERROR));
    }

    private void setRequest() {
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
    }
}