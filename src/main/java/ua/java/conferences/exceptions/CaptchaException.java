package ua.java.conferences.exceptions;

import static ua.java.conferences.exceptions.constants.Message.*;

/**
 * For not passing captcha exception
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class CaptchaException extends ServiceException {
    public CaptchaException() {
        super(CAPTCHA_INVALID);
    }
}