package ua.java.conferences.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.java.conferences.controller.context.AppContext;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private static final String PROPERTIES_FILE = "context.properties";
    private final ServletContext servletContext = mock(ServletContext.class);

    @Test
    void testDoGet() throws IOException, ServletException {
        AppContext.createAppContext(servletContext, PROPERTIES_FILE);
        when(request.getRequestDispatcher(isA(String.class))).thenReturn(requestDispatcher);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        new Controller().doGet(request, response);
        verify(request).getRequestDispatcher(captor.capture());
        assertEquals("index.jsp", captor.getValue());
    }

    @Test
    void testDoPost() throws IOException {
        AppContext.createAppContext(servletContext, PROPERTIES_FILE);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        new Controller().doPost(request, response);
        verify(response).sendRedirect(captor.capture());
        assertEquals("index.jsp", captor.getValue());
    }

    @Test
    void testDoPostException() throws IOException {
        AppContext.createAppContext(servletContext, PROPERTIES_FILE);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(request.getParameter("action")).thenReturn("sign-in");
        new Controller().doPost(request, response);
        verify(response).sendRedirect(captor.capture());
        assertEquals("error.jsp", captor.getValue());
    }
}