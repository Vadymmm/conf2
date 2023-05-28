package ua.java.conferences.utils;

import lombok.*;
import ua.java.conferences.utils.query.*;

/**
 * Factory to return concrete query builders
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryBuilderUtil {

    /**
     * @return UserQueryBuilder to create query for get sorted list of users
     */
    public static QueryBuilder userQueryBuilder() {
        return new UserQueryBuilder();
    }

    /**
     * @return EventQueryBuilder to create query for get sorted list of all events
     */
    public static QueryBuilder eventQueryBuilder() {
        return new EventQueryBuilder();
    }

    /**
     * @return VisitorEventQueryBuilder to create query for get sorted list of visitors/speakers events
     */
    public static QueryBuilder visitorEventQueryBuilder() {
        return new VisitorEventQueryBuilder();
    }
}