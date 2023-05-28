package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.NO_EVENT;

/**
 * In case of no such event in database or access denied for the user
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class NoSuchEventException extends ServiceException {
    public NoSuchEventException() {
        super(NO_EVENT);
    }
    public NoSuchEventException(String message) {
        super(message);
    }
}