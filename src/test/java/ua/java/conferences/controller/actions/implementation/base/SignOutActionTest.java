package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.SIGN_IN_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

class SignOutActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void testExecute() {
        String en = "en";
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOGGED_USER, UserDTO.builder().build());
        myRequest.getSession().setAttribute(LOCALE, en);
        assertEquals(SIGN_IN_PAGE, new SignOutAction().execute(myRequest, response));
        assertEquals(en, myRequest.getSession().getAttribute(LOCALE));
    }
}