package ua.java.conferences.controller.actions.implementation.visitor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_EVENT_BY_VISITOR_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is RegisterOrCancelAction class. Accessible by visitor. Allows to register or cancel registrations for event.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class RegisterOrCancelAction implements Action {

    private final UserService userService;

    /**
     * @param appContext contains UserService instance to use in action
     */
    public RegisterOrCancelAction(AppContext appContext) {
        userService = appContext.getUserService();
    }

    /**
     * Obtains required path and change user registration status for event.
     *
     * @param request to get user and event ids
     * @return path to redirect to executeGet method in ViewEventByVisitorAction through front-controller with required
     * parameters.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        long userId = ((UserDTO) request.getSession().getAttribute(LOGGED_USER)).getId();
        String eventId = request.getParameter(EVENT_ID);
        registerOrCancel(userId, eventId, request.getParameter(TODO));
        return getActionToRedirect(VIEW_EVENT_BY_VISITOR_ACTION, EVENT_ID, eventId);
    }

    private void registerOrCancel(long userId, String eventId, String todo) throws ServiceException {
        if (todo.equals(REGISTER)) {
            userService.registerForEvent(userId, eventId);
            log.info(String.format("User with id=%d registered for event with id=%s", userId, eventId));
        } else if (todo.equals(CANCEL)) {
            userService.cancelRegistration(userId, eventId);
            log.info(String.format("User with id=%d cancel it's registration for event with id=%s", userId, eventId));
        }
    }
}
