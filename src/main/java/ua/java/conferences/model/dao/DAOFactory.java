package ua.java.conferences.model.dao;

import javax.sql.DataSource;
import ua.java.conferences.model.dao.mysql.MysqlDAOFactory;
import ua.java.conferences.model.dao.constants.DbImplementations;

/**
 * Abstract factory that provides concrete factories to obtain DAOs
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public abstract class DAOFactory {
    /** A single instance of the factory (Singleton pattern) */
    private static DAOFactory instance;

    /** Constructor should be used only in subclasses */
    protected DAOFactory() {}

    /**
     * Obtains single instance of the class. Synchronized to avoid multithreading collisions
     * @param dbImplementation - name of concrete database type (as an example 'MySql')
     * @param dataSource - datasource to connect to database
     * @return concrete DAO factory
     */
    public static synchronized DAOFactory getInstance(String dbImplementation, DataSource dataSource) {
        if (instance == null && DbImplementations.MYSQL.equalsIgnoreCase(dbImplementation)) {
            instance = new MysqlDAOFactory(dataSource);
        }
        return instance;
    }

    /**
     * Obtains concrete instance of DAO class
     * @return EventDAO for required database type
     */
    public abstract EventDAO getEventDAO();

    /**
     * Obtains concrete instance of DAO class
     * @return ReportDAO for required database type
     */
    public abstract ReportDAO getReportDAO();

    /**
     * Obtains concrete instance of DAO class
     * @return UserDAO for required database type
     */
    public abstract UserDAO getUserDAO();
}