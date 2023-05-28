package ua.java.conferences.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * EncodingFilter  class. Sets encoding for any values from view
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class EncodingFilter extends HttpFilter {
    private String encoding;

    /**
     * Sets default encoding
     * @param config passed by application
     */
    @Override
    public void init(FilterConfig config) {
        log.info("Default encoding was set");
        encoding = config.getInitParameter("encoding");
    }

    /**
     * Sets default encoding for any values from user.
     * @param request passed by application
     * @param response passed by application
     * @param chain passed by application
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
}