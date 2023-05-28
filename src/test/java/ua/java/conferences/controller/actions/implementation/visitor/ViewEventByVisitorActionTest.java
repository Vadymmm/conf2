package ua.java.conferences.controller.actions.implementation.visitor;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.VIEW_EVENT_BY_VISITOR_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.NO_EVENT;

class ViewEventByVisitorActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);
    private final ReportService reportService = mock(ReportService.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(eventService.getById(ONE)).thenReturn(getEventDTO());
        when(reportService.viewEventsReports(ONE)).thenReturn(getReportDTOs());
        when(userService.isRegistered(ID_ONE, ONE)).thenReturn(true);
        String path = new ViewEventByVisitorAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_EVENT_BY_VISITOR_PAGE, path);
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertEquals(getReportDTOs(), myRequest.getAttribute(REPORTS));
        assertEquals(true, myRequest.getAttribute(IS_REGISTERED));
        assertEquals(true, myRequest.getAttribute(IS_COMING));
    }

    @Test
    void testExecuteNoEvent() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(eventService.getById(ONE)).thenThrow(new NoSuchEventException());
        String path = new ViewEventByVisitorAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_EVENT_BY_VISITOR_PAGE, path);
        assertEquals(NO_EVENT, myRequest.getAttribute(ERROR));
    }
}