package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;

import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * This is SearchUserAction class. Accessible by admin. Allows to find user from database by email.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class SearchUserAction implements Action {
    private final UserService userService;

    /**
     * @param appContext contains UserService instance to use in action
     */
    public SearchUserAction(AppContext appContext) {
        userService = appContext.getUserService();
    }

    /**
     * Obtains required path and sets user to request if it finds
     *
     * @param request to get user email and put user in request or error if it can't find user
     * @return users by email page if it finds or search user page if not
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String path = USER_BY_EMAIL_PAGE;
        String email = request.getParameter(EMAIL);
        try {
            request.setAttribute(USER, userService.getByEmail(email));
        } catch (NoSuchUserException | IncorrectFormatException e) {
            log.info(String.format("Couldn't find user - %s", e.getMessage()));
            request.setAttribute(ERROR, e.getMessage());
            request.setAttribute(EMAIL, email);
            path = SEARCH_USER_PAGE;
        }
        return path;
    }
}