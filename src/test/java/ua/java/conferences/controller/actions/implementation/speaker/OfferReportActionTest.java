package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.dto.ReportDTO;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.OFFER_REPORT_ACTION;
import static ua.java.conferences.controller.actions.constants.Pages.OFFER_REPORT_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.exceptions.constants.Message.ENTER_CORRECT_TOPIC;

class OfferReportActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);
    private final ReportService reportService = mock(ReportService.class);
    private final UserService userService = mock(UserService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest(myRequest);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(reportService).addReport(isA(ReportDTO.class));
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        when(userService.getModerators()).thenReturn(getUserDTOs());
        String path = new OfferReportAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(OFFER_REPORT_ACTION, EVENT_ID, ONE), path);
        assertEquals(SUCCEED_ADD, myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testBadExecutePost() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setPostRequest(myRequest);
        when(appContext.getReportService()).thenReturn(reportService);
        doThrow(new IncorrectFormatException(ENTER_CORRECT_TOPIC)).when(reportService).addReport(isA(ReportDTO.class));
        String path = new OfferReportAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(OFFER_REPORT_ACTION, EVENT_ID, ONE), path);
        assertEquals(ENTER_CORRECT_TOPIC, myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteGet() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        when(appContext.getEventService()).thenReturn(eventService);
        when(eventService.getSortedByUser(isA(String.class),isA(Role.class))).thenReturn(getEventDTOs());
        String path = new OfferReportAction(appContext).execute(myRequest, response);

        assertEquals(OFFER_REPORT_PAGE, path);
        assertEquals(getEventDTO(), myRequest.getAttribute(EVENT));
        assertEquals(SUCCEED_ADD, myRequest.getAttribute(MESSAGE));
        assertEquals(ENTER_CORRECT_TOPIC, myRequest.getAttribute(ERROR));
        assertNull(myRequest.getSession().getAttribute(MESSAGE));
        assertNull(myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteGetNoEvent() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setGetRequest(myRequest);
        when(appContext.getEventService()).thenReturn(eventService);
        when(eventService.getSortedByUser(isA(String.class),isA(Role.class))).thenReturn(List.of(getPassedEventDTO()));
        String path = new OfferReportAction(appContext).execute(myRequest, response);

        assertEquals(OFFER_REPORT_PAGE, path);
        assertEquals(OFFER_FORBIDDEN, myRequest.getAttribute(ERROR));
    }

    private void setPostRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(POST);
        when(request.getParameter(TOPIC)).thenReturn(TOPIC_VALUE);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
    }

    private void setGetRequest(MyRequest myRequest) {
        when(request.getMethod()).thenReturn(GET);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        myRequest.getSession().setAttribute(MESSAGE, SUCCEED_ADD);
        myRequest.getSession().setAttribute(ERROR, ENTER_CORRECT_TOPIC);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
    }
}