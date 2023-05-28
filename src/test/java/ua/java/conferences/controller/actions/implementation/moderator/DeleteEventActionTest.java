package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.DELETE_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.SEARCH_EVENT_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_DELETE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class DeleteEventActionTest {
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
        doNothing().when(eventService).delete(isA(String.class));
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        when(userService.getParticipants(isA(String.class), isA(Role.class))).thenReturn(getUserDTOs());
        String path = new DeleteEventAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(DELETE_EVENT_ACTION), path);
        assertEquals(SUCCEED_DELETE, myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getMethod()).thenReturn(GET);
        myRequest.getSession().setAttribute(MESSAGE, SUCCEED_DELETE);

        assertEquals(SEARCH_EVENT_PAGE, new DeleteEventAction(appContext).execute(myRequest, response));
        assertEquals(SUCCEED_DELETE, myRequest.getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }

    private void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
    }
}