package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.PASSWORD_RESET_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.RESET_PASSWORD_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.CHECK_EMAIL;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.*;

class ResetPasswordActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        when(userService.getByEmail(EMAIL_VALUE)).thenReturn(getUserDTO());
        when(userService.changePassword(getUserDTO().getId())).thenReturn(PASS_VALUE);
        String path = new ResetPasswordAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(PASSWORD_RESET_ACTION), path);
        assertEquals(EMAIL_VALUE, myRequest.getSession().getAttribute(EMAIL));
        assertEquals(CHECK_EMAIL, myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteBadPost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        doThrow(new NoSuchUserException()).when(userService).getByEmail(isA(String.class));
        String path = new ResetPasswordAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(PASSWORD_RESET_ACTION), path);
        assertEquals(NO_USER, myRequest.getSession().getAttribute(ERROR));
        assertEquals(EMAIL_VALUE, myRequest.getSession().getAttribute(EMAIL));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        String path = new ResetPasswordAction(appContext).execute(myRequest, response);

        assertEquals(RESET_PASSWORD_PAGE, path);
        assertEquals(CHECK_EMAIL, myRequest.getAttribute(MESSAGE));
        assertEquals(NO_USER, myRequest.getAttribute(ERROR));
        assertEquals(EMAIL_VALUE , myRequest.getAttribute(EMAIL));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(EMAIL));
    }

    void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(EMAIL)).thenReturn(EMAIL_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
    }

    void setGetRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(GET);
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, CHECK_EMAIL);
        session.setAttribute(ERROR, NO_USER);
        session.setAttribute(EMAIL, EMAIL_VALUE);
    }
}