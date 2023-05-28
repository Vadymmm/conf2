package ua.java.conferences.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.java.conferences.controller.actions.util.MyRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.SIGN_IN_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ACCESS_DENIED;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

class AuthorizationFilterTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
    private final FilterChain chain = mock(FilterChain.class);

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserPages.csv", "/loggedUserPages.csv"})
    void testVisitorDoFilterCorrectPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "VISITOR");
        when(request.getServletPath()).thenReturn(page);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserPages.csv", "/loggedUserPages.csv"})
    void testSpeakerDoFilterCorrectPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "SPEAKER");
        when(request.getServletPath()).thenReturn(page);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserPages.csv", "/loggedUserPages.csv", "/moderatorPages.csv"})
    void testModeratorDoFilterCorrectPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "MODERATOR");
        when(request.getServletPath()).thenReturn(page);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserPages.csv", "/loggedUserPages.csv", "/adminPages.csv"})
    void testAdminDoFilterCorrectPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "ADMIN");
        when(request.getServletPath()).thenReturn(page);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserActions.csv", "/loggedUserActions.csv", "/visitorActions.csv"})
    void testVisitorDoFilterCorrectAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "VISITOR");
        when(request.getParameter(ACTION)).thenReturn(action);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserActions.csv", "/loggedUserActions.csv", "/speakerActions.csv"})
    void testSpeakerDoFilterCorrectAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "SPEAKER");
        when(request.getParameter(ACTION)).thenReturn(action);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserActions.csv", "/loggedUserActions.csv", "/moderatorActions.csv"})
    void testModeratorDoFilterCorrectAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "MODERATOR");
        when(request.getParameter(ACTION)).thenReturn(action);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/unLoggedUserActions.csv", "/loggedUserActions.csv", "/adminActions.csv"})
    void testAdminDoFilterCorrectAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "ADMIN");
        when(request.getParameter(ACTION)).thenReturn(action);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/moderatorPages.csv", "/adminPages.csv"})
    void testVisitorDoFilterAccessDeniedPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "VISITOR");
        when(request.getServletPath()).thenReturn(page);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/moderatorPages.csv", "/adminPages.csv"})
    void testSpeakerDoFilterAccessDeniedPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "SPEAKER");
        when(request.getServletPath()).thenReturn(page);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/adminPages.csv")
    void testModeratorDoFilterAccessDeniedPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "MODERATOR");
        when(request.getServletPath()).thenReturn(page);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/moderatorPages.csv")
    void testAdminDoFilterAccessDeniedPage(String page) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "ADMIN");
        when(request.getServletPath()).thenReturn(page);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/speakerActions.csv", "/moderatorActions.csv", "/adminActions.csv"})
    void testVisitorDoFilterAccessDeniedAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "VISITOR");
        when(request.getParameter(ACTION)).thenReturn(action);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/visitorActions.csv", "/moderatorActions.csv", "/adminActions.csv"})
    void testSpeakerDoFilterAccessDeniedAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "SPEAKER");
        when(request.getParameter(ACTION)).thenReturn(action);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }


    @ParameterizedTest
    @CsvFileSource(resources = {"/visitorActions.csv", "/speakerActions.csv", "/adminActions.csv"})
    void testModeratorDoFilterAccessDeniedAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "MODERATOR");
        when(request.getParameter(ACTION)).thenReturn(action);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/visitorActions.csv", "/speakerActions.csv", "/moderatorActions.csv"})
    void testAdminDoFilterAccessDeniedAction(String action) throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(ROLE, "ADMIN");
        when(request.getParameter(ACTION)).thenReturn(action);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @Test
    void testDoFilterNoLoggedUser() throws ServletException, IOException {
        AuthorizationFilter filter = new AuthorizationFilter();
        MyRequest myRequest = new MyRequest(request);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }
}