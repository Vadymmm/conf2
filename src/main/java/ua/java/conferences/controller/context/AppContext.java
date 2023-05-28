package ua.java.conferences.controller.context;

import jakarta.servlet.ServletContext;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.model.connection.MyDataSource;
import ua.java.conferences.model.dao.DAOFactory;
import ua.java.conferences.model.services.*;
import ua.java.conferences.utils.*;

import javax.sql.DataSource;

import java.io.*;
import java.util.Properties;

import static ua.java.conferences.model.dao.constants.DbImplementations.MYSQL;

/**
 * AppContext  class. Contains all required to correct application work objects
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Getter
@Slf4j
public class AppContext {
    private static AppContext appContext;
    private final EventService eventService;
    private final UserService userService;
    private final ReportService reportService;
    private final EmailSender emailSender;
    private final PdfUtil pdfUtil;
    private final Captcha captcha;

    private AppContext(ServletContext servletContext, String propertiesFile) {
        pdfUtil = new PdfUtil(servletContext);
        Properties properties = getProperties(propertiesFile);
        emailSender = new EmailSender(properties);
        captcha = new Captcha(properties);
        DataSource dataSource = MyDataSource.getDataSource(properties);
        DAOFactory daoFactory = DAOFactory.getInstance(MYSQL, dataSource);
        ServiceFactory serviceFactory = ServiceFactory.getInstance(daoFactory);
        eventService =  serviceFactory.getEventService();
        userService = serviceFactory.getUserService();
        reportService = serviceFactory.getReportService();
    }

    /**
     * @return instance of AppContext
     */
    public static AppContext getAppContext() {
        return appContext;
    }

    /**
     * Creates instance of AppContext to use in Actions. Configure all required classes. Loads properties
     * @param servletContext - to use relative address in classes
     * @param propertiesFile - to configure DataSource, EmailSender and Captcha
     */
    public static void createAppContext(ServletContext servletContext, String propertiesFile) {
        appContext = new AppContext(servletContext, propertiesFile);
    }

    private static Properties getProperties(String propertiesFile) {
        Properties properties = new Properties();
        try (InputStream resource = AppContext.class.getClassLoader().getResourceAsStream(propertiesFile)){
            properties.load(resource);
        } catch (Exception e) {
            log.error(String.format("AppContext couldn't read properties because of %s", e.getMessage()));
        }
        return properties;
    }
}