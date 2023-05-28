package ua.java.conferences.controller.actions.constants;

import lombok.*;

/**
 * This is ParameterValues class. It contains all constant parameters and attributes values
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterValues {

    /** Some common messages for user */
    public static final String CHECK_EMAIL = "check.email";
    public static final String SUCCEED_REGISTER = "succeed.registered";
    public static final String SUCCEED_ADD = "succeed.add";
    public static final String SUCCEED_UPDATE = "succeed.update";
    public static final String SUCCEED_DELETE = "succeed.delete";
    public static final String FAIL_SET_SPEAKER = "fail.set.speaker";

    /** Some common errors for user */
    public static final String ACCESS_DENIED = "access.denied";
    public static final String ERROR_EVENT_EDIT = "error.event.edit";
    public static final String OFFER_FORBIDDEN = "offer.forbidden";

    /** Available parameters and attributes values for date */
    public static final String PASSED = "passed";
    public static final String UPCOMING = "upcoming";

    /** Available parameters and attributes values for order */
    public static final String ASCENDING_ORDER = "ASC";
    public static final String DESCENDING_ORDER = "DESC";

    /** Available parameters and attributes values for registration or canceling registration for conference */
    public static final String REGISTER = "register";
    public static final String CANCEL = "cancel";

    /** Available parameters and attributes values for setting or removing speaker for report */
    public static final String SET = "set";
    public static final String REMOVE = "remove";
}