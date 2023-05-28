package ua.java.conferences.model.dao.mysql.constants;

import lombok.*;

/**
 * Class that contains all My SQL queries for UserDAO
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSQLQueries {
    public static final String ADD_USER = "INSERT INTO user (email, password, name, surname) VALUES (?,?,?,?)";
    public static final String GET_USER_BY_ID = "SELECT * FROM user WHERE id=?";
    public static final String GET_USER_BY_EMAIL = "SELECT * FROM user WHERE email=?";
    public static final String GET_USERS = "SELECT * FROM user";
    public static final String GET_PARTICIPANTS = "SELECT * FROM user JOIN user_has_event " +
            "ON user.id=user_has_event.user_id WHERE event_id=?";
    public static final String GET_SPEAKERS = "SELECT * FROM user JOIN report ON user.id=report.user_id " +
            "WHERE event_id=? GROUP BY user.id";
    public static final String GET_SORTED = "SELECT * FROM user %s";
    public static final String GET_NUMBER_OF_RECORDS = "SELECT COUNT(id) AS numberOfRecords FROM user %s";
    public static final String UPDATE_USER = "UPDATE user SET email=?, name=?, surname=? WHERE id=?";
    public static final String UPDATE_PASSWORD = "UPDATE user SET password=? WHERE id=?";
    public static final String SET_ROLE = "UPDATE user SET role_id=? WHERE email=?";
    public static final String DELETE_USER = "DELETE FROM user WHERE id=?";
    public static final String REGISTER_FOR_EVENT = "INSERT INTO user_has_event VALUES (?,?)";
    public static final String CANCEL_REGISTRATION = "DELETE FROM user_has_event WHERE user_id=? AND event_id=?";
    public static final String IS_REGISTERED = "SELECT * from user_has_event event where user_id=? AND event_id=?";
}