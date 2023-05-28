package ua.java.conferences.utils;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import ua.java.conferences.controller.actions.util.Util;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.util.Util.*;

class PdfUtilTest {
    private final ServletContext servletContext = mock(ServletContext.class);
    private final String FONT = "file:./../fonts/arial.ttf";
    private final String EN = "en";
    private final String UA = "uk_Ua";

    @Test
    void testUserPdfEn() throws MalformedURLException {
        when(servletContext.getResource(Util.FONT)).thenReturn(new URL(FONT));
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        assertDoesNotThrow(() -> pdfUtil.createUsersPdf(getUserDTOs(), EN));
    }

    @Test
    void testUserPdfUa() throws MalformedURLException {
        when(servletContext.getResource(Util.FONT)).thenReturn(new URL(FONT));
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        assertDoesNotThrow(() -> pdfUtil.createUsersPdf(getUserDTOs(), UA));
    }

    @Test
    void testEventPdfEn() throws MalformedURLException {
        when(servletContext.getResource(Util.FONT)).thenReturn(new URL(FONT));
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        assertDoesNotThrow(() -> pdfUtil.createEventsPdf(getEventDTOs(), EN));
    }

    @Test
    void testEventPdfUa() throws MalformedURLException {
        when(servletContext.getResource(Util.FONT)).thenReturn(new URL(FONT));
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        assertDoesNotThrow(() -> pdfUtil.createEventsPdf(getEventDTOs(), UA));
    }

    @Test
    void testEventPdfNoFont() throws MalformedURLException {
        when(servletContext.getResource(Util.FONT)).thenReturn(new URL("file:./../fonts/wrong.ttf"));
        PdfUtil pdfUtil = new PdfUtil(servletContext);
        assertDoesNotThrow(() -> pdfUtil.createEventsPdf(getEventDTOs(), UA));
    }
}