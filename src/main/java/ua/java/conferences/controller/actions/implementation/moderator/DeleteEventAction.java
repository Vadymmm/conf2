package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is DeleteEventAction class. Accessible by moderator. Allows to delete conference from database.
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class DeleteEventAction implements Action {
    private final EventService eventService;
    private final UserService userService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains EventService, UserService and EmailSender instances to use in action
     */
    public DeleteEventAction(AppContext appContext) {
        eventService = appContext.getEventService();
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
     * @param request to get message attribute from session and put it in request
     * @return search event page after trying to delete conference
     */
    private String executeGet(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, MESSAGE);
        return SEARCH_EVENT_PAGE;
    }

    /**
     * Called from doPost method in front-controller. Tries to delete conference from database. Sends email to all
     * visitors and speakers participate in the conference. Sends email using multithreading to make it faster so
     * moderator will not wail till all emails are send.
     *
     * @param request to get event id and set message in case of successful deleting
     * @return path to redirect to executeGet method through front-controller
     */
    private String executePost(HttpServletRequest request) throws ServiceException {
        String eventId = request.getParameter(EVENT_ID);
        String title = request.getParameter(TITLE);
        sendEmail(eventId, title);
        eventService.delete(eventId);
        log.info(String.format("Event %s was deleted", title));
        request.getSession().setAttribute(MESSAGE, SUCCEED_DELETE);
        return getActionToRedirect(DELETE_EVENT_ACTION);
    }


    private void sendEmail(String eventId, String title) throws ServiceException {
        for (UserDTO participant : userService.getParticipants(eventId, Role.VISITOR)) {
            new Thread(
                    () -> {
                        String body = String.format(MESSAGE_EVENT_DELETED, participant.getName(), title);
                        emailSender.send(SUBJECT_NOTIFICATION, body, participant.getEmail());})
                    .start();

        }
        for (UserDTO participant : userService.getParticipants(eventId, Role.SPEAKER)) {
            new Thread(
                    () -> {
                        String body = String.format(MESSAGE_EVENT_DELETED, participant.getName(), title);
                        emailSender.send(SUBJECT_NOTIFICATION, body, participant.getEmail());})
                    .start();
        }
    }
}