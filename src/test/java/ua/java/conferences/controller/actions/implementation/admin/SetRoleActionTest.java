package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_USER_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.EMAIL_VALUE;

class SetRoleActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);

    @Test
    void testExecute() throws ServiceException {
        String email = EMAIL_VALUE;
        String role = Role.ADMIN.name();
        MyRequest myRequest = new MyRequest(request);
        when(request.getParameter(EMAIL)).thenReturn(email);
        when(request.getParameter(ROLE)).thenReturn(role);
        when(appContext.getUserService()).thenReturn(userService);
        doNothing().when(userService).setRole(email, 1);
        UserDTO user = UserDTO.builder().role(role).build();
        when(userService.getByEmail(email)).thenReturn(user);

        String path = new SetRoleAction(appContext).execute(myRequest, response);
        assertEquals(getActionToRedirect(SEARCH_USER_ACTION, EMAIL, email), path);
        assertEquals(user, myRequest.getAttribute(USER));
    }
}