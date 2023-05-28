package ua.java.conferences.exceptions;

/**
 * Use different messages for incorrect email, password, name, surname, topic, title, date, location, description
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class IncorrectFormatException extends ServiceException {
    public IncorrectFormatException(String message) {
        super(message);
    }
}