package ua.java.conferences.controller.actions.implementation.admin;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.UserService;
import ua.java.conferences.utils.query.QueryBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.VIEW_USERS_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.DESCENDING_ORDER;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.getUserDTOs;
import static ua.java.conferences.utils.QueryBuilderUtil.userQueryBuilder;

class ViewUsersActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final UserService userService = mock(UserService.class);
    private final String SPEAKER = "3";
    private final String ZERO = "0";
    private final String FIVE = "5";

    @Test
    void testExecute() throws ServiceException {
        setRequest();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getUserService()).thenReturn(userService);
        when(userService.getSortedUsers(getQueryBuilder().getQuery())).thenReturn(getUserDTOs());
        when(userService.getNumberOfRecords(getQueryBuilder().getRecordQuery())).thenReturn(10);

        assertEquals(VIEW_USERS_PAGE, new ViewUsersAction(appContext).execute(myRequest, response));
        assertEquals(getUserDTOs(), myRequest.getAttribute(USERS));
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(2, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(2, myRequest.getAttribute(END));
    }

    private void setRequest() {
        when(request.getParameter(ROLE)).thenReturn(SPEAKER);
        when(request.getParameter(SORT)).thenReturn(NAME);
        when(request.getParameter(ORDER)).thenReturn(DESCENDING_ORDER);
        when(request.getParameter(OFFSET)).thenReturn(ZERO);
        when(request.getParameter(RECORDS)).thenReturn(FIVE);
    }

    private QueryBuilder getQueryBuilder() {
        return userQueryBuilder()
                .setRoleFilter(SPEAKER)
                .setSortField(NAME)
                .setOrder(DESCENDING_ORDER)
                .setLimits(ZERO, FIVE);
    }
}