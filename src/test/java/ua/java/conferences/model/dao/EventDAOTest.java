package ua.java.conferences.model.dao;

import org.junit.jupiter.api.*;
import ua.java.conferences.model.dao.mysql.MysqlEventDAO;
import ua.java.conferences.model.entities.*;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.*;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.model.dao.DAOTestUtils.*;
import static ua.java.conferences.model.dao.mysql.constants.SQLFields.*;

class EventDAOTest {

    @Test
    void testAdd() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> eventDAO.add(getTestEvent()));
        }
    }

    @Test
    void testSqlExceptionAdd() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.add(getTestEvent()));
    }

    @Test
    void testGetById() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            Event resultEvent = eventDAO.getById(ID_VALUE).orElse(null);
            assertNotNull(resultEvent);
            assertEquals(getTestEvent(), resultEvent);
            assertEquals(getTestEvent().toString(), resultEvent.toString());
        }
    }

    @Test
    void testGetByIdAbsent() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Event resultEvent = eventDAO.getById(ID_VALUE).orElse(null);
            assertNull(resultEvent);
        }
    }

    @Test
    void testSqlExceptionGetById() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.getById(ID_VALUE));
    }

    @Test
    void testGetByTitle() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            Event resultEvent = eventDAO.getByTitle(TITLE_VALUE).orElse(null);
            assertNotNull(resultEvent);
            assertEquals(getTestEvent(), resultEvent);
        }
    }

    @Test
    void testGetByTitleAbsent() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Event resultEvent = eventDAO.getByTitle(TITLE_VALUE).orElse(null);
            assertNull(resultEvent);
        }
    }

    @Test
    void testSqlExceptionGetByTitle() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.getByTitle(TITLE_VALUE));
    }

    @Test
    void testGetAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Event> events = eventDAO.getAll();
            assertEquals(ONE, events.size());
            assertEquals(getTestEvent(), events.get(0));
        }
    }

    @Test
    void testGetNoEvents() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Event> events = eventDAO.getAll();
            assertEquals(ZERO, events.size());
        }
    }

    @Test
    void testSqlExceptionGetAll() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, eventDAO::getAll);
    }

    @Test
    void testGetSorted() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Event> events = eventDAO.getSorted("query");
            assertEquals(ONE, events.size());
            assertEquals(getTestEvent(), events.get(0));
        }
    }

    @Test
    void testGetSortedNoEvents() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Event> events = eventDAO.getSorted("query");
            assertEquals(ZERO, events.size());
        }
    }

    @Test
    void testSqlExceptionGetSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.getSorted("query"));
    }

    @Test
    void testGetSortedByUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Event> events = eventDAO.getSortedByUser("query", Role.VISITOR);
            assertEquals(ONE, events.size());
            assertEquals(getTestEvent(), events.get(0));
        }
    }

    @Test
    void testGetSortedByUserNoEvents() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Event> events = eventDAO.getSortedByUser("query", Role.VISITOR);
            assertEquals(ZERO, events.size());
        }
    }

    @Test
    void testSqlExceptionGetSortedByUser() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.getSortedByUser("query", Role.VISITOR));
    }

    @Test
    void testGetNumberOfRecordsByVisitor() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(NUMBER_OF_RECORDS)).thenReturn(100);
            int records = eventDAO.getNumberOfRecords("filter", Role.VISITOR);
            assertEquals(100, records);
        }
    }

    @Test
    void testGetNumberOfRecordsBySpeaker() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(NUMBER_OF_RECORDS)).thenReturn(100);
            int records = eventDAO.getNumberOfRecords("filter", Role.SPEAKER);
            assertEquals(100, records);
        }
    }

    @Test
    void testGetNumberOfRecordsByModerator() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(NUMBER_OF_RECORDS)).thenReturn(100);
            int records = eventDAO.getNumberOfRecords("filter", Role.MODERATOR);
            assertEquals(100, records);
        }
    }

    @Test
    void testSqlExceptionGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.getNumberOfRecords("filter", Role.VISITOR));
    }

    @Test
    void testUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> eventDAO.update(getTestEvent()));
        }
    }

    @Test
    void testSqlExceptionUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.update(getTestEvent()));
    }

    @Test
    void testSetVisitorsCount() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> eventDAO.setVisitorsCount(ID_VALUE, 100));
        }
    }

    @Test
    void testSqlExceptionSetVisitorsCount() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.setVisitorsCount(ID_VALUE, 100));
    }

    @Test
    void testDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> eventDAO.delete(ID_VALUE));
        }
    }

    @Test
    void testSqlExceptionDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        EventDAO eventDAO = new MysqlEventDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> eventDAO.delete(ID_VALUE));
    }

    private PreparedStatement prepareMocks(DataSource dataSource) throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        when(preparedStatement.execute()).thenReturn(true);
        return preparedStatement;
    }

    private static void prepareResultSet(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(ID)).thenReturn(ID_VALUE);
        when(resultSet.getString(TITLE)).thenReturn(TITLE_VALUE);
        when(resultSet.getDate(DATE)).thenReturn(Date.valueOf(DATE_VALUE));
        when(resultSet.getString(LOCATION)).thenReturn(LOCATION_VALUE);
        when(resultSet.getString(DESCRIPTION)).thenReturn(DESCRIPTION_VALUE);
        when(resultSet.getInt(REGISTRATIONS)).thenReturn(50);
        when(resultSet.getInt(VISITORS)).thenReturn(40);
        when(resultSet.getInt(REPORTS)).thenReturn(20);
    }
}