package ua.java.conferences.utils;

import javax.mail.*;
import javax.mail.internet.*;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Send emails to Users
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class EmailSender {
    private final String user;
    private final Session session;

    /**
     * @param properties - should contain all required fields to properly configure
     */
    public EmailSender(Properties properties) {
        user = properties.getProperty("mail.user");
        session = getSession(properties, user);
    }

    /**
     * Sends email to one User. Email sends in html/text format with some tags
     * @param subject - email's greetings
     * @param body - email's letter
     * @param sendTo - email's recipient
     */
    public void send(String subject, String body, String sendTo) {
        MimeMessage message = new MimeMessage(session);
        try {
            sendEmail(subject, body, sendTo, message);
            log.info(String.format("Email was send to %s", sendTo));
        } catch (MessagingException e) {
            log.error(String.format("Email wasn't send to %s because of %s", sendTo, e.getMessage()));
        }
    }

    private void sendEmail(String subject, String body, String sendTo, MimeMessage message) throws MessagingException {
        message.setFrom(new InternetAddress(user));
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        mimeBodyPart.setContent(body, "text/html; charset=utf-8");
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }

    private static Session getSession(Properties properties, String user) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, properties.getProperty("mail.password"));
            }
        });
    }
}