package ua.java.conferences;

import java.time.LocalDate;

public final class Constants {
    public static final long ID_VALUE = 1L;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int THREE = 3;
    public static final int TEN = 10;
    public static final String EMAIL_VALUE = "em@em.com";
    public static final String INCORRECT_EMAIL = "em.com";
    public static final String PASSWORD_VALUE = "Password1";
    public static final String INCORRECT_PASSWORD = "Pass1";
    public static final String NAME_VALUE = "Joe";
    public static final String SURNAME_VALUE = "Yi";
    public static final String SPEAKER_NAME = "Joe Yi";
    public static final String ROLE_VISITOR = "VISITOR";
    public static final int ROLE_ID_VALUE = 4;
    public static final String TOPIC_VALUE = "Report topic";
    public static final String NAME_FIELD = "name";
    public static final String EMAIL_FIELD = "email";
    public static final String UPCOMING = "upcoming";
    public static final String PASSED = "passed";
    public static final String DESC = "DESC";
    public static final String TITLE_VALUE = "Event title";
    public static final String TITLE_FIELD = "title";
    public static final LocalDate DATE_VALUE = LocalDate.now().plusDays(ONE);
    public static final String DATE_NAME = DATE_VALUE.toString();
    public static final String INCORRECT_DATE_NAME = LocalDate.now().minusDays(ONE).toString();
    public static final String LOCATION_VALUE = "Kyiv";
    public static final String DESCRIPTION_VALUE = "What an awesome event!";
    public static final int REGISTRATIONS_VALUE = 111;
    public static final int VISITORS_VALUE = 99;
    public static final int REPORTS_VALUE = 13;
    private Constants() {}
}