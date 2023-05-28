package ua.java.conferences.controller.actions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.java.conferences.exceptions.ServiceException;

/**
 * Action interface. Implement it to create new actions
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public interface Action {

    /**
     * Obtains path to sendRedirect or forward in front-controller. Edits request and response if needed.
     *
     * @param request passed by controller
     * @param response passed by controller
     * @return path to return to front-controller
     * @throws ServiceException - any unhandled exception. Will cause front-controller to redirect to error page
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}