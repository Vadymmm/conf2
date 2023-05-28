package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.actions.Action;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.UserService;
import ua.java.conferences.utils.PdfUtil;
import ua.java.conferences.utils.query.QueryBuilder;

import java.io.*;
import java.util.List;

import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_USERS_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.QueryBuilderUtil.userQueryBuilder;

/**
 * This is UsersToPdfAction class. Accessible by admin. Allows to download list of all users that match demands
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class UsersToPdfAction implements Action {
    private final UserService userService;
    private final PdfUtil pdfUtil;

    /**
     * @param appContext contains UserService and PdfUtil instances to use in action
     */
    public UsersToPdfAction(AppContext appContext) {
        userService = appContext.getUserService();
        pdfUtil = appContext.getPdfUtil();
    }

    /**
     * Builds required query for service, sets users list in response to download. Checks for locale to set up
     * locale for pdf document
     *
     * @param request to get queries parameters
     * @param response to set users list there
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        QueryBuilder queryBuilder = getQueryBuilder(request);
        List<UserDTO> users = userService.getSortedUsers(queryBuilder.getQuery());
        String locale = (String) request.getSession().getAttribute(LOCALE);
        ByteArrayOutputStream usersPdf = pdfUtil.createUsersPdf(users, locale);
        setResponse(response, usersPdf);
        return getActionToRedirect(VIEW_USERS_ACTION);
    }

    private QueryBuilder getQueryBuilder(HttpServletRequest request) {
        String zero = "0";
        String max = String.valueOf(Integer.MAX_VALUE);
        return userQueryBuilder()
                .setRoleFilter(request.getParameter(ROLE))
                .setSortField(request.getParameter(SORT))
                .setOrder(request.getParameter(ORDER))
                .setLimits(zero, max);
    }

    /**
     * Sets users list in response to download. Configure response to download pdf document
     *
     * @param response to set users list there
     * @param output - output stream that contains pdf document
     */
    private void setResponse(HttpServletResponse response, ByteArrayOutputStream output) {
        response.setContentType("application/pdf");
        response.setContentLength(output.size());
        response.setHeader("Content-Disposition", "attachment; filename=\"users.pdf\"");
        try (OutputStream outputStream = response.getOutputStream()) {
            output.writeTo(outputStream);
            outputStream.flush();
            log.info("User list was downloaded");
        } catch (IOException e) {
            log.error(String.format("Couldn't set user list to download because of %s", e.getMessage()));
        }
    }
}