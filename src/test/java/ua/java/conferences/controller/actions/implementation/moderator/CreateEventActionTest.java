package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.exceptions.DuplicateTitleException;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.EventService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.CREATE_EVENT_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.DUPLICATE_TITLE;

class CreateEventActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);


    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getEventService()).thenReturn(eventService);
        doNothing().when(eventService).addEvent(getEventDTO());
        when(eventService.getByTitle(TITLE_VALUE)).thenReturn(getEventDTO());
        String path = new CreateEventAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID,ONE), path);
    }

    @Test
    void testExecuteBadPost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getEventService()).thenReturn(eventService);
        doThrow(new DuplicateTitleException()).when(eventService).addEvent(isA(EventDTO.class));
        String path = new CreateEventAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(CREATE_EVENT_ACTION), path);
        assertEquals(getEventDTO(), myRequest.getSession().getAttribute(EVENT));
        assertEquals(DUPLICATE_TITLE, myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getMethod()).thenReturn(GET);
        myRequest.getSession().setAttribute(ERROR, DUPLICATE_TITLE);
        myRequest.getSession().setAttribute(EVENT, getEventDTO());

        assertEquals(CREATE_EVENT_PAGE, new CreateEventAction(appContext).execute(myRequest, response));
        assertEquals(DUPLICATE_TITLE, myRequest.getAttribute(ERROR));
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertNull(myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(EVENT));
    }

    private void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(request.getParameter(DATE)).thenReturn(FUTURE_DATE);
        when(request.getParameter(LOCATION)).thenReturn(LOCATION_VALUE);
        when(request.getParameter(DESCRIPTION)).thenReturn(DESCRIPTION_VALUE);

    }
}