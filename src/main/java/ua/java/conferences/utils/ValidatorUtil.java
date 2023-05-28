package ua.java.conferences.utils;

import lombok.*;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.utils.constants.Regex;

import java.time.LocalDate;

import static ua.java.conferences.exceptions.constants.Message.*;
/**
 * Validator to validate emails, names, etc..
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtil {

    public static void validateEmail(String email) throws IncorrectFormatException {
        validateFormat(email, Regex.EMAIL_REGEX, ENTER_CORRECT_EMAIL);
    }

    public static void validatePassword(String password) throws IncorrectFormatException {
        validateFormat(password, Regex.PASSWORD_REGEX, ENTER_CORRECT_PASSWORD);
    }


    public static void validateName(String name, String message) throws IncorrectFormatException {
        validateFormat(name, Regex.NAME_REGEX, message);
    }

    public static void validateComplexName(String name, String message) throws IncorrectFormatException {
        validateFormat(name, Regex.COMPLEX_NAME_REGEX, message);
    }

    public static void validateDescription(String name) throws IncorrectFormatException {
        validateFormat(name, Regex.DESCRIPTION_REGEX, ENTER_CORRECT_DESCRIPTION);
    }

    private static void validateFormat(String name, String regex,String message) throws IncorrectFormatException {
        if (name == null || !name.matches(regex))
            throw new IncorrectFormatException(message);
    }

    public static void validateDate(LocalDate date) throws IncorrectFormatException {
        if (date == null || !date.isAfter(LocalDate.now())) {
            throw new IncorrectFormatException(ENTER_VALID_DATE);
        }
    }

    public static void checkPasswordMatching(String password, String confirmPassword) throws PasswordMatchingException {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMatchingException();
        }
    }

    public static long getEventId(String idString) throws ServiceException {
        return checkId(idString, new NoSuchEventException());
    }


    public static long getReportId(String idString) throws ServiceException {
        return checkId(idString, new NoSuchReportException());

    }

    public static long getUserId(String idString) throws ServiceException {
        return checkId(idString, new NoSuchUserException());
    }

    private static long checkId(String idString, ServiceException exception) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw exception;
        }
        return id;
    }

    public static long getLong(String longString) throws ServiceException {
        long result;
        try {
            result = Long.parseLong(longString);
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    public static int getInt(String intString) throws ServiceException {
        int result;
        try {
            result = Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    public static void checkStrings(String... strings) throws ServiceException {
        for (String string : strings) {
            if (string == null) {
                throw new ServiceException(new NullPointerException("Enter valid values"));
            }
        }
    }
}