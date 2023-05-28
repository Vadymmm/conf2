package ua.java.conferences.utils;

import de.mkammerer.argon2.*;
import lombok.*;
import ua.java.conferences.exceptions.IncorrectPasswordException;

/**
 * Encode and verify encoded passwords. Uses Argon2 library to encode
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordHashUtil {
    private static final Argon2 argon2 = Argon2Factory.create();
    private static final int ITERATIONS = 2;
    private static final int MEMORY = 15*1024;
    private static final int PARALLELISM = 1;

    /**
     * Encode user's password
     * @param password to properly encode use not null value
     * @return encoded password
     */
    public static String encode(String password) {
        return password != null ? argon2.hash(ITERATIONS,MEMORY,PARALLELISM, password.toCharArray()) : "";
    }

    /**
     * Verify password
     * @param hash should be only encoded password
     * @param password - password value
     * @throws IncorrectPasswordException in case of bad verification
     */
    public static void verify(String hash, String password) throws IncorrectPasswordException {
        if (!argon2.verify(hash, password.toCharArray())) {
            throw new IncorrectPasswordException();
        }
    }
}