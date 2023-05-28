package ua.java.conferences.exceptions.constants;

import lombok.*;

/**
 * Contains messages for all user-defined exceptions
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {
    public static final String ENTER_CORRECT_EMAIL = "error.email.format";
    public static final String ENTER_CORRECT_PASSWORD = "error.pass.format";
    public static final String ENTER_CORRECT_NAME = "error.name.format";
    public static final String ENTER_CORRECT_SURNAME = "error.surname.format";
    public static final String ENTER_CORRECT_TOPIC = "error.topic.format";
    public static final String ENTER_CORRECT_TITLE = "error.title.format";
    public static final String ENTER_VALID_DATE = "error.date.format";
    public static final String ENTER_CORRECT_LOCATION = "error.location.format";
    public static final String ENTER_CORRECT_DESCRIPTION = "error.description.format";
    public static final String DUPLICATE_EMAIL = "error.email.duplicate";
    public static final String DUPLICATE_TITLE = "error.title.duplicate";
    public static final String WRONG_PASSWORD = "error.pass.wrong";
    public static final String PASSWORD_MATCHING = "error.pass.match";
    public static final String NO_EVENT = "error.event.absent";
    public static final String NO_REPORT = "error.report.absent";
    public static final String NO_USER = "error.email.absent";
    public static final String CAPTCHA_INVALID = "error.captcha.invalid";
}