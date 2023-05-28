package ua.java.conferences.controller.actions.implementation.moderator;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.actions.util.MyResponse;
import ua.java.conferences.controller.context.AppContext;
import ua.java.conferences.exceptions.ServiceException;
import ua.java.conferences.model.services.EventService;
import ua.java.conferences.utils.PdfUtil;
import ua.java.conferences.utils.query.QueryBuilder;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionUtil.getActionToRedirect;
import static ua.java.conferences.controller.actions.constants.ActionNames.VIEW_EVENTS_ACTION;
import static ua.java.conferences.controller.actions.constants.ParameterValues.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.controller.actions.util.Util.*;
import static ua.java.conferences.utils.QueryBuilderUtil.eventQueryBuilder;

class EventsToPdfActionTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AppContext appContext = mock(AppContext.class);
    private final EventService eventService = mock(EventService.class);
    private final ServletContext servletContext = mock(ServletContext.class);

    @Test
    void testExecute() throws ServiceException, MalformedURLException {
        setRequest();
        MyResponse myResponse = new MyResponse(response);
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOCALE, EN);
        when(appContext.getEventService()).thenReturn(eventService);
        when(servletContext.getResource(FONT)).thenThrow(MalformedURLException.class);
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        when(appContext.getPdfUtil()).thenReturn(pdfUtil);
        when(eventService.getSorted(getQueryBuilder().getQuery())).thenReturn(getEventDTOs());

        String path = new EventsToPdfAction(appContext).execute(myRequest, myResponse);
        assertEquals(getActionToRedirect(VIEW_EVENTS_ACTION), path);

        String expected = pdfUtil.createEventsPdf(getEventDTOs(), EN).toString();
        String actual = myResponse.getOutputStream().toString();
        assertEquals(clearString(expected), clearString(actual));
    }

    @Test
    void testExecuteIOException() throws ServiceException, IOException {
        setRequest();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(LOCALE, EN);
        when(appContext.getEventService()).thenReturn(eventService);
        when(servletContext.getResource(FONT)).thenThrow(MalformedURLException.class);
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        when(appContext.getPdfUtil()).thenReturn(pdfUtil);
        when(eventService.getSorted(getQueryBuilder().getQuery())).thenReturn(getEventDTOs());
        when(response.getOutputStream()).thenThrow(IOException.class);

        String path = new EventsToPdfAction(appContext).execute(myRequest, response);
        assertEquals(getActionToRedirect(VIEW_EVENTS_ACTION), path);
    }

    private void setRequest() {
        when(request.getParameter(DATE)).thenReturn(PASSED);
        when(request.getParameter(SORT)).thenReturn(TITLE);
        when(request.getParameter(ORDER)).thenReturn(DESCENDING_ORDER);
    }


    private QueryBuilder getQueryBuilder() {
        String zero = "0";
        String max = String.valueOf(Integer.MAX_VALUE);
        return eventQueryBuilder()
                .setDateFilter(PASSED)
                .setSortField(TITLE)
                .setOrder(DESCENDING_ORDER)
                .setLimits(zero, max);
    }
}