package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.NO_REPORT;

/**
 * In case of no such report
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class NoSuchReportException extends ServiceException {
    public NoSuchReportException() {
        super(NO_REPORT);
    }
}