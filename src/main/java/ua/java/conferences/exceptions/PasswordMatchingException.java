package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.PASSWORD_MATCHING;

/**
 * If password doesn't match confirmation password.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class PasswordMatchingException extends ServiceException{
    public PasswordMatchingException() {
        super(PASSWORD_MATCHING);
    }
}