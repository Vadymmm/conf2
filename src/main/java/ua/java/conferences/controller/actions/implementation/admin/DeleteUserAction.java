package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;

/**
 * This is DeleteUserAction class. Accessible by admin. Allows to delete user from database. Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class DeleteUserAction implements Action {
    private final UserService userService;

    /**
     * @param appContext contains UserService instance to use in action
     */
    public DeleteUserAction(AppContext appContext) {
        userService = appContext.getUserService();
    }

    /**
     * Checks method and calls required implementation
     *
     * @param request  to get method, session and set all required attributes
     * @return path to redirect or forward by front-controller
     * @throws ServiceException to call error page in front-controller
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        return isPostMethod(request) ? executePost(request) : executeGet(request);
    }

    /**
     * Called from doGet method in front-controller. Obtains required path and transfer attributes from session
     * to request
     *
     * @param request to get message attribute from session and put it in request
     * @return search user page after trying delete user
     */
    private String executeGet(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, MESSAGE);
        return SEARCH_USER_PAGE;
    }

    /**
     * Called from doPost method in front-controller. Tries to delete user from database.
     * Logs error in case if not able
     *
     * @param request to get users id and set message in case of successful deleting
     * @return path to redirect to executeGet method through front-controller
     */
    private String executePost(HttpServletRequest request) throws ServiceException {
        try {
            userService.delete(request.getParameter(USER_ID));
            request.getSession().setAttribute(MESSAGE, SUCCEED_DELETE);
        } catch (NoSuchUserException e) {
            log.info("Couldn't delete user - no such user");
        }
        return getActionToRedirect(DELETE_USER_ACTION);
    }
}