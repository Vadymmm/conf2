package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.DELETE_USER_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.SEARCH_USER_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_DELETE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class DeleteUserActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(USER_ID)).thenReturn(ONE);
        when(appContext.getUserService()).thenReturn(userService);
        doNothing().when(userService).delete(ONE);
        String path = new DeleteUserAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(DELETE_USER_ACTION), path);
        assertEquals(SUCCEED_DELETE, myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecutePostNull() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(USER_ID)).thenReturn(null);
        when(appContext.getUserService()).thenReturn(userService);
        doThrow(NoSuchUserException.class).when(userService).delete(null);
        String path = new DeleteUserAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(DELETE_USER_ACTION), path);
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        when(request.getMethod()).thenReturn(GET);
        myRequest.getSession().setAttribute(MESSAGE, SUCCEED_DELETE);

        assertEquals(SEARCH_USER_PAGE, new DeleteUserAction(appContext).execute(myRequest, response));
        assertEquals(SUCCEED_DELETE, myRequest.getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }
}