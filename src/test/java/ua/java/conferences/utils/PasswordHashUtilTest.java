package ua.java.conferences.utils;

import org.junit.jupiter.api.Test;
import ua.java.conferences.exceptions.IncorrectPasswordException;

import static org.junit.jupiter.api.Assertions.*;
import static ua.java.conferences.utils.PasswordHashUtil.*;

class PasswordHashUtilTest {
    private static final String password = "PassWord1";
    private static final String wrongPassword = "Password1";

    @Test
    void testPasswordHashing() {
        String encoded = encode(password);
        assertNotEquals(password, encoded);
    }

    @Test
    void testVerifying() {
        String encoded = encode(password);
        assertDoesNotThrow(() -> verify(encoded, password));
    }

    @Test
    void testWrongPassword() {
        String encoded = encode(password);
        assertThrows(IncorrectPasswordException.class, () -> verify(encoded, wrongPassword));
    }
}