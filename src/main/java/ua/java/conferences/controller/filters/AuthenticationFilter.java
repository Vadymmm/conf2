package ua.java.conferences.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static ua.java.conferences.controller.actions.constants.Pages.SIGN_IN_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ACCESS_DENIED;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.filters.domain.Domain.getDomain;

/**
 * AuthorizationFilter class. Controls access to pages for anonymous user
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class AuthenticationFilter extends HttpFilter {
    /**
     * Checks user in session and then checks if user has access to page or action.
     * @param request passed by application
     * @param response passed by application
     * @param chain passed by application
     */
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String servletPath = request.getServletPath();
        String action = request.getParameter(ACTION);
        if (isNoLoggedUser(request) && isAccessDenied(servletPath, action)) {
            log.info("Anonymous user tried to access forbidden page");
            request.setAttribute(MESSAGE, ACCESS_DENIED);
            request.getRequestDispatcher(SIGN_IN_PAGE).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static boolean isNoLoggedUser(HttpServletRequest request) {
        return request.getSession().getAttribute(LOGGED_USER) == null;
    }

    private boolean isAccessDenied(String servletPath, String action) {
        return getDomain(servletPath, action).checkAccess();
    }
}