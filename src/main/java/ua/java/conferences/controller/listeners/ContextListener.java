package ua.java.conferences.controller.listeners;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.context.AppContext;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ContextListener  class.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class ContextListener implements ServletContextListener {
    /** Name of properties file to configure DataSource, EmailSender and Captcha */
    private static final String PROPERTIES_FILE = "context.properties";

    /**
     * creates AppContext and passes ServletContext and properties to initialize all required classes
     * @param sce passed by application
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("The application has started");
        try {
            AppContext.createAppContext(sce.getServletContext(), PROPERTIES_FILE);
            log.info("AppContext was set");
        } catch (Exception e) {
            log.error("AppContext wasn't set");
        }
    }

    /**
     * closes mysql thread and deregister all drivers
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        AbandonedConnectionCleanupThread.checkedShutdown();
        deregisterDrivers();
        log.info("The application has stopped working");
    }

    private static void deregisterDrivers() {
        DriverManager.drivers().forEach(driver -> {try {
            DriverManager.deregisterDriver(driver);
        } catch (SQLException e) {
            log.warn(String.format("Couldn't deregister %s", driver), e);
        }
        });
    }
}
