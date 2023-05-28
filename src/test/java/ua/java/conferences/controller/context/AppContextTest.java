package ua.java.conferences.controller.context;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AppContextTest {
    private static final String PROPERTIES_FILE = "context.properties";
    private final ServletContext SERVLET_CONTEXT = mock(ServletContext.class);

    @Test
    void testRightFile() {
        assertDoesNotThrow(() -> AppContext.createAppContext(SERVLET_CONTEXT, PROPERTIES_FILE));
        AppContext appContext = AppContext.getAppContext();
        assertNotNull(appContext.getUserService());
        assertNotNull(appContext.getEventService());
        assertNotNull(appContext.getReportService());
        assertNotNull(appContext.getCaptcha());
        assertNotNull(appContext.getEmailSender());
        assertNotNull(appContext.getPdfUtil());
    }

    @Test
    void testWrongFile() {
        assertDoesNotThrow(() -> AppContext.createAppContext(SERVLET_CONTEXT, "wrong"));
        AppContext appContext = AppContext.getAppContext();
        assertNotNull(appContext.getUserService());
        assertNotNull(appContext.getEventService());
        assertNotNull(appContext.getReportService());
        assertNotNull(appContext.getCaptcha());
        assertNotNull(appContext.getEmailSender());
        assertNotNull(appContext.getPdfUtil());
    }
}