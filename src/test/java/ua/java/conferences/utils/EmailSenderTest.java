package ua.java.conferences.utils;

import com.icegreen.greenmail.util.*;
import org.junit.jupiter.api.*;

import javax.mail.MessagingException;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EmailSenderTest {

    private static GreenMail greenMail;
    private static final String SUBJECT = "some subject";
    private static final String BODY = "some body";
    private static final String SEND_TO = "to@localhost";

    @BeforeEach
    public void startMailServer() {
        greenMail = new GreenMail(ServerSetup.SMTP);
        greenMail.start();
    }

    @AfterEach
    public void stopMailServer() {
        greenMail.stop();
    }

    @Test
    void testSend() throws MessagingException, IOException {
        greenMail.setUser("user", "pass");
        new EmailSender(getProperties()).send(SUBJECT, BODY, SEND_TO);
        assertTrue(greenMail.waitForIncomingEmail(300, 1));
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        MimeMultipart multipart = (MimeMultipart) receivedMessage.getContent();
        assertEquals(BODY, multipart.getBodyPart(0).getContent().toString());
    }

    @Test
    void testSendError() {
        greenMail.setUser("user1", "pass1");
        new EmailSender(getProperties()).send(SUBJECT, BODY, SEND_TO);
        assertFalse(greenMail.waitForIncomingEmail(300, 1));
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.user", "user");
        properties.put("mail.password", "pass");
        properties.put("mail.transport.protocol", ServerSetup.SMTP);
        properties.put("mail.smtp.host", ServerSetup.SMTP.getBindAddress());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", ServerSetup.SMTP.getPort());
        return properties;
    }
}