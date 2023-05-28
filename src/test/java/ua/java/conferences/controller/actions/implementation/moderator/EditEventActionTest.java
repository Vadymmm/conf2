package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.DUPLICATE_TITLE;

class EditEventActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);
    private final EventService eventService = mock(EventService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getEventService()).thenReturn(eventService);
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(eventService).update(isA(EventDTO.class));
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        when(userService.getParticipants(isA(String.class), isA(Role.class))).thenReturn(getUserDTOs());
        String path = new EditEventAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(EDIT_EVENT_ACTION, EVENT_ID, ONE), path);
        assertEquals(SUCCEED_UPDATE, myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecuteBadPost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getEventService()).thenReturn(eventService);
        doThrow(new DuplicateTitleException()).when(eventService).update(isA(EventDTO.class));
        String path = new EditEventAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(EDIT_EVENT_ACTION, EVENT_ID, ONE), path);
        assertEquals(DUPLICATE_TITLE, myRequest.getSession().getAttribute(ERROR));
        assertEquals(getEventDTO(), myRequest.getSession().getAttribute(EVENT_NEW));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest, getEventDTO());
        when(appContext.getEventService()).thenReturn(eventService);
        when(eventService.getById(ONE)).thenReturn(getEventDTO());
        String path = new EditEventAction(appContext).execute(myRequest, response);

        assertEquals(EDIT_EVENT_PAGE, path);
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT_NEW));
        assertEquals(SUCCEED_UPDATE, myRequest.getAttribute(MESSAGE));
        assertEquals(DUPLICATE_TITLE, myRequest.getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(EVENT_NEW));
    }

    @Test
    void testExecuteBadGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest, getPassedEventDTO());
        when(appContext.getEventService()).thenReturn(eventService);
        when(eventService.getById(ONE)).thenReturn(getPassedEventDTO());
        String path = new EditEventAction(appContext).execute(myRequest, response);

        assertEquals(EDIT_EVENT_PAGE, path);
        assertEquals(ERROR_EVENT_EDIT, myRequest.getAttribute(ERROR));
    }

    private void setGetRequest(MyRequest myRequest, EventDTO event) {
        when(request.getMethod()).thenReturn(GET);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, SUCCEED_UPDATE);
        session.setAttribute(ERROR, DUPLICATE_TITLE);
        session.setAttribute(EVENT, event);
    }

    private void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(request.getParameter(DATE)).thenReturn(FUTURE_DATE);
        when(request.getParameter(LOCATION)).thenReturn(LOCATION_VALUE);
        when(request.getParameter(DESCRIPTION)).thenReturn(DESCRIPTION_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
    }
}