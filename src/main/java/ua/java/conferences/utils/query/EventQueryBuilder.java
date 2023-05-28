package ua.java.conferences.utils.query;

import java.util.*;

import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * EventQueryBuilder. Able to build query to obtain sorted, ordered and limited list of events
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class EventQueryBuilder extends QueryBuilder {
    private static final String EVENT_DOT_ID = "event.id";
    /** Contains set of allowed sort fields */
    private static final Set<String> EVENT_SORT_FIELDS_SET = new HashSet<>();

    static {
        EVENT_SORT_FIELDS_SET.add(EVENT_DOT_ID);
        EVENT_SORT_FIELDS_SET.add(TITLE);
        EVENT_SORT_FIELDS_SET.add(DATE);
        EVENT_SORT_FIELDS_SET.add(LOCATION);
        EVENT_SORT_FIELDS_SET.add(REPORTS);
        EVENT_SORT_FIELDS_SET.add(REGISTRATIONS);
        EVENT_SORT_FIELDS_SET.add(VISITORS);
    }

    /**
     * set id as default sort field
     */
    public EventQueryBuilder() {
        super(EVENT_DOT_ID);
    }

    /**
     * @return concrete order by
     */
    @Override
    protected String getGroupByQuery() {
        return " GROUP BY " + EVENT_DOT_ID + " ";
    }

    @Override
    protected String checkSortField(String sortField) {
        if (EVENT_SORT_FIELDS_SET.contains(sortField.toLowerCase())) {
            return sortField;
        }
        return EVENT_DOT_ID;
    }

    @Override
    public EventQueryBuilder setRoleFilter(String roleFilter) {
        throw new UnsupportedOperationException();
    }
}
