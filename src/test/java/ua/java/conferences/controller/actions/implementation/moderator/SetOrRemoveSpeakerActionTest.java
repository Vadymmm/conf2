package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.ReportService;
import ua.java.conferences.utils.EmailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_REPORT_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class SetOrRemoveSpeakerActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final ReportService reportService = mock(ReportService.class);
    private final EmailSender emailSender = mock(EmailSender.class);

    @Test
    void testExecuteSetSpeaker() throws ServiceException {
        setRequestSetSpeaker();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        when(reportService.setSpeaker(isA(long.class), isA(long.class))).thenReturn(true);
        when(reportService.getById(ONE)).thenReturn(getReportDTO());
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        String path = new SetOrRemoveSpeakerAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, ONE), path);
        assertEquals(SUCCEED_UPDATE, myRequest.getSession().getAttribute(MESSAGE));
    }

    @Test
    void testExecuteSetSpeakerAlreadySet() throws ServiceException {
        setRequestSetSpeaker();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        when(reportService.setSpeaker(isA(long.class), isA(long.class))).thenReturn(false);
        when(reportService.getById(ONE)).thenReturn(getReportDTO());
        String path = new SetOrRemoveSpeakerAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, ONE), path);
        assertEquals(FAIL_SET_SPEAKER, myRequest.getSession().getAttribute(ERROR));
    }

    @Test
    void testExecuteRemoveSpeaker() throws ServiceException {
        setRequestRemoveSpeaker();
        MyRequest myRequest = new MyRequest(request);
        when(appContext.getReportService()).thenReturn(reportService);
        when(appContext.getEmailSender()).thenReturn(emailSender);
        doNothing().when(reportService).deleteSpeaker(isA(long.class));
        when(reportService.getById(ONE)).thenReturn(getReportDTO());
        doNothing().when(emailSender).send(isA(String.class), isA(String.class), isA(String.class));
        String path = new SetOrRemoveSpeakerAction(appContext).execute(myRequest, response);

        assertEquals(getActionToRedirect(VIEW_REPORT_ACTION, REPORT_ID, ONE), path);
        assertEquals(SUCCEED_UPDATE, myRequest.getSession().getAttribute(MESSAGE));
    }

    private void setRequestSetSpeaker(){
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
        when(request.getParameter(USER_ID)).thenReturn(ONE);
        when(request.getParameter(TODO)).thenReturn(SET);
    }

    private void setRequestRemoveSpeaker(){
        when(request.getParameter(REPORT_ID)).thenReturn(ONE);
        when(request.getParameter(TODO)).thenReturn(REMOVE);
        when(request.getParameter(EMAIL)).thenReturn(EMAIL_VALUE);
        when(request.getParameter(NAME)).thenReturn(NAME_VALUE);
    }
}