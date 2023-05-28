package ua.java.conferences.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.java.conferences.exceptions.CaptchaException;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CaptchaTest {

    private final String[] rightValues = {"https://www.google.com/recaptcha/api/siteverify",
                                        "1234",
                                        "POST",
                                        "Mozilla/5.0",
                                        "en-US,en;q=0.5"};
    private final String[] wrongValues = {"https://somestupidnamesite8123.com/",
                                        "123",
                                        "POST",
                                        "Mozilla/5.0",
                                        "en-US,en;q=0.5"};

    @ParameterizedTest
    @ValueSource(strings = {"1", "word", "true", "human"})
    void testVerify(String reCaptcha) {
        assertThrows(CaptchaException.class, () -> new Captcha(getProperties(rightValues)).verify(reCaptcha));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "word", "true", "human"})
    void testVerifyWrongConnection(String reCaptcha) {
        assertDoesNotThrow(() -> new Captcha(getProperties(wrongValues)).verify(reCaptcha));
    }

    private Properties getProperties(String[] values) {
        Properties properties = new Properties();
        properties.put("captcha.url", values[0]);
        properties.put("captcha.secret", values[1]);
        properties.put("captcha.method", values[2]);
        properties.put("user-agent", values[3]);
        properties.put("accept-language", values[4]);
        return properties;
    }
}