package ua.java.conferences.exceptions;

/**
 * Wrapper for SQLException
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class DAOException extends Exception {
    public DAOException(Throwable cause) {
        super(cause);
    }
}