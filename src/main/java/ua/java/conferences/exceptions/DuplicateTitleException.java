package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.DUPLICATE_TITLE;

/**
 * Uses to change SQLException to ServiceException
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class DuplicateTitleException extends ServiceException {
    public DuplicateTitleException() {
        super(DUPLICATE_TITLE);
    }
}