package ua.java.conferences.model.connection;

import lombok.*;

/**
 * Contains keys for properties to configure database connection
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionConstants {
    public static final String URL_PROPERTY = "connection.url";
    public static final String DB_USER = "db.user";
    public static final String DB_PASSWORD = "db.password";
    public static final String DRIVER = "driver";
}