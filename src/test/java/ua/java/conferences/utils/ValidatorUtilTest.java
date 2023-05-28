package ua.java.conferences.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.java.conferences.exceptions.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ua.java.conferences.exceptions.constants.Message.*;
import static ua.java.conferences.utils.ValidatorUtil.*;

class ValidatorUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"kory@amber.com.tv", "kory@amber.co", "kory.kory@.amber.com", "email@em.coma"})
    void testValidateEmail(String email) {assertDoesNotThrow(() -> validateEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"kory.amber.com.tv", "kory@amber", "kory@.amber", "@amber.com.tv", "kory@amber.com.t"})
    void testValidateBadEmail(String email) {
        IncorrectFormatException exception = assertThrows(IncorrectFormatException.class, () -> validateEmail(email));
        assertEquals(ENTER_CORRECT_EMAIL, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testValidateEmptyEmail(String email) {
        IncorrectFormatException exception = assertThrows(IncorrectFormatException.class, () -> validateEmail(email));
        assertEquals(ENTER_CORRECT_EMAIL, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1", "Password1_", "passWord1", "1Password", "1passWord"})
    void testValidatePassword(String password) {assertDoesNotThrow(() -> validatePassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"NoDigitPass", "no_upper_letters1", "NO_LOW_CASE_1", "Short1", "TooLongPassword1234567890"})
    void testValidateBadPassword(String password) {assertThrows(IncorrectFormatException.class,
            () -> validatePassword(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testValidateEmptyPassword(String password) {
        assertThrows(IncorrectFormatException.class, () -> validatePassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Taras", "Joe Biden", "taras", "Залужний", "Квітка-Основ'яненко"})
    void testValidateName(String name) {
        assertDoesNotThrow(() -> validateName(name, ENTER_CORRECT_NAME));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Taras2", "Joe Biden 2", "Залужный", "Занадтодовгеім'ямаєбутинебільшетридцятисимволів"})
    void testValidateBadName(String name) {
        IncorrectFormatException exception =
                assertThrows(IncorrectFormatException.class, () -> validateName(name, ENTER_CORRECT_NAME));
        assertEquals(ENTER_CORRECT_NAME, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testValidateEmptyName(String name) {
        IncorrectFormatException exception =
                assertThrows(IncorrectFormatException.class, () -> validateName(name, ENTER_CORRECT_NAME));
        assertEquals(ENTER_CORRECT_NAME, exception.getMessage());
    }

    @Test
    void testValidateComplexName() {
        String topic = "SQL. Introduction to SQL and JDBC";
        assertDoesNotThrow(() -> validateComplexName(topic, ENTER_CORRECT_TOPIC));

        String title = "Java for Students Summer 2022";
        assertDoesNotThrow(() -> validateComplexName(title, ENTER_CORRECT_TITLE));

        String location = "Київ, вул. Хрещатик 1";
        assertDoesNotThrow(() -> validateComplexName(location, ENTER_CORRECT_LOCATION));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testValidateEmptyComplexName(String title) {
        IncorrectFormatException exception =
                assertThrows(IncorrectFormatException.class, () -> validateComplexName(title, ENTER_CORRECT_TITLE));
        assertEquals(ENTER_CORRECT_TITLE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "Java for Students Summer 2022. Занадто довге ім'я має бути не більше 70 символів"})
    void testValidateBadComplexName(String topic) {
        IncorrectFormatException exception =
                assertThrows(IncorrectFormatException.class, () -> validateComplexName(topic, ENTER_CORRECT_TOPIC));
        assertEquals(ENTER_CORRECT_TOPIC, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2, 1000})
    void testValidateDate(int i) {
        LocalDate localDate = LocalDate.now().plusDays(i);
        assertDoesNotThrow(() -> validateDate(localDate));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1,2,1000})
    void testValidateBadDate(int i) {
        LocalDate localDate = LocalDate.now().minusDays(i);
        IncorrectFormatException exception = assertThrows(IncorrectFormatException.class, () -> validateDate(localDate));
        assertEquals(ENTER_VALID_DATE, exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void testValidateEmptyDate(LocalDate localDate) {
        IncorrectFormatException exception = assertThrows(IncorrectFormatException.class, () -> validateDate(localDate));
        assertEquals(ENTER_VALID_DATE, exception.getMessage());
    }

    @Test
    void testValidateDescription() {
        String description = "Epam conference for Ukrainian students. Winter 2023";
        assertDoesNotThrow(() -> validateDescription(description));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testValidateBadDescription(String description) {
        IncorrectFormatException exception =
                assertThrows(IncorrectFormatException.class, () -> validateDescription(description));
        assertEquals(ENTER_CORRECT_DESCRIPTION, exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"123, 123", "asd, asd", "LONG_WORD, LONG_WORD"})
    void testPasswordMatching(String pass1, String pass2) {
        assertDoesNotThrow(() -> checkPasswordMatching(pass1, pass2));
    }

    @ParameterizedTest
    @CsvSource({"123, asd", "asd, LONG_WORD", "LONG_WORD, 123"})
    void testBadPasswordMatching(String pass1, String pass2) {
        PasswordMatchingException exception =
                assertThrows(PasswordMatchingException.class, () -> checkPasswordMatching(pass1, pass2));
        assertEquals(PASSWORD_MATCHING, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","2","1000"})
    void testGetEventId(String number) throws ServiceException {
        assertEquals(Long.parseLong(number), getEventId(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a","asd","112a"})
    void testBadGetEventId(String number) {
        NoSuchEventException exception = assertThrows(NoSuchEventException.class, () -> getEventId(number));
        assertEquals(NO_EVENT, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyGetEventId(String number) {
        NoSuchEventException exception = assertThrows(NoSuchEventException.class, () -> getEventId(number));
        assertEquals(NO_EVENT, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","2","1000"})
    void testGetReportId(String number) throws ServiceException {
        assertEquals(Long.parseLong(number), getReportId(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a","asd","112a"})
    void testBadGetReportId(String number) {
        NoSuchReportException exception = assertThrows(NoSuchReportException.class, () -> getReportId(number));
        assertEquals(NO_REPORT, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyGetReportId(String number) {
        NoSuchReportException exception = assertThrows(NoSuchReportException.class, () -> getReportId(number));
        assertEquals(NO_REPORT, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","2","1000"})
    void testGetUserId(String number) throws ServiceException {assertEquals(Long.parseLong(number), getUserId(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a","asd","112a"})
    void testBadGetUserId(String number) {
        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () -> getUserId(number));
        assertEquals(NO_USER, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyGetUserId(String number) {
        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () -> getUserId(number));
        assertEquals(NO_USER, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","2","1000"})
    void testGetLong(String number) throws ServiceException {
        assertEquals(Long.parseLong(number), getLong(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a","asd","112a"})
    void testBadGetLong(String number) {
        ServiceException exception = assertThrows(ServiceException.class, () -> getLong(number));
        assertTrue(exception.getMessage().contains("NumberFormatException"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyGetLong(String number) {
        ServiceException exception = assertThrows(ServiceException.class, () -> getLong(number));
        assertTrue(exception.getMessage().contains("NumberFormatException"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","2","1000"})
    void testGetInt(String number) throws ServiceException {
        assertEquals(Long.parseLong(number), getInt(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a","asd","112a"})
    void testBadGetInt(String number) {
        ServiceException exception = assertThrows(ServiceException.class, () -> getInt(number));
        assertTrue(exception.getMessage().contains("NumberFormatException"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyGetInt(String number) {
        ServiceException exception = assertThrows(ServiceException.class, () -> getInt(number));
        assertTrue(exception.getMessage().contains("NumberFormatException"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","asd","1000"})
    void testCheckStrings(String number) {
        assertDoesNotThrow(() -> checkStrings(number));
    }

    @Test
    void testCheckManyStrings() {
        assertDoesNotThrow(() -> checkStrings("1","asd","1000"));
    }

    @Test
    void testNullString() {
        ServiceException exception = assertThrows(ServiceException.class, () -> checkStrings("1",null,"1000"));
        assertTrue(exception.getMessage().contains("NullPointerException"));
    }
}