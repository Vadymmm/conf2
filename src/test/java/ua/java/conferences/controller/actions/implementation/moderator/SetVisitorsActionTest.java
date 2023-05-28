package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.EventService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

class SetVisitorsActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);

    @Test
    void testExecute() throws ServiceException {
        setRequest();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getEventService()).thenReturn(eventService);
        doNothing().when(eventService).setVisitorsCount(isA(String.class), isA(String.class));
        String path = new SetVisitorsAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, "3"), path);
    }

    private void setRequest() {
        when(request.getParameter(EVENT_ID)).thenReturn("3");
        when(request.getParameter(VISITORS)).thenReturn("100");
    }
}