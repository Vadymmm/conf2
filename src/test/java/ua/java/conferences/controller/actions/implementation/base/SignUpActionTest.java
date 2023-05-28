package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;
import ua.java.conferences.utils.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_REGISTER;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.CAPTCHA_INVALID;

class SignUpActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);
    private final EmailSender emailSender = mock(EmailSender.class);
    private final Captcha captcha = mock(Captcha.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getCaptcha()).thenReturn(captcha);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(captcha).verify(isA(String.class));
        doNothing().when(userService).add(isA(UserDTO.class), isA(String.class), isA(String.class));
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        String path = new SignUpAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SIGN_IN_ACTION), path);
        assertNull(myRequest.getSession().getAttribute(USER));
        assertEquals(SUCCEED_REGISTER, myRequest.getSession().getAttribute(MESSAGE));
        assertEquals(getUserDTO().getEmail(), myRequest.getSession().getAttribute(EMAIL));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteBadPost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getCaptcha()).thenReturn(captcha);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doThrow(new CaptchaException()).when(captcha).verify(isA(String.class));
        String path = new SignUpAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SIGN_UP_ACTION), path);
        assertEquals(getUserDTO(), myRequest.getSession().getAttribute(USER));
        assertEquals(CAPTCHA_INVALID, myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(EMAIL));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        String path = new SignUpAction(appContext).execute(myRequest, response);

        assertEquals(SIGN_UP_PAGE, path);
        assertEquals(CAPTCHA_INVALID, myRequest.getAttribute(ERROR));
        assertEquals(getUserDTO() , myRequest.getAttribute(USER));
        assertNull(myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(USER));
    }

    void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(CAPTCHA)).thenReturn(CAPTCHA_VALUE);
        when(request.getParameter(EMAIL)).thenReturn(EMAIL_VALUE);
        when(request.getParameter(NAME)).thenReturn(NAME_VALUE);
        when(request.getParameter(SURNAME)).thenReturn(SURNAME_VALUE);
        when(request.getParameter(PASSWORD)).thenReturn(PASS_VALUE);
        when(request.getParameter(CONFIRM_PASSWORD)).thenReturn(PASS_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
    }

    void setGetRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(GET);
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, SUCCEED_REGISTER);
        session.setAttribute(ERROR, CAPTCHA_INVALID);
        session.setAttribute(USER, getUserDTO());
    }
}