package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.WRONG_PASSWORD;

/**
 * If password does not match database password
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class IncorrectPasswordException extends ServiceException{
    public IncorrectPasswordException() {
        super(WRONG_PASSWORD);
    }
}