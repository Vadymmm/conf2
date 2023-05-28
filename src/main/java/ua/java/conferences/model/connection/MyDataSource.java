package ua.java.conferences.model.connection;

import static ua.java.conferences.model.connection.ConnectionConstants.*;

import com.zaxxer.hikari.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Class to configure and obtain HikariDataSource. Use it to connect to database
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MyDataSource {
    private static DataSource dataSource;

    /**
     * Configures and obtains HikariDataSource.
     * @param properties - all required info to configure datasource
     * @return singleton instance of HikariDataSource
     */
    public static synchronized DataSource getDataSource(Properties properties) {
        if (dataSource == null) {
            HikariConfig config = getHikariConfig(properties);
            dataSource = new HikariDataSource(config);
            log.info("Hikari pool instance was created");
        }
        return dataSource;
    }

    private static HikariConfig getHikariConfig(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty(URL_PROPERTY));
        config.setUsername(properties.getProperty(DB_USER));
        config.setPassword(properties.getProperty(DB_PASSWORD));
        config.setDriverClassName(properties.getProperty(DRIVER));
        config.setDataSourceProperties(properties);
        log.info("Hikari pool was configured");
        return config;
    }
}