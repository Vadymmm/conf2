package ua.java.conferences.controller.actions.implementation.visitor;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.EventService;
import ua.java.conferences.utils.query.QueryBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.VIEW_VISITORS_EVENTS_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.model.entities.role.Role.VISITOR;
import static ua.java.conferences.utils.QueryBuilderUtil.visitorEventQueryBuilder;

class ViewVisitorsEventsActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);
    private final String ZERO = "0";
    private final String FIVE = "5";

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest(myRequest);
        when(appContext.getEventService()).thenReturn(eventService);
        when(eventService.getSortedByUser(getQueryBuilder().getQuery(), VISITOR)).thenReturn(getEventDTOs());
        when(eventService.getNumberOfRecords(getQueryBuilder().getRecordQuery(), VISITOR)).thenReturn(10);
        String path = new ViewVisitorsEventsAction(appContext).execute(myRequest, response);

        assertEquals(VIEW_VISITORS_EVENTS_PAGE, path);
        assertEquals(getEventDTOs(), myRequest.getAttribute(EVENTS));
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(2, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(2, myRequest.getAttribute(END));
    }

    private void setRequest(MyRequest myRequest) {
        when(request.getParameter(DATE)).thenReturn(UPCOMING);
        when(request.getParameter(SORT)).thenReturn(TITLE);
        when(request.getParameter(ORDER)).thenReturn(DESCENDING_ORDER);
        when(request.getParameter(OFFSET)).thenReturn(ZERO);
        when(request.getParameter(RECORDS)).thenReturn(FIVE);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
    }

    private QueryBuilder getQueryBuilder() {
        return visitorEventQueryBuilder()
                .setUserIdFilter(ID_ONE)
                .setDateFilter(UPCOMING)
                .setSortField(TITLE)
                .setOrder(DESCENDING_ORDER)
                .setLimits(ZERO, FIVE);
    }
}