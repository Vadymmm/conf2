package ua.java.conferences.model.services.implementation;

import lombok.RequiredArgsConstructor;
import ua.java.conferences.dto.UserDTO;
import ua.java.conferences.model.dao.UserDAO;
import ua.java.conferences.model.entities.User;
import ua.java.conferences.model.entities.role.Role;
import ua.java.conferences.exceptions.*;
import ua.java.conferences.model.services.UserService;

import java.util.*;
import java.util.stream.IntStream;

import static ua.java.conferences.exceptions.constants.Message.*;
import static ua.java.conferences.utils.ConvertorUtil.*;
import static ua.java.conferences.utils.PasswordHashUtil.*;
import static ua.java.conferences.utils.ValidatorUtil.*;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

/**
 * Implementation of UserService interface.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    /** Contains userDAO field to work with UserDAO */
    private final UserDAO userDAO;
    private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random random = new Random();

    /**
     * Gets UserDTO from action and calls DAO to add relevant entity. Validate user's fields, passwords.
     * Encode password for database. Converts UserDTO to User
     * @param userDTO - DTO to be added as User to database
     * @param password - password to be added to User
     * @param confirmPassword - will check if passwords match
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException with specific message,
     * PasswordMatchingException, DuplicateEmailException.
     */
    @Override
    public void add(UserDTO userDTO, String password, String confirmPassword) throws ServiceException {
        validateUser(userDTO);
        validatePassword(password);
        checkPasswordMatching(password, confirmPassword);
        User user = convertDTOToUser(userDTO);
        user.setPassword(encode(password));
        try {
            userDAO.add(user);
        } catch (DAOException e) {
            checkExceptionType(e);
        }
    }

    /**
     * Checks if parameters are correct. Obtains necessary User entity and checks if password matches.
     * Converts UserDTO to User
     * @param email - to find user in database
     * @param password - to check if matches with user password
     * @return UserDTO - that matches this User entity
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchUserException, IncorrectPasswordException.
     */
    @Override
    public UserDTO signIn(String email, String password) throws ServiceException {
        checkStrings(email, password);
        UserDTO userDTO;
        try {
            User user = userDAO.getByEmail(email).orElseThrow(NoSuchUserException::new);
            verify(user.getPassword(), password);
            userDTO = convertUserToDTO(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTO;
    }

    /**
     * Obtains instance of User from DAO by id. Checks if id valid. Converts User to UserDTO
     * @param userIdString - id as a String to validate and convert to long
     * @return UserDTO instance
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchUserException
     */
    @Override
    public UserDTO getById(String userIdString) throws ServiceException {
        UserDTO userDTO;
        long userId = getUserId(userIdString);
        try {
            User user = userDAO.getById(userId).orElseThrow(NoSuchUserException::new);
            userDTO = convertUserToDTO(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTO;
    }

    /**
     * Obtains instance of User from DAO by email. Checks if email valid. Converts User to UserDTO
     * @param email - email to find User's instance
     * @return DTO instance
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchUserException
     */
    @Override
    public UserDTO getByEmail(String email) throws ServiceException {
        validateEmail(email);
        UserDTO userDTO;
        try {
            User user = userDAO.getByEmail(email).orElseThrow(NoSuchUserException::new);
            userDTO = convertUserToDTO(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTO;
    }

    /**
     * Obtains list of all instances of User from DAO. Converts Users to UserDTOs
     * @return List of UserDTOs
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<UserDTO> getAll() throws ServiceException {
        List<UserDTO> userDTOS = new ArrayList<>();
        try {
            List<User> users = userDAO.getAll();
            users.forEach(user -> userDTOS.add(convertUserToDTO(user)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTOS;
    }

    /**
     * Calls DAO to get sorted, filtered and limited list of DTOs. Converts Users to UserDTOs
     * @param query - to obtain necessary DTOs
     * @return List of UserDTOs that match demands
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<UserDTO> getSortedUsers(String query) throws ServiceException {
        List<UserDTO> userDTOS = new ArrayList<>();
        try {
            List<User> users = userDAO.getSorted(query);
            users.forEach(user -> userDTOS.add(convertUserToDTO(user)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTOS;
    }

    /**
     * Calls DAO to get users that participate in the event. Checks if id is valid. Converts Users to UserDTOs
     * @param eventId - id as a String to validate and convert to long
     * @param role - should be either VISITOR or SPEAKER
     * @return List of UserDTOs that match demands
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public List<UserDTO> getParticipants(String eventId, Role role) throws ServiceException {
        List<UserDTO> userDTOS = new ArrayList<>();
        try {
            List<User> users = userDAO.getParticipants(Long.parseLong(eventId), role);
            users.forEach(user -> userDTOS.add(convertUserToDTO(user)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTOS;
    }

    /**
     * Calls DAO to get number of all records that match filter
     * @param filter - conditions for such Users
     * @return number of records that match demands
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public int getNumberOfRecords(String filter) throws ServiceException {
        int records;
        try {
            records = userDAO.getNumberOfRecords(filter);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return records;
    }

    /**
     * Calls DAO to get all speakers. Prepare query. Converts Users to UserDTOs
     * @return list of UserDTOs with role = SPEAKER
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<UserDTO> getSpeakers() throws ServiceException {
        List<UserDTO> userDTOS = new ArrayList<>();
        try {
            List<User> speakers = userDAO.getSorted(userQueryBuilder().setRoleFilter("3").getQuery());
            speakers.forEach(user -> userDTOS.add(convertUserToDTO(user)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTOS;
    }

    /**
     * Calls DAO to get all moderators. Prepare query. Converts Users to UserDTOs
     * @return list of UserDTOs with role = MODERATOR
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public List<UserDTO> getModerators() throws ServiceException {
        List<UserDTO> userDTOS = new ArrayList<>();
        try {
            List<User> moderators = userDAO.getSorted(userQueryBuilder().setRoleFilter("2").getQuery());
            moderators.forEach(user -> userDTOS.add(convertUserToDTO(user)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userDTOS;
    }

    /**
     * Updates User's email, name, surname. Validate UserDTO. Converts UserDTO to User
     * @param dto - UserDTO that contains user's id, email, name and surname.
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException or
     * DuplicateEmailException
     */
    @Override
    public void update(UserDTO dto) throws ServiceException {
        validateUser(dto);
        User user = convertDTOToUser(dto);
        try {
            userDAO.update(user);
        } catch (DAOException e) {
            checkExceptionType(e);
        }
    }

    /**
     * Updates User's password. Validate passwords. Encode new password
     * @param userId - id to find user by
     * @param password - old password
     * @param newPass - new password
     * @param confirmPass - should match new password
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException,
     * IncorrectPasswordException, NoSuchUserException, PasswordMatchingException
     */
    @Override
    public void changePassword(long userId, String password, String newPass, String confirmPass) throws ServiceException {
        checkStrings(password, newPass, confirmPass);
        try {
            User user = userDAO.getById(userId).orElseThrow(NoSuchUserException::new);
            verify(user.getPassword(), password);
            checkPasswordMatching(newPass, confirmPass);
            validatePassword(newPass);
            User userToUpdate = User.builder().id(userId).password(encode(newPass)).build();
            userDAO.updatePassword(userToUpdate);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Calls DAO to update User with new password id. Generate new password. Encode new password.
     * @param userId id to find user by
     * @return new password for user
     * @throws ServiceException - may wrap DAOException or be thrown as IncorrectFormatException, NoSuchUserException.
     */
    @Override
    public String changePassword(long userId) throws ServiceException {
        String newPass = generatePassword();
        try {
            User user = User.builder().id(userId).password(encode(newPass)).build();
            userDAO.updatePassword(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return newPass;
    }

    /**
     * Calls DAO to set new user role
     * @param email - to find user by email
     * @param roleId new role for user
     * @throws ServiceException - may wrap DAOException
     */
    @Override
    public void setRole(String email, int roleId) throws ServiceException {
        try {
            Role role = Role.getRole(roleId);
            userDAO.setUserRole(email, role);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Deletes User entity from database. Validate id.
     * @param userIdString - id as a String
     * @throws ServiceException - may wrap DAOException or be thrown as NoSuchUserException
     */
    @Override
    public void delete(String userIdString) throws ServiceException {
        long userId = getUserId(userIdString);
        try {
            userDAO.delete(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Calls DAO to register User for Event. Validate id.
     * @param userId - user id
     * @param eventIdString - event id
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public void registerForEvent(long userId, String eventIdString) throws ServiceException {
        try {
            long eventId = getLong(eventIdString);
            userDAO.registerForEvent(userId, eventId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Calls DAO to cancel User registration for Event. Validate id.
     * @param userId - user id
     * @param eventIdString - event id
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public void cancelRegistration(long userId, String eventIdString) throws ServiceException {
        try {
            long eventId = getLong(eventIdString);
            userDAO.cancelRegistration(userId, eventId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Calls DAO to check if user registered for event. Validate id.
     * @param userId - user id
     * @param eventIdString - event id
     * @throws ServiceException - may wrap DAOException or be thrown by another mistakes
     */
    @Override
    public boolean isRegistered(long userId, String eventIdString) throws ServiceException {
        boolean result;
        try {
            long eventId = getLong(eventIdString);
            result = userDAO.isRegistered(userId, eventId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    /**
     * Specify Service Exception
     * @param e - exception thrown by DAO
     * @throws ServiceException in case of general SQLException and DuplicateEmailException if email is already
     * registered
     */
    private void checkExceptionType(DAOException e) throws ServiceException {
        if (e.getMessage().contains("Duplicate")) {
            throw new DuplicateEmailException();
        } else {
            throw new ServiceException(e);
        }
    }

    private void validateUser(UserDTO userDTO) throws IncorrectFormatException {
        validateEmail(userDTO.getEmail());
        validateName(userDTO.getName(), ENTER_CORRECT_NAME);
        validateName(userDTO.getSurname(), ENTER_CORRECT_SURNAME);
    }

    /**
     * Obtains new password for User
     * @return generated password
     */
    private String generatePassword() {
        return IntStream.generate(() -> random.nextInt(SYMBOLS.length()))
                .map(SYMBOLS::charAt)
                .limit(17)
                .collect(() -> new StringBuilder("1aA"), StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}