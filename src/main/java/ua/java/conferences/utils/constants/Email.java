package ua.java.conferences.utils.constants;

import lombok.*;

/**
 * Contains letter's subjects and bodies
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {
    /** For new users only */
    public static final String SUBJECT_GREETINGS = "Welcome to Conference Smart App!";

    /** Any notification letter subject */
    public static final String SUBJECT_NOTIFICATION = "Conference Smart App notification!";

    /** Place user's name instead of %s */
    public static final String HELLO = "Hello %s,<br>";
    public static final String INFORMATION = "We have some important information for you:";
    public static final String SIGNATURE = "Yours truly,<br>Conference Smart App team";
    public static final String A_HREF = "<a href=";
    public static final String DOUBLE_ENTER = "<br><br>";

    /** Place correct domain name instead of %s */
    public static final String MESSAGE_GREETINGS = HELLO +
            "Thank you for choosing Conference Smart App!<br>" +
            "We have prepared some useful information for you:" +
            DOUBLE_ENTER +
            "<h4>Upcoming Conferences</h4>" +
            "Check " + A_HREF + "%2$s" + "/controller?action=view-upcoming-events>upcoming events</a>, " +
            "register and enjoy listening to world famous Conference Smart App speakers." +
            DOUBLE_ENTER +
            "<h4>Your Conferences</h4>" +
            "Navigate through " + A_HREF + "%2$s" + "/controller?action=view-visitors-events>your conferences</a>, " +
            "check date, location, topics." +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place password and then correct domain name instead of %s */
    public static final String MESSAGE_RESET_PASSWORD = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "Your temporary password is %s. Do not forget to change it in your profile!" +
            DOUBLE_ENTER +
            "Enter your account " +
            A_HREF + "%s" + "/signIn.jsp>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place title, date, location, description, then correct domain name and event id instead of %s */
    public static final String MESSAGE_EVENT_CHANGED_VISITOR = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "The conference \"%s\" was changed due to important circumstances<br>" +
            "Conference date: %s <br>" +
            "Conference location: %s <br>" +
            "Conference description: %s" +
            DOUBLE_ENTER +
            "You can also check this conference " +
            A_HREF + "%s" + "/controller?action=view-event-by-visitor&event-id=%s>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place title, date, location, description, then correct domain name and event id instead of %s */
    public static final String MESSAGE_EVENT_CHANGED_SPEAKER = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "The conference \"%s\" was changed due to important circumstances<br>" +
            "Conference date: %s <br>" +
            "Conference location: %s <br>" +
            "Conference description: %s" +
            DOUBLE_ENTER +
            "You can also check this conference " +
            A_HREF + "%s" + "/controller?action=view-event-by-speaker&event-id=%s>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place title instead of %s */
    public static final String MESSAGE_EVENT_DELETED = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "The conference \"%s\" was canceled due to important circumstances" +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place title, date, location, topic, then correct domain name and event id instead of %s */
    public static final String MESSAGE_TOPIC_CHANGED = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "Topic for your report at %s (%s %s) was changed to %s" +
            "<br>" +
            "You can check this report " + A_HREF + "%s" + "/controller?action=view-event-by-speaker&event-id=%s>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place topic, title, date, location instead of %s */
    public static final String MESSAGE_REPORT_DELETED = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "The report \"%s\" at %s (%s %s)  was canceled due to important circumstances" +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place topic, title, date, location instead of %s */
    public static final String MESSAGE_REMOVE_SPEAKER = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "You were removed as speaker for report \"%s\" at %s (%s %s) due to important circumstances" +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place topic, title, date, location instead of %s */
    public static final String MESSAGE_SET_SPEAKER = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "You were set as speaker for report \"%s\" at %s (%s %s) due to important circumstances" +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place speaker's name, topic, title, then correct domain name and event id instead of %s */
    public static final String MESSAGE_OFFER_REPORT = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "Speaker %s offered report \"%s\" for %s" +
            DOUBLE_ENTER +
            "Check this conference " +
            A_HREF + "%s" + "/controller?action=view-event&event-id=%s>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place speaker's name, topic, title, then correct domain name and event id instead of %s */
    public static final String MESSAGE_SET_SPEAKER_BY_SPEAKER = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "%s set himself as speaker for  report \"%s\" at %s" +
            DOUBLE_ENTER +
            "Check this report " +
            A_HREF + "%s" + "/controller?action=view-report&report-id=%s>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;

    /** Place speaker's name, topic, title, then correct domain name and event id instead of %s */
    public static final String MESSAGE_REMOVE_SPEAKER_BY_SPEAKER = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "%s remove himself as speaker for  report \"%s\" at %s" +
            DOUBLE_ENTER +
            "Check this report " +
            A_HREF + "%s" + "/controller?action=view-report&report-id=%s>here</a>, " +
            DOUBLE_ENTER +
            SIGNATURE;
}