package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is SetRoleAction class. Accessible by admin. Allows to set users role. Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class SetRoleAction implements Action {
    private final UserService userService;

    /**
     * @param appContext contains UserService instance to use in action
     */
    public SetRoleAction(AppContext appContext) {
        userService = appContext.getUserService();
    }

    /**
     * Obtains required path and sets users role
     *
     * @param request to get user email and new role and put user in request
     * @return path to redirect to executeGet method in SearchUserAction through front-controller with required
     * parameters to find user.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String email = request.getParameter(EMAIL);
        String role = request.getParameter(ROLE);
        int roleId = Role.valueOf(role).getValue();
        userService.setRole(email, roleId);
        log.info(String.format("User %s got new role: %s", email, role));
        request.setAttribute(USER, userService.getByEmail(email));
        return getActionToRedirect(SEARCH_USER_ACTION, EMAIL, email);
    }
}
