package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.CHANGE_PASSWORD_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.CHANGE_PASSWORD_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_UPDATE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.ENTER_CORRECT_PASSWORD;

class ChangePasswordActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, UserDTO.builder().id(1).build());
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        doNothing().when(userService).changePassword(ID_ONE, PASS_VALUE, PASS_VALUE, PASS_VALUE);
        String path = new ChangePasswordAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(CHANGE_PASSWORD_ACTION), path);
        assertEquals(SUCCEED_UPDATE, myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecutePostIncorrectFormatException() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, UserDTO.builder().id(1).build());
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        doThrow(new IncorrectFormatException(ENTER_CORRECT_PASSWORD)).when(userService)
                .changePassword(isA(long.class), isA(String.class), isA(String.class), isA(String.class));
        String path = new ChangePasswordAction(appContext).execute(myRequest, response);
        assertEquals(getActionToRedirect(CHANGE_PASSWORD_ACTION), path);
        assertEquals(ENTER_CORRECT_PASSWORD, myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        String path = new ChangePasswordAction(appContext).execute(myRequest, response);

        assertEquals(CHANGE_PASSWORD_PAGE, path);
        assertEquals(SUCCEED_UPDATE, myRequest.getAttribute(MESSAGE));
        assertEquals(ENTER_CORRECT_PASSWORD, myRequest.getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(OLD_PASSWORD)).thenReturn(PASS_VALUE);
        when(request.getParameter(PASSWORD)).thenReturn(PASS_VALUE);
        when(request.getParameter(CONFIRM_PASSWORD)).thenReturn(PASS_VALUE);
    }

    void setGetRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(GET);
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, SUCCEED_UPDATE);
        session.setAttribute(ERROR, ENTER_CORRECT_PASSWORD);
    }
}