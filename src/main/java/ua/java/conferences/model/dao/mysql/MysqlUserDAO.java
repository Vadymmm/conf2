package ua.java.conferences.model.dao.mysql;

import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.model.dao.UserDAO;
import ua.java.conferences.model.entities.User;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.DAOException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static ua.java.conferences.model.dao.mysql.constants.UserSQLQueries.*;
import static ua.java.conferences.model.dao.mysql.constants.SQLFields.*;
import static ua.java.conferences.model.entities.role.Role.VISITOR;

/**
 * User DAO class for My SQL database. Match tables 'user' and 'user_has_event' in database.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class MysqlUserDAO implements UserDAO {
    /** An instance of datasource to provide connection to database */
    private final DataSource dataSource;

    public MysqlUserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserts new user to database
     * @param user - id will be generated by database. Email, name and surname should be not null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void add(User user) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER)) {
            setStatementFieldsForAddMethod(user, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(String.format("Couldn't add new user %s because of %s", user.getEmail(), e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Obtains instance of User from database by id
     * @param userId - value of id field in database
     * @return Optional.ofNullable - user is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<User> getById(long userId) throws DAOException {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID)) {
            int k = 0;
            preparedStatement.setLong(++k, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = createUser(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(String.format("Couldn't find user with id=%d because of %s", userId, e.getMessage()));
            throw new DAOException(e);
        }
        return  Optional.ofNullable(user);
    }

    /**
     * Obtains instance of User from database by email
     * @param email - value of email field in database
     * @return Optional.ofNullable - user is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<User> getByEmail(String email) throws DAOException {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            int k = 0;
            preparedStatement.setString(++k, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = createUser(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(String.format("Couldn't find user with email - %s because of %s", email, e.getMessage()));
            throw new DAOException(e);
        }
        return  Optional.ofNullable(user);
    }

    /**
     * Obtains list of all users from database
     * @return users list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getAll() throws DAOException {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUser(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(String.format("Couldn't get list of all users because of %s", e.getMessage()));
            throw new DAOException(e);
        }
        return users;
    }

    /**
     * Obtains sorted and limited list of users from database
     * @param query should contain filters (where), order (order field and type), limits for pagination
     * @return users list that matches demands. Will be empty if there are no users
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getSorted(String query) throws DAOException {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String.format(GET_SORTED, query))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUser(resultSet));
                }
            }
        }catch (SQLException e) {
            log.error(String.format("Couldn't get sorted list of users because of %s", e.getMessage()));
            throw new DAOException(e);
        }
        return users;
    }

    /**
     * Obtains list of users that registered for the event from database
     * @param eventId - value of event_id field in database
     * @param role can be either VISITOR or SPEAKER. It changes query.
     * @return users list that matches demands
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getParticipants(long eventId, Role role) throws DAOException {
        List<User> users = new ArrayList<>();
        String query = role == VISITOR ? GET_PARTICIPANTS : GET_SPEAKERS;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int k = 0;
            preparedStatement.setLong(++k, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUser(resultSet));
                }
            }
        }catch (SQLException e) {
            log.error(String.format("Couldn't get list of participants of event because of %s", e.getMessage()));
            throw new DAOException(e);
        }
        return users;
    }

    /**
     * Obtains number of all records matching filter
     * @param filter should contain 'where' to specify query
     * @return number of records
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public int getNumberOfRecords (String filter) throws DAOException {
        int numberOfRecords = 0;
        String query = String.format(GET_NUMBER_OF_RECORDS, filter);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    numberOfRecords = resultSet.getInt(NUMBER_OF_RECORDS);
                }
            }
        }catch (SQLException e) {
            log.error(String.format("Couldn't get number of users because of %s", e.getMessage()));
            throw new DAOException(e);
        }
        return numberOfRecords;
    }

    /**
     * Updates user
     * @param user should contain id, email, name and surname to be updated
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void update(User user) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            setStatementFieldsForUpdateMethod(user, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(String.format("Couldn't update user %s because of %s", user.getEmail(), e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Updates user's password
     * @param user should contain user id and password
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void updatePassword(User user) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD)) {
            int k = 0;
            preparedStatement.setString(++k, user.getPassword());
            preparedStatement.setLong(++k, user.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(String.format("Couldn't update user %s password because of %s", user.getEmail(), e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Sets new user's role
     * @param userEmail - value of email field in database
     * @param role - new role for user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void setUserRole(String userEmail, Role role) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SET_ROLE)) {
            int k = 0;
            preparedStatement.setInt(++k, role.getValue());
            preparedStatement.setString(++k, userEmail);
            preparedStatement.execute();
        }catch (SQLException e) {
            log.error(String.format("Couldn't set user %s role because of %s", userEmail, e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Deletes user record in database
     * @param userId - value of id field in database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void delete(long userId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            int k = 0;
            preparedStatement.setLong(++k, userId);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(String.format("Couldn't delete user with id=%d because of %s", userId, e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Inserts new record for user_has_event table
     * @param userId - value of user_id field in database
     * @param eventId - value of event_id field in database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void registerForEvent(long userId, long eventId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_FOR_EVENT)) {
            setIds(userId, eventId, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(String.format("Couldn't register for event user with id=%d because of %s", userId, e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Deletes record in user_has_event table
     * @param userId - value of user_id field in database
     * @param eventId - value of event_id field in database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void cancelRegistration(long userId, long eventId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CANCEL_REGISTRATION)) {
            setIds(userId, eventId, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(String.format("Couldn't cancel registration for event user with id=%d because of %s",
                    userId, e.getMessage()));
            throw new DAOException(e);
        }
    }

    /**
     * Checks if record exists in user_has_event table
     * @param userId - value of user_id field in database
     * @param eventId - value of event_id field in database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public boolean isRegistered(long userId, long eventId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(IS_REGISTERED)) {
            setIds(userId, eventId, preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        }catch (SQLException e) {
            log.error(String.format("Couldn't check of user with id=%d registered for event because of %s",
                    userId, e.getMessage()));
            throw new DAOException(e);
        }
        return false;
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong(ID))
                .email(resultSet.getString(EMAIL))
                .name(resultSet.getString(NAME))
                .surname(resultSet.getString(SURNAME))
                .password(resultSet.getString(PASSWORD))
                .roleId(resultSet.getInt(ROLE_ID))
                .build();
    }

    private void setStatementFieldsForAddMethod(User user, PreparedStatement preparedStatement) throws SQLException {
        int k = 0;
        preparedStatement.setString(++k, user.getEmail());
        preparedStatement.setString(++k, user.getPassword());
        preparedStatement.setString(++k, user.getName());
        preparedStatement.setString(++k, user.getSurname());
    }

    private static void setStatementFieldsForUpdateMethod(User user, PreparedStatement preparedStatement)
            throws SQLException {
        int k = 0;
        preparedStatement.setString(++k, user.getEmail());
        preparedStatement.setString(++k, user.getName());
        preparedStatement.setString(++k, user.getSurname());
        preparedStatement.setLong(++k, user.getId());
    }

    private static void setIds(long userId, long eventId, PreparedStatement preparedStatement) throws SQLException {
        int k = 0;
        preparedStatement.setLong(++k, userId);
        preparedStatement.setLong(++k, eventId);
    }
}