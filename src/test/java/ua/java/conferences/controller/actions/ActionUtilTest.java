package ua.java.conferences.controller.actions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.DELETE_USER_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SET;
import static ua.java.conferences.controller.actions.constants.ParameterValues.SUCCEED_UPDATE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

class ActionUtilTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void testIsPostMethodForPost() {
        when(request.getMethod()).thenReturn("POST");
        assertTrue(isPostMethod(request));
    }

    @Test
    void testIsPostMethodForGet() {
        when(request.getMethod()).thenReturn("GET");
        assertFalse(isPostMethod(request));
    }

    @Test
    void testTransferUserDTOFromSessionToRequest() {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(USER, getTestUserDTO());
        transferUserDTOFromSessionToRequest(myRequest);
        assertEquals(getTestUserDTO(), myRequest.getAttribute(USER));
        assertNull(myRequest.getSession().getAttribute(USER));
    }

    @Test
    void testTransferUserDTOFromSessionToRequestNoUser() {
        MyRequest myRequest = new MyRequest(request);
        transferUserDTOFromSessionToRequest(myRequest);
        assertNull(myRequest.getAttribute(USER));
        assertNull(myRequest.getSession().getAttribute(USER));
    }

    @Test
    void testTransferEventDTOFromSessionToRequest() {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(EVENT, getTestEventDTO());
        transferEventDTOFromSessionToRequest(myRequest, EVENT);
        assertEquals(getTestEventDTO(), myRequest.getAttribute(EVENT));
        assertNull(myRequest.getSession().getAttribute(EVENT));
    }

    @Test
    void testTransferEventDTOFromSessionToRequestNullEvent() {
        MyRequest myRequest = new MyRequest(request);
        transferEventDTOFromSessionToRequest(myRequest, EVENT);
        assertNull(myRequest.getAttribute(EVENT));
        assertNull(myRequest.getSession().getAttribute(EVENT));
    }

    @Test
    void testTransferStringFromSessionToRequest() {
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(MESSAGE, SUCCEED_UPDATE);
        transferStringFromSessionToRequest(myRequest, MESSAGE);
        assertEquals(SUCCEED_UPDATE, myRequest.getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testTransferStringFromSessionToRequestNullString() {
        MyRequest myRequest = new MyRequest(request);
        transferStringFromSessionToRequest(myRequest, MESSAGE);
        assertNull(myRequest.getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testGetActionToRedirectNoParameters() {
        String result = "controller?action=delete-user";
        assertEquals(result, getActionToRedirect(DELETE_USER_ACTION));
    }

    @Test
    void testGetActionToRedirectWithParameter() {
        String result = "controller?action=delete-user&user-id=1";
        assertEquals(result, getActionToRedirect(DELETE_USER_ACTION, USER_ID, "1"));
    }

    @Test
    void testGetActionToRedirectWithParameters() {
        String result = "controller?action=delete-user&user-id=1&todo=set";
        assertEquals(result, getActionToRedirect(DELETE_USER_ACTION, USER_ID, "1", TODO, SET));
    }

    @Test
    void testGetURL() {
        String result = "http://localhost:8080/conferences";
        when(request.getServletPath()).thenReturn("/controller");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/conferences/controller"));
        assertEquals(result, getURL(request));
    }

    private UserDTO getTestUserDTO() {
        return UserDTO.builder()
                .id(ID_VALUE)
                .email(EMAIL_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .role(ROLE_VISITOR)
                .build();
    }

    private EventDTO getTestEventDTO() {
        return EventDTO.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_NAME)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .reports(REPORTS_VALUE)
                .registrations(REGISTRATIONS_VALUE)
                .visitors(VISITORS_VALUE)
                .build();
    }
}