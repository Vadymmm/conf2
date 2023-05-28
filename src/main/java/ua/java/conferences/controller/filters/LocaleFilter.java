package ua.java.conferences.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static ua.java.conferences.controller.actions.constants.Parameters.LOCALE;

/**
 * LocaleFilter  class. Sets and changes locale
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class LocaleFilter  extends HttpFilter {
    private static final String REFERER = "referer";
    private String defaultLocale;

    /**
     * Sets default locale
     * @param config passed by application
     */
    @Override
    public void init(FilterConfig config) {
        log.info("Default locale was set");
        defaultLocale = config.getInitParameter("defaultLocale");
    }

    /**
     * Checks if request contains locale parameter and sets locale to session as attribute if present.
     * Returns previous page in this case.
     * In other case checks if locale presents in session. If not check cookies for last locale and sets either locale
     * from cookies or default locale. doFilter after that.
     * @param request passed by application
     * @param response passed by application
     * @param chain passed by application
     */
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String locale = request.getParameter(LOCALE);
        if (isNotBlank(locale)) {
            request.getSession().setAttribute(LOCALE, locale);
            response.addCookie(new Cookie(LOCALE, locale));
            response.sendRedirect(request.getHeader(REFERER));
        } else {
            String sessionLocale = (String) request.getSession().getAttribute(LOCALE);
            if (isBlank(sessionLocale)) {
                request.getSession().setAttribute(LOCALE, getCookiesLocale(request));
            }
            chain.doFilter(request, response);
        }
    }

    /**
     * Obtains locale value. Checks if Cookies are present, if so then checks if Cookie locale is present.
     * @param request to get Cookies
     * @return either cookie locale or default locale
     */
    private String getCookiesLocale(HttpServletRequest request) {
        return Stream.ofNullable(request.getCookies())
                .flatMap(Arrays::stream)
                .filter(cookie -> cookie.getName().equals(LOCALE))
                .map(Cookie::getValue)
                .findAny().orElse(defaultLocale);
    }

    private boolean isBlank(String locale) {
        return locale == null || locale.isEmpty();
    }

    private boolean isNotBlank(String locale) {
        return !isBlank(locale);
    }
}