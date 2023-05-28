package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_DELETE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.NO_EVENT;

class SearchEventActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);
    private final ReportService reportService = mock(ReportService.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, SUCCEED_DELETE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(eventService.getByTitle(TITLE_VALUE)).thenReturn(getEventDTO());
        when(reportService.viewEventsReports(ONE)).thenReturn(getReportDTOs());
        String path = new SearchEventAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_EVENT_PAGE, path);
        assertEquals(SUCCEED_DELETE, myRequest.getAttribute(MESSAGE));
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertEquals(getReportDTOs(), myRequest.getAttribute(REPORTS));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecuteNoTitle() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(eventService.getById(ONE)).thenReturn(getEventDTO());
        when(reportService.viewEventsReports(ONE)).thenReturn(getReportDTOs());
        String path = new SearchEventAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_EVENT_PAGE, path);
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertEquals(getReportDTOs(), myRequest.getAttribute(REPORTS));
    }

    @Test
    void testExecuteNoEvent() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(eventService.getByTitle(TITLE_VALUE)).thenThrow(new NoSuchEventException());
        String path = new SearchEventAction(appContext).execute(myRequest, response);

        assertEquals(SEARCH_EVENT_PAGE, path);
        assertEquals(NO_EVENT, myRequest.getAttribute(ERROR));
        assertEquals(TITLE_VALUE, myRequest.getAttribute(TITLE));
    }
}