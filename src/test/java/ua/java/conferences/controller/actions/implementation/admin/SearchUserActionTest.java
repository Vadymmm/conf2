package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.*;

class SearchUserActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecute() throws ServiceException {
        String email = EMAIL_VALUE;
        when(request.getParameter(EMAIL)).thenReturn(email);
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.getByEmail(email)).thenReturn(getUserDTO());

        assertEquals(USER_BY_EMAIL_PAGE, new SearchUserAction(appContext).execute(myRequest, response));
        assertEquals(getUserDTO(), myRequest.getAttribute(USER));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email.com", "@email.com", "email@email"})
    void testExecuteBadEmails(String email) throws ServiceException {
        when(request.getParameter(EMAIL)).thenReturn(email);
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.getByEmail(email)).thenThrow(new IncorrectFormatException(ENTER_CORRECT_EMAIL));

        assertEquals(SEARCH_USER_PAGE, new SearchUserAction(appContext).execute(myRequest, response));
        assertEquals(ENTER_CORRECT_EMAIL, myRequest.getAttribute(ERROR));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testExecuteNoEmails(String email) throws ServiceException {
        when(request.getParameter(EMAIL)).thenReturn(email);
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.getByEmail(email)).thenThrow(new IncorrectFormatException(ENTER_CORRECT_EMAIL));

        assertEquals(SEARCH_USER_PAGE, new SearchUserAction(appContext).execute(myRequest, response));
        assertEquals(ENTER_CORRECT_EMAIL, myRequest.getAttribute(ERROR));
    }

    @ParameterizedTest
    @ValueSource(strings = {"noUser@epam.com", "noUser2@epam.com"})
    void testExecuteNoUser(String email) throws ServiceException {
        when(request.getParameter(EMAIL)).thenReturn(email);
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.getByEmail(email)).thenThrow(new NoSuchUserException());

        assertEquals(SEARCH_USER_PAGE, new SearchUserAction(appContext).execute(myRequest, response));
        assertEquals(NO_USER, myRequest.getAttribute(ERROR));
    }
}