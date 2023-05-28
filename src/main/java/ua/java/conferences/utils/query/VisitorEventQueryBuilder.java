package ua.java.conferences.utils.query;

/**
 * VisitorEventQueryBuilder. Able to build query to obtain sorted, ordered and limited list of events by user
 * participation
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class VisitorEventQueryBuilder extends EventQueryBuilder {

    /**
     * @return empty String - no need to group by in userQuery
     */
    @Override
    protected String getGroupByQuery() {
        return "";
    }
}
