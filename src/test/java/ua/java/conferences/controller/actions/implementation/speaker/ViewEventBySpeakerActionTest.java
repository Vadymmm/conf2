package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ACCESS_DENIED;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class ViewEventBySpeakerActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);
    private final ReportService reportService = mock(ReportService.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(eventService.getSortedByUser(isA(String.class), isA(Role.class))).thenReturn(getEventDTOs());
        when(reportService.viewEventsReports(ONE)).thenReturn(getReportDTOs());
        String path = new ViewEventBySpeakerAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_EVENT_BY_SPEAKER_PAGE, path);
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertEquals(getReportDTOs(), myRequest.getAttribute(REPORTS));
        assertEquals(true, myRequest.getAttribute(IS_COMING));
    }

    @Test
    void testExecuteNoEvent() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getReportService()).thenReturn(reportService);
        when(eventService.getSortedByUser(isA(String.class), isA(Role.class)))
                .thenReturn(List.of(EventDTO.builder().build()));
        String path = new ViewEventBySpeakerAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_EVENT_BY_SPEAKER_PAGE, path);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(ERROR));
    }
}