package ua.java.conferences.model.dao.mysql;

import javax.sql.DataSource;
import ua.java.conferences.model.dao.*;

/**
 * My SQL factory that provides My SQL DAOs
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class MysqlDAOFactory extends DAOFactory {
    /** A single instance of the eventDAO (Singleton pattern) */
    private EventDAO eventDAO;

    /** A single instance of the reportDAO (Singleton pattern) */
    private ReportDAO reportDAO;

    /** A single instance of the userDAO (Singleton pattern) */
    private UserDAO userDAO;

    /** An instance of datasource to provide connection to database */
    private final DataSource dataSource;

    public MysqlDAOFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Obtains single instance of the EventDAO.
     * @return MysqlEventDAO
     */
    public EventDAO getEventDAO() {
        if (eventDAO == null) {
            eventDAO = new MysqlEventDAO(dataSource);
        }
        return eventDAO;
    }

    /**
     * Obtains single instance of the ReportDAO.
     * @return MysqlReportDAO
     */
    public ReportDAO getReportDAO() {
        if (reportDAO == null) {
            reportDAO = new MysqlReportDAO(dataSource);
        }
        return reportDAO;
    }

    /**
     * Obtains single instance of the UserDAO.
     * @return MysqlUserDAO
     */
    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new MysqlUserDAO(dataSource);
        }
        return userDAO;
    }
}