package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.PASSWORD_RESET_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.CHECK_EMAIL;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is ResetPasswordAction class. Accessible by any user. Allows to reset user's password.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ResetPasswordAction implements Action {
    private final UserService userService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains UserService and EmailSender instances to use in action
     */
    public ResetPasswordAction(AppContext appContext) {
        userService = appContext.getUserService();
        emailSender = appContext.getEmailSender();
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
     * @param request to get message, email and/or error attribute from session and put it in request
     * @return reset password page
     */
    private String executeGet(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, EMAIL);
        transferStringFromSessionToRequest(request, ERROR);
        transferStringFromSessionToRequest(request, MESSAGE);
        return RESET_PASSWORD_PAGE;
    }

    /**
     * Called from doPost method in front-controller. Tries to reset users password via service. Gets email from
     * request. Sends email to user with new password if reset was successful.
     *
     * @param request to get users id and all passwords. Also, to set message in case of successful deleting and error
     * in another case
     *
     * @return path to redirect to executeGet method through front-controller
     */
    private String executePost(HttpServletRequest request) throws ServiceException {
        String email = request.getParameter(EMAIL);
        request.getSession().setAttribute(EMAIL, email);
        try {
            UserDTO user = userService.getByEmail(email);
            String newPass = userService.changePassword(user.getId());
            request.getSession().setAttribute(MESSAGE, CHECK_EMAIL);
            sendEmail(user, newPass, getURL(request));
            log.info(String.format("%s was successfully reset his password", email));
        } catch (IncorrectFormatException | NoSuchUserException e) {
            request.getSession().setAttribute(ERROR, e.getMessage());
            log.info(String.format("%s couldn't reset his password", email));
        }
        return getActionToRedirect(PASSWORD_RESET_ACTION);
    }

    private void sendEmail(UserDTO user, String newPass, String url)  {
        String body = String.format(MESSAGE_RESET_PASSWORD, user.getName(), newPass, url);
        new Thread(() -> emailSender.send(SUBJECT_NOTIFICATION, body, user.getEmail())).start();
    }
}