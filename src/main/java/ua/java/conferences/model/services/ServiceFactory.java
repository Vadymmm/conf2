package ua.java.conferences.model.services;

import lombok.Getter;
import ua.java.conferences.model.dao.DAOFactory;
import ua.java.conferences.model.services.implementation.*;

/**
 * Service factory that provides concrete implementations of EventService, ReportService and UserService classes
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Getter
public final class ServiceFactory {
    private final UserService userService;
    private final ReportService reportService;
    private final EventService eventService;

    private ServiceFactory(DAOFactory daoFactory) {
        userService = new UserServiceImpl(daoFactory.getUserDAO());
        reportService = new ReportServiceImpl(daoFactory.getReportDAO());
        eventService = new EventServiceImpl(daoFactory.getEventDAO());
    }

    /**
     * Obtains instance of ServiceFactory to get concrete Services
     * @param daoFactory - pass concrete DAOFactory instance to get access to DAO
     * @return ServiceFactory instance
     */
    public static ServiceFactory getInstance(DAOFactory daoFactory) {
        return new  ServiceFactory(daoFactory);
    }
}