package ua.java.conferences.controller.actions.implementation.speaker;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.*;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class SetOrRemoveSpeakerBySpeakerActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);
    private final UserService userService = mock(UserService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecuteSetSpeaker() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequestSetSpeaker(myRequest);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        when(reportService.setSpeaker(isA(long.class), isA(long.class))).thenReturn(true);
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        when(userService.getModerators()).thenReturn(getUserDTOs());
        String path = new SetOrRemoveSpeakerBySpeakerAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_EVENT_BY_SPEAKER_ACTION, EVENT_ID, ONE), path);
    }

    @Test
    void testExecuteRemoveSpeaker() throws ServiceException {
        MyRequest myRequest = new MyRequest(request);
        setRequestRemoveSpeaker(myRequest);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getUserService()).thenReturn(userService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(reportService).deleteSpeaker(isA(long.class));
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        when(userService.getModerators()).thenReturn(getUserDTOs());
        String path = new SetOrRemoveSpeakerBySpeakerAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_SPEAKERS_REPORTS_ACTION), path);
    }

    private void setRequestSetSpeaker(MyRequest myRequest){
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
        when(request.getParameter(EVENT_ID)).thenReturn(ONE);
        when(request.getParameter(TODO)).thenReturn(SET);
        when(request.getParameter(TOPIC)).thenReturn(TOPIC_VALUE);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
    }

    private void setRequestRemoveSpeaker(MyRequest myRequest){
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
        when(request.getParameter(EVENT_ID)).thenReturn(null);
        when(request.getParameter(TODO)).thenReturn(REMOVE);
        when(request.getParameter(TOPIC)).thenReturn(TOPIC_VALUE);
        when(request.getParameter(TITLE)).thenReturn(TITLE_VALUE);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        when(request.getRequestURL()).thenReturn(REQUEST_URL);
        myRequest.getSession().setAttribute(LOGGED_USER, getUserDTO());
    }
}