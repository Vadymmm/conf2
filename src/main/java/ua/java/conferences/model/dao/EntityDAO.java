package ua.java.conferences.model.dao;

import ua.java.conferences.exceptions.DAOException;
import java.util.*;

/**
 * Entity DAO interface.
 * Implement methods in all concrete DAOs
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 * @param <T> – the type of entities
 */
public interface EntityDAO<T> {

    /**
     * Insert record into database
     * @param t - concrete entity in implementations
     * @throws DAOException is wrapper for SQLException
     */
    void add(T t) throws DAOException;

    /**
     * Obtains instance of entity from database
     * @param id - value of id field in database
     * @return Optional.ofNullable - entity is null if there is no entity
     * @throws DAOException is wrapper for SQLException
     */
    Optional<T> getById(long id) throws DAOException;

    /**
     * Obtains list of all entities from database
     * @return entities list
     * @throws DAOException is wrapper for SQLException
     */
    List<T> getAll() throws DAOException;

    /**
     * Updates entity
     * @param t should contain all necessary fields
     * @throws DAOException is wrapper for SQLException
     */
    void update(T t) throws DAOException;

    /**
     * Deletes record in database table
     * @param id - value of id field in database
     * @throws DAOException is wrapper for SQLException
     */
    void delete(long id) throws DAOException;
}