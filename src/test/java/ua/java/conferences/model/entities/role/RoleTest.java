package ua.java.conferences.model.entities.role;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @ParameterizedTest
    @CsvSource({"ADMIN, 1", "MODERATOR, 2", "SPEAKER, 3", "VISITOR, 4"})
    void testGetValue(String roleString, String value) {
        Role role = Role.valueOf(roleString);
        int number = Integer.parseInt(value);
        assertEquals(number, role.getValue());
    }

    @ParameterizedTest
    @CsvSource({"ADMIN, 1", "MODERATOR, 2", "SPEAKER, 3", "VISITOR, 4"})
    void testGetRole(String roleString, String value) {
        Role role = Role.valueOf(roleString);
        int number = Integer.parseInt(value);
        assertEquals(role, Role.getRole(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,5,100})
    void testDefaultRole(int number) {
        assertEquals(Role.VISITOR, Role.getRole(number));
    }
}