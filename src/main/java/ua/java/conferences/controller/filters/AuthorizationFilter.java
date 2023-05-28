package ua.java.conferences.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static ua.java.conferences.controller.actions.constants.Pages.SIGN_IN_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ACCESS_DENIED;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.filters.domain.Domain.*;

/**
 * AuthorizationFilter class. Controls access to pages for logged user
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class AuthorizationFilter extends HttpFilter {

    /**
     * Checks for role in session and then checks if user has access to page or action.
     * @param request passed by application
     * @param response passed by application
     * @param chain passed by application
     */
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String role = (String) request.getSession().getAttribute(ROLE);
        String servletPath = request.getServletPath();
        String action = request.getParameter(ACTION);
        if (role != null && isAccessDenied(servletPath, action, role)) {
            request.setAttribute(MESSAGE, ACCESS_DENIED);
            log.warn(String.format("%s tried to access forbidden page", request.getSession().getAttribute(USER)));
            request.getRequestDispatcher(SIGN_IN_PAGE).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isAccessDenied(String servletPath, String action, String role) {
        return (getDomain(servletPath, action, role).checkAccess());
    }
}