package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.ReportService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.SEARCH_EVENT_ACTION;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.ENTER_CORRECT_TOPIC;

class CreateReportActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);

    @Test
    void testExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest();
        when(appContext.getReportService()).thenReturn(reportService);
        doNothing().when(reportService).addReport(getReportDTO());
        String path = new CreateReportAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, ONE), path);
    }

    @Test
    void testBadExecute() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequest();
        when(appContext.getReportService()).thenReturn(reportService);
        doThrow(new IncorrectFormatException(ENTER_CORRECT_TOPIC)).when(reportService).addReport(isA(ReportDTO.class));
        String path = new CreateReportAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(SEARCH_EVENT_ACTION, EVENT_ID, ONE), path);
        assertEquals(ENTER_CORRECT_TOPIC, myRequest.getSession().getAttribute(ERROR));
    }

    private void setRequest() {
        when(request.getParameter(TOPIC)).thenReturn(TOPIC_VALUE);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
    }
}