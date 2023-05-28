package ua.java.conferences.model.dao;

import ua.java.conferences.model.entities.*;

import static ua.java.conferences.Constants.*;

public final class DAOTestUtils {

    public static User getTestUser() {
        return User.builder()
                .id(ID_VALUE)
                .email(EMAIL_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .password(PASSWORD_VALUE)
                .roleId(ROLE_ID_VALUE)
                .build();
    }

    public static Event getTestEvent() {
        return Event.builder()
                .id(ID_VALUE)
                .title(TITLE_VALUE)
                .date(DATE_VALUE)
                .location(LOCATION_VALUE)
                .description(DESCRIPTION_VALUE)
                .reports(20)
                .visitors(40)
                .registrations(50)
                .build();
    }

    public static Report getTestReport() {
        return Report.builder()
                .id(ID_VALUE)
                .topic(TOPIC_VALUE)
                .event(getTestEvent())
                .speaker(getTestUser())
                .build();
    }
}