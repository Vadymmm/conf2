package ua.java.conferences.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.MyRequest;
import ua.java.conferences.controller.actions.util.MyResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.java.conferences.controller.actions.constants.Parameters.LOCALE;

class LocaleFilterTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final FilterConfig config = mock(FilterConfig.class);
    private static final String EN = "en";
    private static final String UA = "uk_UA";

    @BeforeEach
    void setDefault() {
        when(config.getInitParameter("defaultLocale")).thenReturn(EN);
    }

    @Test
    void testDoFilterNoLocaleNullCookies() throws ServletException, IOException {
        LocaleFilter filter = new LocaleFilter();
        filter.init(config);
        MyRequest myRequest = new MyRequest(request);
        filter.doFilter(myRequest, response, chain);
        assertEquals(EN, myRequest.getSession().getAttribute(LOCALE));
    }

    @Test
    void testDoFilterNoLocaleNoRequiredCookies() throws ServletException, IOException {
        LocaleFilter filter = new LocaleFilter();
        filter.init(config);
        MyRequest myRequest = new MyRequest(request);
        myRequest.addCookie("wrong", "cookie");
        filter.doFilter(myRequest, response, chain);
        assertEquals(EN, myRequest.getSession().getAttribute(LOCALE));
    }

    @Test
    void testDoFilterNoLocaleCorrectCookie() throws ServletException, IOException {
        LocaleFilter filter = new LocaleFilter();
        filter.init(config);
        MyRequest myRequest = new MyRequest(request);
        myRequest.addCookie(LOCALE, UA);
        filter.doFilter(myRequest, response, chain);
        assertEquals(UA, myRequest.getSession().getAttribute(LOCALE));
    }

    @Test
    void testDoFilterParameterLocale() throws ServletException, IOException {
        LocaleFilter filter = new LocaleFilter();
        filter.init(config);
        when(request.getParameter(LOCALE)).thenReturn(UA);
        when(request.getServletPath()).thenReturn("index.jsp");
        MyRequest myRequest = new MyRequest(request);
        MyResponse myResponse = new MyResponse(response);
        filter.doFilter(myRequest, myResponse, chain);
        assertEquals(UA, myRequest.getSession().getAttribute(LOCALE));
        assertEquals(myResponse.getCookies().get(0), new Cookie(LOCALE, UA));
    }
}