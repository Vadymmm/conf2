package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.SIGN_IN_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.NO_USER;

class SignInActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.signIn(EMAIL_VALUE, PASS_VALUE)).thenReturn(getUserDTO());
        String path = new SignInAction(appContext).execute(myRequest, response);

        assertEquals(PROFILE_PAGE, path);
        assertEquals(getUserDTO(), myRequest.getSession().getAttribute(LOGGED_USER));
        assertEquals(getUserDTO().getRole(), myRequest.getSession().getAttribute(ROLE));
    }

    @Test
    void testExecuteBadPost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.signIn(EMAIL_VALUE, PASS_VALUE)).thenThrow(new NoSuchUserException());
        String path = new SignInAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SIGN_IN_ACTION), path);
        assertEquals(EMAIL_VALUE, myRequest.getSession().getAttribute(EMAIL));
        assertEquals(NO_USER, myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        String path = new SignInAction(appContext).execute(myRequest, response);

        assertEquals(SIGN_IN_PAGE, path);
        assertEquals(NO_USER, myRequest.getAttribute(ERROR));
        assertEquals(EMAIL_VALUE , myRequest.getAttribute(EMAIL));
        assertNull(myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(EMAIL));
    }

    void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(EMAIL)).thenReturn(EMAIL_VALUE);
        when(request.getParameter(PASSWORD)).thenReturn(PASS_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
    }

    void setGetRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(GET);
        HttpSession session = myRequest.getSession();
        session.setAttribute(ERROR, NO_USER);
        session.setAttribute(EMAIL, EMAIL_VALUE);
    }
}