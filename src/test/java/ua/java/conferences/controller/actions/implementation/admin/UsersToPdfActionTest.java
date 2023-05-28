package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.actions.util.MyResponse;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.UserService;
import ua.java.conferences.utils.PdfUtil;
import ua.java.conferences.utils.query.QueryBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_USERS_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ASCENDING_ORDER;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.utils.QueryBuilderUtil.userQueryBuilder;

class UsersToPdfActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);
    private final ServletContext servletContext = mock(ServletContext.class);

    @Test
    void testExecute() throws ServiceException, MalformedURLException {
        setRequest();
        MyResponse myResponse = new MyResponse(response);
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOCALE, UA);
        when(appContext.getUserService()).thenReturn(userService);
        when(servletContext.getResource(FONT)).thenReturn(new URL("file:./../fonts/arial.ttf"));
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        when(appContext.getPdfUtil()).thenReturn(pdfUtil);
        when(userService.getSortedUsers(getQueryBuilder().getQuery())).thenReturn(getUserDTOs());

        String path = new UsersToPdfAction(appContext).execute(myRequest, myResponse);
        assertEquals(getActionToRedirect(VIEW_USERS_ACTION), path);

        String expected = pdfUtil.createUsersPdf(getUserDTOs(), UA).toString();
        String actual = myResponse.getOutputStream().toString();
        assertEquals(clearString(expected), clearString(actual));
    }

    @Test
    void testExecuteIOException() throws ServiceException, IOException {
        setRequest();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOCALE, EN);
        when(appContext.getUserService()).thenReturn(userService);
        when(servletContext.getResource(FONT)).thenThrow(MalformedURLException.class);
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        when(appContext.getPdfUtil()).thenReturn(pdfUtil);
        when(userService.getSortedUsers(getQueryBuilder().getQuery())).thenReturn(getUserDTOs());
        when(response.getOutputStream()).thenThrow(IOException.class);

        String path = new UsersToPdfAction(appContext).execute(myRequest, response);
        assertEquals(getActionToRedirect(VIEW_USERS_ACTION), path);
    }

    private void setRequest() {
        when(request.getParameter(ROLE)).thenReturn("3");
        when(request.getParameter(SORT)).thenReturn(NAME);
        when(request.getParameter(ORDER)).thenReturn(ASCENDING_ORDER);
    }


    private QueryBuilder getQueryBuilder() {
        String zero = "0";
        String max = String.valueOf(Integer.MAX_VALUE);
        return userQueryBuilder()
                .setRoleFilter("3")
                .setSortField(NAME)
                .setOrder(ASCENDING_ORDER)
                .setLimits(zero, max);
    }

}