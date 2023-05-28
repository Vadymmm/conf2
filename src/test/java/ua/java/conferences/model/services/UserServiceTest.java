package ua.java.conferences.model.services;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import ua.java.conferences.model.dao.UserDAO;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.model.entities.User;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.implementation.UserServiceImpl;
import ua.java.conferences.utils.*;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.utils.PasswordHashUtil.*;
import static ua.java.conferences.exceptions.constants.Message.*;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

class UserServiceTest {
    private final UserDAO userDAO = mock(UserDAO.class);
    private final UserService userService = new UserServiceImpl(userDAO);
    private final Long ONE = 1L;

    @Test
    void testCorrectRegistration() throws DAOException {
       doNothing().when(userDAO).add(isA(User.class));
       UserDTO userDTO = getTestUserDTO();
        try (MockedStatic<ValidatorUtil> validator = mockStatic(ValidatorUtil.class);
             MockedStatic<ConvertorUtil> convertor = mockStatic(ConvertorUtil.class);
             MockedStatic<PasswordHashUtil> passwordHash = mockStatic(PasswordHashUtil.class)) {
            validator.when(() -> ValidatorUtil.validateEmail(anyString())).thenAnswer(invocationOnMock -> null);
            validator.when(() -> ValidatorUtil.validateName(anyString(), anyString())).thenAnswer(invocationOnMock -> null);
            validator.when(() -> ValidatorUtil.validatePassword(anyString())).thenAnswer(invocationOnMock -> null);
            convertor.when(() -> ConvertorUtil.convertDTOToUser(userDTO)).thenReturn(getTestUser());
            passwordHash.when(() -> PasswordHashUtil.encode(anyString())).thenReturn(PASSWORD_VALUE);
            assertDoesNotThrow(() -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
            validator.verify(() -> ValidatorUtil.validateEmail(anyString()));
            validator.verify(() -> ValidatorUtil.validateName(anyString(), anyString()), times(2));
            validator.verify(() -> ValidatorUtil.validatePassword(anyString()));
       }
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "email.com", "email@com", "email@epam.m", "email@epam.mmmmmmmm", "email@epam.444"})
    void testWrongEmailRegistration(String email) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setEmail(email);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(ENTER_CORRECT_EMAIL, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullEmailRegistration(String email) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setEmail(email);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(ENTER_CORRECT_EMAIL, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pass1", "PASSWORD", "password", "12345678", "PASSWORD1", "password1", "password1Password1234"})
    void testWrongPassRegistration(String password) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, password, password));
        assertEquals(ENTER_CORRECT_PASSWORD, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullPassRegistration(String password) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, password, password));
        assertEquals(ENTER_CORRECT_PASSWORD, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "123", "!!!", "$*$", "Julian 2", "X Æ A-12"})
    void testWrongNameRegistration(String name) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setName(name);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(ENTER_CORRECT_NAME, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullNameRegistration(String name) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setName(name);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(ENTER_CORRECT_NAME, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "123", "!!!", "$*$", "Julian 2", "X Æ A-12"})
    void testWrongSurnameRegistration(String surname) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setSurname(surname);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(ENTER_CORRECT_SURNAME, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullSurnameRegistration(String surname) throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setSurname(surname);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(ENTER_CORRECT_SURNAME, e.getMessage());
    }

    @Test
    void testDuplicateEmailRegistration() throws DAOException {
        doThrow(new DAOException(new SQLException("Duplicate entry"))).when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        DuplicateEmailException e = assertThrows(DuplicateEmailException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(DUPLICATE_EMAIL, e.getMessage());
    }

    @Test
    void testPasswordsDoNotMatch() throws DAOException {
        doNothing().when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        PasswordMatchingException e = assertThrows(PasswordMatchingException.class ,
                () -> userService.add(userDTO, PASSWORD_VALUE, INCORRECT_PASSWORD));
        assertEquals(PASSWORD_MATCHING, e.getMessage());
    }

    @Test
    void testDbIsDown() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).add(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        ServiceException e = assertThrows(ServiceException.class , () -> userService.add(userDTO, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(e.getCause(), exception);
        assertFalse(e.getMessage().contains("Duplicate entry"));
    }


    @Test
    void testSignIn() throws DAOException, ServiceException {
        User user = getTestUser();
        user.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getByEmail(EMAIL_VALUE)).thenReturn(Optional.of(user));
        assertEquals(getTestUserDTO(), userService.signIn(EMAIL_VALUE, PASSWORD_VALUE));
    }

    @Test
    void testWrongEmailSignIn() throws DAOException {
        when(userDAO.getByEmail(EMAIL_VALUE)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class,() -> userService.signIn(EMAIL_VALUE, PASSWORD_VALUE));
    }

    @Test
    void testWrongPasswordSignIn() throws DAOException {
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getByEmail(EMAIL_VALUE)).thenReturn(Optional.of(testUser));
        assertThrows(IncorrectPasswordException.class,() -> userService.signIn(EMAIL_VALUE, INCORRECT_PASSWORD));
    }

    @Test
    void testWrongDbSignIn() throws DAOException {
        when(userDAO.getByEmail(EMAIL_VALUE)).thenThrow(DAOException.class);
        assertThrows(ServiceException.class,() -> userService.signIn(EMAIL_VALUE, PASSWORD_VALUE));
    }

    @Test
    void testNullSignIn() {
        assertThrows(ServiceException.class,() -> userService.signIn(null, null));
    }

    @Test
    void testViewProfile() throws DAOException, ServiceException {
        when(userDAO.getById(ONE)).thenReturn(Optional.of(getTestUser()));
        assertEquals(getTestUserDTO(), userService.getById(String.valueOf(ONE)));
    }

    @Test
    void testViewProfileNoUser() throws DAOException {
        when(userDAO.getById(ONE)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class,() -> userService.getById(String.valueOf(ONE)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testViewProfileWrongId(String id) {
        assertThrows(NoSuchUserException.class,() -> userService.getById(id));
    }

    @Test
    void testViewProfileWrongId2() {
        assertThrows(NoSuchUserException.class,() -> userService.getById("id"));
    }

    @Test
    void testNullViewProfile() {
        assertThrows(ServiceException.class,() -> userService.getById(String.valueOf(ONE)));
    }

    @Test
    void testSQLErrorViewProfile() throws DAOException {
        when(userDAO.getById(ONE)).thenThrow(DAOException.class);
        assertThrows(ServiceException.class,() -> userService.getById(String.valueOf(ONE)));
    }

    @Test
    void testSearchUser() throws DAOException, ServiceException {
        when(userDAO.getByEmail(EMAIL_VALUE)).thenReturn(Optional.of(getTestUser()));
        assertEquals(getTestUserDTO(), userService.getByEmail(EMAIL_VALUE));
    }

    @Test
    void testSearchNoUser() throws DAOException {
        when(userDAO.getByEmail(EMAIL_VALUE)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class,() -> userService.getByEmail(EMAIL_VALUE));
    }

    @Test
    void testSearchIncorrectFormat() {
        IncorrectFormatException exception = assertThrows(IncorrectFormatException.class,
                () -> userService.getByEmail(INCORRECT_EMAIL));
        assertEquals(ENTER_CORRECT_EMAIL, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testSearchIncorrectFormat2(String email) {
        IncorrectFormatException exception = assertThrows(IncorrectFormatException.class,
                () -> userService.getByEmail(email));
        assertEquals(ENTER_CORRECT_EMAIL, exception.getMessage());
    }

    @Test
    void testSQLErrorSearchUser() throws DAOException {
        when(userDAO.getByEmail(EMAIL_VALUE)).thenThrow(DAOException.class);
        assertThrows(ServiceException.class,() -> userService.getByEmail(EMAIL_VALUE));
    }

    @Test
    void testViewUsers() throws DAOException, ServiceException {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();
        users.add(getTestUser());
        userDTOs.add(getTestUserDTO());
        when(userDAO.getAll()).thenReturn(users);
        assertIterableEquals(userDTOs, userService.getAll());
    }

    @Test
    void testSQLErrorViewUsers() throws DAOException {
        when(userDAO.getAll()).thenThrow(DAOException.class);
        assertThrows(ServiceException.class, userService::getAll);
    }

    @Test
    void testViewSortedUsers() throws DAOException, ServiceException {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();
        users.add(getTestUser());
        userDTOs.add(getTestUserDTO());
        String query = userQueryBuilder().getQuery();
        when(userDAO.getSorted(query)).thenReturn(users);
        assertIterableEquals(userDTOs, userService.getSortedUsers(query));
    }

    @Test
    void testSQLErrorViewSortedUsers() throws DAOException {
        when(userDAO.getSorted("query")).thenThrow(DAOException.class);
        assertThrows(ServiceException.class,() -> userService.getSortedUsers("query"));
    }

    @Test
    void testGetParticipants() throws DAOException, ServiceException {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();
        users.add(getTestUser());
        userDTOs.add(getTestUserDTO());
        when(userDAO.getParticipants(1, Role.VISITOR)).thenReturn(users);
        assertIterableEquals(userDTOs, userService.getParticipants("1", Role.VISITOR));
    }

    @Test
    void testSQLErrorGetParticipants() throws DAOException {
        when(userDAO.getParticipants(1, Role.VISITOR)).thenThrow(DAOException.class);
        assertThrows(ServiceException.class,() -> userService.getParticipants("1", Role.VISITOR));
    }

    @Test
    void testNumberOfRecords() throws DAOException, ServiceException {
        String filter = userQueryBuilder().getRecordQuery();
        when(userDAO.getNumberOfRecords(filter)).thenReturn(1);
        assertEquals(1, userService.getNumberOfRecords(filter));
    }

    @Test
    void testSQLErrorNumberOfRecords() throws DAOException {
        when(userDAO.getNumberOfRecords("filter")).thenThrow(DAOException.class);
        assertThrows(ServiceException.class,() -> userService.getNumberOfRecords("filter"));
    }

    @Test
    void testViewSpeakers() throws DAOException, ServiceException {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();
        users.add(getTestUser());
        userDTOs.add(getTestUserDTO());
        String query = userQueryBuilder().setRoleFilter("3").getQuery();
        when(userDAO.getSorted(query)).thenReturn(users);
        assertIterableEquals(userDTOs, userService.getSpeakers());
    }

    @Test
    void testSQLErrorViewSpeakers() throws DAOException {
        String query = userQueryBuilder().setRoleFilter("3").getQuery();
        when(userDAO.getSorted(query)).thenThrow(DAOException.class);
        assertThrows(ServiceException.class, userService::getSpeakers);
    }

    @Test
    void testViewModerators() throws DAOException, ServiceException {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();
        users.add(getTestUser());
        userDTOs.add(getTestUserDTO());
        String query = userQueryBuilder().setRoleFilter("2").getQuery();
        when(userDAO.getSorted(query)).thenReturn(users);
        assertIterableEquals(userDTOs, userService.getModerators());
    }

    @Test
    void testSQLErrorViewModerators() throws DAOException {
        String query = userQueryBuilder().setRoleFilter("2").getQuery();
        when(userDAO.getSorted(query)).thenThrow(DAOException.class);
        assertThrows(ServiceException.class, userService::getModerators);
    }

    @Test
    void testEditProfile() throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        assertDoesNotThrow(() -> userService.update(getTestUserDTO()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "email.com", "email@com", "email@epam.m", "email@epam.mmmmmmmm", "email@epam.444"})
    void testWrongEmailEditProfile(String email) throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setEmail(email);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.update(userDTO));
        assertEquals(ENTER_CORRECT_EMAIL, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullEmailEditProfile(String email) throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setEmail(email);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.update(userDTO));
        assertEquals(ENTER_CORRECT_EMAIL, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "123", "!!!", "$*$", "Julian 2", "X Æ A-12"})
    void testWrongNameEditProfile(String name) throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setName(name);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.update(userDTO));
        assertEquals(ENTER_CORRECT_NAME, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullNameEditProfile(String name) throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setName(name);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.update(userDTO));
        assertEquals(ENTER_CORRECT_NAME, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "123", "!!!", "$*$", "Julian 2", "X Æ A-12"})
    void testWrongSurnameEditProfile(String surname) throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setSurname(surname);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.update(userDTO));
        assertEquals(ENTER_CORRECT_SURNAME, e.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullSurnameEditProfile(String surname) throws DAOException {
        doNothing().when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        userDTO.setSurname(surname);
        IncorrectFormatException e = assertThrows(IncorrectFormatException.class ,
                () -> userService.update(userDTO));
        assertEquals(ENTER_CORRECT_SURNAME, e.getMessage());
    }

    @Test
    void testDuplicateEmailEditProfile() throws DAOException {
        doThrow(new DAOException(new SQLException("Duplicate entry"))).when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        DuplicateEmailException e = assertThrows(DuplicateEmailException.class ,
                () -> userService.update(userDTO));
        assertEquals(DUPLICATE_EMAIL, e.getMessage());
    }

    @Test
    void testSQLErrorEditProfile() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).update(isA(User.class));
        UserDTO userDTO = getTestUserDTO();
        ServiceException e = assertThrows(ServiceException.class , () -> userService.update(userDTO));
        assertEquals(e.getCause(), exception);
        assertFalse(e.getMessage().contains("Duplicate entry"));
    }

    @Test
    void testUpdatePassword() throws DAOException {
        doNothing().when(userDAO).updatePassword(isA(User.class));
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getById(ONE)).thenReturn(Optional.of(testUser));
        try (MockedStatic<ValidatorUtil> validator = mockStatic(ValidatorUtil.class)) {
            validator.when(() -> ValidatorUtil.validatePassword(anyString()))
                    .thenAnswer(invocation -> null);
            assertDoesNotThrow(() -> userService.changePassword(ONE, PASSWORD_VALUE, PASSWORD_VALUE, PASSWORD_VALUE));
        }
    }

    @Test
    void testUpdatePasswordException() throws DAOException {
        doNothing().when(userDAO).updatePassword(isA(User.class));
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getById(ONE)).thenReturn(Optional.of(testUser));
        try (MockedStatic<ValidatorUtil> validator = mockStatic(ValidatorUtil.class)) {
            validator.when(() -> ValidatorUtil.validatePassword(anyString()))
                    .thenThrow(IncorrectFormatException.class);
            assertThrows(IncorrectFormatException.class,
                    () -> userService.changePassword(ONE, PASSWORD_VALUE, PASSWORD_VALUE, PASSWORD_VALUE));
        }
    }

    @Test
    void testResetPassword() throws DAOException, ServiceException {
        doNothing().when(userDAO).updatePassword(isA(User.class));
        String password = userService.changePassword(ID_VALUE);
        assertEquals(20, password.length());
        assertEquals("1aA", password.substring(0,3));
    }

    @Test
    void testEditWrongNewPassword() throws DAOException {
        doNothing().when(userDAO).updatePassword(isA(User.class));
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getById(ONE)).thenReturn(Optional.of(testUser));
        assertThrows(IncorrectFormatException.class,
                () -> userService.changePassword(ONE, PASSWORD_VALUE, WRONG_PASSWORD, WRONG_PASSWORD));
    }

    @Test
    void testEditWrongOldPassword() throws DAOException {
        doNothing().when(userDAO).updatePassword(isA(User.class));
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getById(ONE)).thenReturn(Optional.of(testUser));
        assertThrows(IncorrectPasswordException.class,
                () -> userService.changePassword(ONE, WRONG_PASSWORD, PASSWORD_VALUE, PASSWORD_VALUE));
    }

    @Test
    void testEditWrongConfirmPassword() throws DAOException {
        doNothing().when(userDAO).updatePassword(isA(User.class));
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getById(ONE)).thenReturn(Optional.of(testUser));
        assertThrows(PasswordMatchingException.class,
                () -> userService.changePassword(ONE, PASSWORD_VALUE, PASSWORD_VALUE, WRONG_PASSWORD));
    }

    @Test
    void testEditNullPassword() {
        assertThrows(ServiceException.class,
                () -> userService.changePassword(ONE, null, null, null));
    }

    @Test
    void testSQLErrorEditPassword() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).getById(isA(Long.class));
        ServiceException e = assertThrows(ServiceException.class ,
                () -> userService.changePassword(ONE, PASSWORD_VALUE, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void testSQLErrorEditPassword2() throws DAOException {
        User testUser = getTestUser();
        testUser.setPassword(encode(PASSWORD_VALUE));
        when(userDAO.getById(ONE)).thenReturn(Optional.of(testUser));
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).updatePassword(isA(User.class));
        ServiceException e = assertThrows(ServiceException.class ,
                () -> userService.changePassword(ONE, PASSWORD_VALUE, PASSWORD_VALUE, PASSWORD_VALUE));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void testSQLErrorEditPassword3() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).updatePassword(isA(User.class));
        ServiceException e = assertThrows(ServiceException.class ,
                () -> userService.changePassword(ONE));
        assertEquals(e.getCause(), exception);
        assertFalse(e.getMessage().contains("Duplicate entry"));
    }


    @Test
    void testSetRole() throws DAOException {
        doNothing().when(userDAO).setUserRole(isA(String.class), isA(Role.class));
        assertDoesNotThrow(() -> userService.setRole(EMAIL_VALUE, ROLE_ID_VALUE));
    }

    @Test
    void testSQLErrorSetRole() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).setUserRole(isA(String.class), isA(Role.class));
        ServiceException e = assertThrows(ServiceException.class, () -> userService.setRole(EMAIL_VALUE, ROLE_ID_VALUE));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void testDeleteUser() throws DAOException {
        doNothing().when(userDAO).delete(isA(long.class));
        assertDoesNotThrow(() -> userService.delete(String.valueOf(ONE)));
    }

    @Test
    void testSQLErrorDeleteUser() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).delete(isA(Long.class));
        ServiceException e = assertThrows(ServiceException.class, () -> userService.delete(String.valueOf(ONE)));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void testRegisterForEvent() throws DAOException {
        doNothing().when(userDAO).registerForEvent(isA(long.class), isA(long.class));
        assertDoesNotThrow(() -> userService.registerForEvent(ONE, String.valueOf(ONE)));
    }

    @Test
    void testSQLErrorRegisterForEvent() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).registerForEvent(isA(long.class), isA(long.class));
        ServiceException e = assertThrows(ServiceException.class,
                () -> userService.registerForEvent(ONE, String.valueOf(ONE)));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void testCancelRegistration() throws DAOException {
        doNothing().when(userDAO).cancelRegistration(isA(long.class), isA(long.class));
        assertDoesNotThrow(() -> userService.cancelRegistration(ONE, String.valueOf(ONE)));
    }

    @Test
    void testSQLErrorCancelRegistration() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).cancelRegistration(isA(long.class), isA(long.class));
        ServiceException e = assertThrows(ServiceException.class,
                () -> userService.cancelRegistration(ONE, String.valueOf(ONE)));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void TestIsRegistered() throws DAOException, ServiceException {
        when(userDAO.isRegistered(ONE, ONE)).thenReturn(true);
        assertTrue(userService.isRegistered(ONE, String.valueOf(ONE)));
    }

    @Test
    void TestIsNotRegistered() throws DAOException, ServiceException {
        when(userDAO.isRegistered(ONE, ONE)).thenReturn(false);
        assertFalse(userService.isRegistered(ONE, String.valueOf(ONE)));
    }

    @Test
    void testSQLErrorIsRegistered() throws DAOException {
        Exception exception = new DAOException(new SQLException());
        doThrow(exception).when(userDAO).isRegistered(isA(long.class), isA(long.class));
        ServiceException e = assertThrows(ServiceException.class,
                () -> userService.isRegistered(ONE, String.valueOf(ONE)));
        assertEquals(e.getCause(), exception);
    }

    @Test
    void testServiceException() {
        assertNotEquals(new ServiceException(), new ServiceException());
    }

    private UserDTO getTestUserDTO() {
        return UserDTO.builder()
                .id(ONE)
                .email(EMAIL_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .role(ROLE_VISITOR)
                .build();
    }

    private User getTestUser() {
        return User.builder()
                .id(ONE)
                .email(EMAIL_VALUE)
                .password(PASSWORD_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .roleId(4)
                .build();
    }
}