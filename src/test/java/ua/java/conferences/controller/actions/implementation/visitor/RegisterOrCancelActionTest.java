package ua.java.conferences.controller.actions.implementation.visitor;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_EVENT_BY_VISITOR_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class RegisterOrCancelActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecuteRegister() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest(myRequest);
        when(request.getParameter(TODO)).thenReturn(REGISTER);
        when(appContext.getUserService()).thenReturn(userService);
        doNothing().when(userService).registerForEvent(ID_ONE, ONE);
        String path = new RegisterOrCancelAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_EVENT_BY_VISITOR_ACTION, EVENT_ID, ONE), path);
    }

    @Test
    void testExecuteCancel() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest(myRequest);
        when(request.getParameter(TODO)).thenReturn(CANCEL);
        when(appContext.getUserService()).thenReturn(userService);
        doNothing().when(userService).cancelRegistration(ID_ONE, ONE);
        String path = new RegisterOrCancelAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_EVENT_BY_VISITOR_ACTION, EVENT_ID, ONE), path);
    }

    private void setRequest(MyRequest myRequest) {
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
    }
}