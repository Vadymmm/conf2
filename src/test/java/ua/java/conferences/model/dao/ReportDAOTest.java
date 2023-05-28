package ua.java.conferences.model.dao;

import org.junit.jupiter.api.*;
import ua.java.conferences.exceptions.DAOException;
import ua.java.conferences.model.dao.mysql.MysqlReportDAO;
import ua.java.conferences.model.entities.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.model.dao.DAOTestUtils.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.model.dao.mysql.constants.SQLFields.*;

class ReportDAOTest {

    @Test
    void testAdd() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> reportDAO.add(getTestReport()));
        }
    }

    @Test
    void testAddNoSpeaker() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        Report testReport = getTestReport();
        testReport.setSpeaker(null);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> reportDAO.add(testReport));
        }
    }

    @Test
    void testSqlExceptionAdd() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.add(getTestReport()));
    }

    @Test
    void testGetById() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);

            Report resultReport = reportDAO.getById(ID_VALUE).orElse(null);
            assertNotNull(resultReport);
            assertEquals(getTestReport(), resultReport);

            User resultSpeaker = resultReport.getSpeaker();
            Event resultEvent = resultReport.getEvent();

            assertEquals(getTestUser().getId(), resultSpeaker.getId());
            assertEquals(getTestUser().getEmail(), resultSpeaker.getEmail());
            assertEquals(getTestUser().getName(), resultSpeaker.getName());
            assertEquals(getTestUser().getSurname(), resultSpeaker.getSurname());
            assertEquals(getTestEvent().getId(), resultEvent.getId());
            assertEquals(getTestEvent().getTitle(), resultEvent.getTitle());
            assertEquals(getTestEvent().getDate(), resultEvent.getDate());
            assertEquals(getTestEvent().getLocation(), resultEvent.getLocation());
        }
    }

    @Test
    void testGetByIdAbsent() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            Report resultReport = reportDAO.getById(ID_VALUE).orElse(null);
            assertNull(resultReport);
        }
    }

    @Test
    void testSqlExceptionGetById() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.getById(ID_VALUE));
    }

    @Test
    void testGetAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Report> reports = reportDAO.getAll();
            assertEquals(ONE, reports.size());
            assertEquals(getTestReport(), reports.get(0));
        }
    }

    @Test
    void testGetNoReports() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Report> reports = reportDAO.getAll();
            assertEquals(ZERO, reports.size());
        }
    }

    @Test
    void testSqlExceptionGetAll() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, reportDAO::getAll);
    }

    @Test
    void testGetEventsReports() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Report> reports = reportDAO.getEventsReports(ID_VALUE);
            assertEquals(ONE, reports.size());
            assertEquals(getTestReport(), reports.get(0));
        }
    }

    @Test
    void testGetEventsNoReports() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Report> reports = reportDAO.getEventsReports(ID_VALUE);
            assertEquals(ZERO, reports.size());
        }
    }

    @Test
    void testSqlExceptionGetEventsReports() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.getById(ID_VALUE));
    }

    @Test
    void testGetSpeakersReports() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);

            List<Report> reports = reportDAO.getSpeakersReports(ID_VALUE);
            assertEquals(ONE, reports.size());
            assertEquals(getTestReport(), reports.get(0));
        }
    }

    @Test
    void testGetSpeakersNoReports() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            List<Report> reports = reportDAO.getSpeakersReports(ID_VALUE);
            assertEquals(ZERO, reports.size());
        }
    }

    @Test
    void testSqlExceptionGetSpeakersReports() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.getSpeakersReports(ID_VALUE));
    }

    @Test
    void testUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> reportDAO.update(getTestReport()));
        }
    }

    @Test
    void testSqlExceptionUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.update(getTestReport()));
    }

    @Test
    void testDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> reportDAO.delete(ID_VALUE));
        }
    }

    @Test
    void testSqlExceptionDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.delete(ID_VALUE));
    }

    @Test
    void testSetSpeakerForReport() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement ignored = prepareUpdateQuery(dataSource)) {
            assertDoesNotThrow(() -> reportDAO.setSpeaker(ID_VALUE, ID_VALUE));
        }
    }

    @Test
    void testSqlExceptionSetSpeakerForReport() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.setSpeaker(ID_VALUE, ID_VALUE));
    }

    @Test
    void testDeleteSpeaker() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> reportDAO.deleteSpeaker(ID_VALUE));
        }
    }

    @Test
    void testSqlExceptionDeleteSpeaker() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportDAO reportDAO = new MysqlReportDAO(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> reportDAO.deleteSpeaker(ID_VALUE));
    }

    private PreparedStatement prepareMocks(DataSource dataSource) throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        doNothing().when(preparedStatement).setNull(isA(int.class), isA(int.class));
        when(preparedStatement.execute()).thenReturn(true);
        return preparedStatement;
    }

    private PreparedStatement prepareUpdateQuery(DataSource dataSource) throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));
        when(preparedStatement.executeUpdate()).thenReturn(1);
        return preparedStatement;
    }



    private static void prepareResultSet(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(ID)).thenReturn(ID_VALUE);
        when(resultSet.getString(TOPIC)).thenReturn(TOPIC_VALUE);
        when(resultSet.getLong(EVENT_ID)).thenReturn(ID_VALUE);
        when(resultSet.getString(TITLE)).thenReturn(TITLE_VALUE);
        when(resultSet.getString(DATE)).thenReturn(String.valueOf(DATE_VALUE));
        when(resultSet.getString(LOCATION)).thenReturn(LOCATION_VALUE);
        when(resultSet.getLong(USER_ID)).thenReturn(ID_VALUE);
        when(resultSet.getString(EMAIL)).thenReturn(EMAIL_VALUE);
        when(resultSet.getString(NAME)).thenReturn(NAME_VALUE);
        when(resultSet.getString(SURNAME)).thenReturn(SURNAME_VALUE);
    }
}