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
import static ua.java.conferences.controller.actions.constants.ActionNames.EDIT_PROFILE_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.EDIT_PROFILE_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_UPDATE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.*;

class EditProfileActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        UserDTO user = getUserDTO();
        myRequest.getSession().setAttribute(LOGGED_USER, user);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        doNothing().when(userService).update(isA(UserDTO.class));
        String path = new EditProfileAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(EDIT_PROFILE_ACTION), path);
        assertEquals(SUCCEED_UPDATE, myRequest.getSession().getAttribute(MESSAGE));
        assertEquals(NEW_EMAIL, user.getEmail());
        assertEquals(NEW_NAME, user.getName());
        assertEquals(NEW_SURNAME, user.getSurname());
    }

    @Test
    void testExecuteBadPost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        UserDTO user = getUserDTO();
        myRequest.getSession().setAttribute(LOGGED_USER, user);
        setPostRequest();
        when(appContext.getUserService()).thenReturn(userService);
        doThrow(new DuplicateEmailException()).when(userService).update(isA(UserDTO.class));
        String path = new EditProfileAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(EDIT_PROFILE_ACTION), path);
        assertEquals(DUPLICATE_EMAIL, myRequest.getSession().getAttribute(ERROR));
        assertEquals(getNewUserDTO(), myRequest.getSession().getAttribute(USER));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        String path = new EditProfileAction(appContext).execute(myRequest, response);

        assertEquals(EDIT_PROFILE_PAGE, path);
        assertEquals(SUCCEED_UPDATE, myRequest.getAttribute(MESSAGE));
        assertEquals(ENTER_CORRECT_NAME, myRequest.getAttribute(ERROR));
        assertEquals(getNewUserDTO(), myRequest.getAttribute(USER));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(USER));
    }

    private static UserDTO getNewUserDTO() {
        return UserDTO.builder().id(1).email(NEW_EMAIL).name(NEW_NAME).surname(NEW_SURNAME).build();
    }

    void setPostRequest() {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(EMAIL)).thenReturn(NEW_EMAIL);
        when(request.getParameter(NAME)).thenReturn(NEW_NAME);
        when(request.getParameter(SURNAME)).thenReturn(NEW_SURNAME);
    }

    void setGetRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(GET);
        HttpSession session = myRequest.getSession();
        session.setAttribute(MESSAGE, SUCCEED_UPDATE);
        session.setAttribute(ERROR, ENTER_CORRECT_NAME);
        session.setAttribute(USER, getNewUserDTO());
    }
}