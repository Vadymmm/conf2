package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.dto.EventDTO;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import java.time.LocalDate;

import static ua.java.conferences.controller.actions.ActionUtil.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.Pages.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.utils.constants.Email.*;

/**
 * This is EditEventAction class. Accessible by moderator. Allows to edit conference
 * Implements PRG pattern
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class EditEventAction implements Action {
    private final EventService eventService;
    private final UserService userService;
    private final EmailSender emailSender;

    /**
     * @param appContext contains EventService, UserService and EmailSender instances to use in action
     */
    public EditEventAction(AppContext appContext) {
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
     * @param request to get message, error and event attributes from session and put it in request
     * @return search event page after trying to delete conference
     */
    private String executeGet(HttpServletRequest request) throws ServiceException {
        transferAttributes(request);
        String eventId = request.getParameter(EVENT_ID);
        try {
            EventDTO event = getEvent(eventId);
            request.setAttribute(EVENT, event);
            log.info(String.format("Event %s was successfully changed ", event.getTitle()));
        } catch (NoSuchEventException e) {
            log.info(String.format("Couldn't change event because of %s", e.getMessage()));
            request.setAttribute(ERROR, e.getMessage());
        }
        return EDIT_EVENT_PAGE;
    }

    private EventDTO getEvent(String eventId) throws ServiceException {
        EventDTO event = eventService.getById(eventId);
        if (LocalDate.now().isAfter(LocalDate.parse(event.getDate()))) {
            throw new NoSuchEventException(ERROR_EVENT_EDIT);
        }
        return event;
    }

    private void transferAttributes(HttpServletRequest request) {
        transferStringFromSessionToRequest(request, MESSAGE);
        transferStringFromSessionToRequest(request, ERROR);
        transferEventDTOFromSessionToRequest(request, EVENT_NEW);
    }

    /**
     * Called from doPost method in front-controller. Tries to edit conference. Sends email to all visitors and
     * speakers participate in the conference. Sends email using multithreading to make it faster so
     * moderator will not wail till all emails are send.
     *
     * @param request to get event id and set message in case of successful updating or error
     * @return path to redirect to executeGet method through front-controller with required parameters
     */
    private String executePost(HttpServletRequest request) throws ServiceException {
        EventDTO event = getEventDTO(request);
        try {
            eventService.update(event);
            request.getSession().setAttribute(MESSAGE, SUCCEED_UPDATE);
            log.info(String.format("Event %s was successfully changed ", event.getTitle()));
            sendEmail(event, getURL(request));
        } catch (IncorrectFormatException | DuplicateTitleException e) {
            request.getSession().setAttribute(EVENT_NEW, event);
            request.getSession().setAttribute(ERROR, e.getMessage());
            log.info(String.format("Couldn't change event %s because of %s", event.getTitle(), e.getMessage()));
        }
        return getActionToRedirect(EDIT_EVENT_ACTION, EVENT_ID, String.valueOf(event.getId()));
    }

    private EventDTO getEventDTO(HttpServletRequest request) {
        return EventDTO.builder()
                .id(Long.parseLong(request.getParameter(EVENT_ID)))
                .title(request.getParameter(TITLE))
                .date(request.getParameter(DATE))
                .location(request.getParameter(LOCATION))
                .description(request.getParameter(DESCRIPTION))
                .build();
    }

    private void sendEmail(EventDTO event, String url) throws ServiceException {
        String eventId = String.valueOf(event.getId());
        String title = event.getTitle();
        String date = event.getDate();
        String location = event.getLocation();
        String description = event.getDescription();
        for (UserDTO participant : userService.getParticipants(eventId, Role.VISITOR)) {
            new Thread(
                    () -> {
                        String body = String.format(MESSAGE_EVENT_CHANGED_VISITOR, participant.getName(), title,
                                date, location, description, url, eventId);
                        emailSender.send(SUBJECT_NOTIFICATION, body, participant.getEmail());})
                    .start();

        }
        for (UserDTO participant : userService.getParticipants(eventId, Role.SPEAKER)) {
            new Thread(
                    () -> {
                        String body = String.format(MESSAGE_EVENT_CHANGED_SPEAKER, participant.getName(), title,
                                date, location, description, url, eventId);
                        emailSender.send(SUBJECT_NOTIFICATION, body, participant.getEmail());})
                    .start();
        }
    }
}