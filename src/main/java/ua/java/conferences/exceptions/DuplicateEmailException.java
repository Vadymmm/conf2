package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.DUPLICATE_EMAIL;

/**
 * Uses to change SQLException to ServiceException
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class DuplicateEmailException extends ServiceException {
    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL);
    }
}