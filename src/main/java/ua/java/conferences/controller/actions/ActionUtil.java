package ua.java.conferences.controller.actions;

import jakarta.servlet.http.HttpServletRequest;
import ua.java.conferences.dto.*;

import java.util.StringJoiner;

import static ua.java.conferences.controller.actions.constants.Pages.CONTROLLER_PAGE;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * ActionUtil  class. Contains utils methods to use in actions.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class ActionUtil {

    /**
     * Checks if method is POST method
     * @param request passed by action
     * @return true if POST method
     */
    public static boolean isPostMethod(HttpServletRequest request) {
        return request.getMethod().equals("POST");
    }

    /**
     * Transfers sessions attributes to request. Delete then
     * @param request passed by action
     */
    public static void transferUserDTOFromSessionToRequest(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER);
        if (user != null) {
            request.setAttribute(USER, user);
            request.getSession().removeAttribute(USER);
        }
    }

    /**
     * Transfers sessions attributes to request. Delete then
     * @param request passed by action
     * @param attributeName - name of attribute to transfer from session to request
     */
    public static void transferEventDTOFromSessionToRequest(HttpServletRequest request, String attributeName) {
        EventDTO event = (EventDTO) request.getSession().getAttribute(EVENT);
        if (event != null) {
            request.setAttribute(attributeName, event);
            request.getSession().removeAttribute(attributeName);
        }
    }

    /**
     * Transfers sessions attributes to request. Delete then
     * @param request passed by action
     * @param attributeName - name of attribute to transfer from session to request
     */
    public static void transferStringFromSessionToRequest(HttpServletRequest request, String attributeName) {
        String attributeValue = (String) request.getSession().getAttribute(attributeName);
        if (attributeValue != null) {
            request.setAttribute(attributeName, attributeValue);
            request.getSession().removeAttribute(attributeName);
        }
    }

    /**
     * Creates path to another Action
     * @param action - Action to be sent
     * @param parameters - required parameters
     * @return - path
     */
    public static String getActionToRedirect(String action, String... parameters) {
        String base = CONTROLLER_PAGE + "?" + ACTION + "=" + action;
        StringJoiner stringJoiner = new StringJoiner("&", "&", "").setEmptyValue("");
        for (int i = 0; i < parameters.length; i+=2) {
            stringJoiner.add(parameters[i] + "=" + parameters[i + 1]);
        }
        return base + stringJoiner;
    }

    /**
     * Obtain Web App domain address. Common usage - email sender
     * @param request passed by action
     * @return - Web App domain address
     */
    public static String getURL(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String requestURL = request.getRequestURL().toString();
        return requestURL.replace(servletPath, "");
    }

    private ActionUtil() {}
}