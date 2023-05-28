package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.NO_USER;

/**
 * In case of no such user
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class NoSuchUserException extends ServiceException {
    public NoSuchUserException() {
        super(NO_USER);
    }
}