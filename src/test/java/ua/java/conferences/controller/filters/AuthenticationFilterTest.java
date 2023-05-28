package ua.java.conferences.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.dto.UserDTO;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Pages.SIGN_IN_PAGE;
import static ua.java.conferences.controller.actions.constants.ParameterValues.ACCESS_DENIED;
import static ua.java.conferences.controller.actions.constants.Parameters.*;

class AuthenticationFilterTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
    private final FilterChain chain = mock(FilterChain.class);

    @ParameterizedTest
    @CsvFileSource(resources = "/unLoggedUserPages.csv")
    void testDoFilterCorrectPage(String page) throws ServletException, IOException {
        AuthenticationFilter filter = new AuthenticationFilter();
        MyRequest myRequest = new MyRequest(request);
        when(request.getServletPath()).thenReturn(page);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/unLoggedUserActions.csv")
    void testDoFilterCorrectAction(String action) throws ServletException, IOException {
        AuthenticationFilter filter = new AuthenticationFilter();
        MyRequest myRequest = new MyRequest(request);
        when(request.getParameter(ACTION)).thenReturn(action);
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/loggedUserPages.csv", "/moderatorPages.csv", "/adminPages.csv"})
    void testDoFilterAccessDeniedPage(String page) throws ServletException, IOException {
        AuthenticationFilter filter = new AuthenticationFilter();
        MyRequest myRequest = new MyRequest(request);
        when(request.getServletPath()).thenReturn(page);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/loggedUserActions.csv", "/visitorActions.csv", "/speakerActions.csv",
            "/moderatorActions.csv", "/adminActions.csv"})
    void testDoFilterAccessDeniedAction(String action) throws ServletException, IOException {
        AuthenticationFilter filter = new AuthenticationFilter();
        MyRequest myRequest = new MyRequest(request);
        when(request.getParameter(ACTION)).thenReturn(action);
        when(request.getRequestDispatcher(SIGN_IN_PAGE)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertEquals(ACCESS_DENIED, myRequest.getAttribute(MESSAGE));
    }


    @Test
    void testDoFilterLoggedUser() throws ServletException, IOException {
        AuthenticationFilter filter = new AuthenticationFilter();
        MyRequest myRequest = new MyRequest(request);
        myRequest.getSession().setAttribute(USER, UserDTO.builder().build());
        doNothing().when(chain).doFilter(myRequest, response);
        filter.doFilter(myRequest, response, chain);
        assertNull(myRequest.getAttribute(MESSAGE));
    }
}