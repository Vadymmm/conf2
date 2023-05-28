package ua.java.conferences.utils.query;

import java.util.*;

import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * UserQueryBuilder. Able to build query to obtain sorted, ordered and limited list of users
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class UserQueryBuilder extends QueryBuilder {

    /** Contains set of allowed sort fields */
    private static final Set<String> USER_SORT_FIELDS_SET = new HashSet<>();

    static {
        USER_SORT_FIELDS_SET.add(ID);
        USER_SORT_FIELDS_SET.add(EMAIL);
        USER_SORT_FIELDS_SET.add(NAME);
        USER_SORT_FIELDS_SET.add(SURNAME);
    }

    /**
     * set id as default sort field
     */
    public UserQueryBuilder() {
        super(ID);
    }

    /**
     * @return empty String - no need to group by in userQuery
     */
    @Override
    protected String getGroupByQuery() {
        return "";
    }

    @Override
    protected String checkSortField(String sortField) {
        if (USER_SORT_FIELDS_SET.contains(sortField.toLowerCase())) {
            return sortField;
        }
        return ID;
    }

    @Override
    public UserQueryBuilder setUserIdFilter(long userIdFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserQueryBuilder setDateFilter(String dateFilter) {
        throw new UnsupportedOperationException();
    }
}
