package ua.java.conferences.exceptions;

/**
 * Main exception for all types of Web App mistakes.
 * Direct subclasses: CaptchaException, DuplicateEmailException, DuplicateTitleException, IncorrectFormatException,
 * IncorrectPasswordException, NoSuchEventException, NoSuchReportException, NoSuchUserException,
 * PasswordMatchingException
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class ServiceException extends Exception{
    public ServiceException() {}
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}