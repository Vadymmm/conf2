package ua.java.conferences.utils.query;

import java.util.*;

import static ua.java.conferences.controller.actions.constants.ParameterValues.*;

/**
 * Abstract queryBuilder. Defines all methods to build query to obtain sorted, ordered and limited list of entities
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public abstract class QueryBuilder {
    private final List<String> filters = new ArrayList<>();
    private String sortField;
    private String order = ASCENDING_ORDER;
    private int offset = 0;
    private int records = 5;

    /**
     * @param sortField by default.
     */
    protected QueryBuilder(String sortField) {
        this.sortField = sortField;
    }

    /**
     * Creates concrete filter for query
     * @param userIdFilter user id for query
     * @return QueryBuilder (as Builder pattern)
     */
    public QueryBuilder setUserIdFilter(long userIdFilter) {
        filters.add("user_id=" + userIdFilter);
        return this;
    }

    /**
     * Creates date filter for query
     * @param dateFilter can be upcoming/passed or all by default
     * @return QueryBuilder (as Builder pattern)
     */
    public QueryBuilder setDateFilter(String dateFilter) {
        if (dateFilter != null && dateFilter.equals(PASSED)) {
            filters.add("date < now()");
        } else if (dateFilter != null && dateFilter.equals(UPCOMING)) {
            filters.add("date > now()");
        }
        return this;
    }

    /**
     * Creates role filter for users query
     * @param roleFilter can be any role value (1-4)
     * @return QueryBuilder (as Builder pattern)
     */
    public QueryBuilder setRoleFilter(String roleFilter) {
        if (roleFilter != null && isPositiveInt(roleFilter)) {
            filters.add("role_id=" + roleFilter);
        }
        return this;
    }

    /**
     * Sets sort field, but will check if it
     * @param sortField will be checked in subclasses to avoid injections
     * @return QueryBuilder (as Builder pattern)
     */
    public QueryBuilder setSortField(String sortField) {
        if (sortField != null) {
            this.sortField = checkSortField(sortField);
        }
        return this;
    }

    /**
     * Sets sorting order
     * @param order - sorting order (ASC by default)
     * @return QueryBuilder (as Builder pattern)
     */
    public QueryBuilder setOrder(String order) {
        if (order != null && order.equalsIgnoreCase(DESCENDING_ORDER)) {
            this.order = DESCENDING_ORDER;
        }
        return this;
    }

    /**
     * Sets limits for pagination
     * @param offset - record to start with. Checks if valid, set by default if not
     * @param records - number of records per page. Checks if valid, set by default if not
     * @return QueryBuilder (as Builder pattern)
     */
    public QueryBuilder setLimits(String offset, String records) {
        if (offset != null && isPositiveInt(offset)) {
            this.offset = Integer.parseInt(offset);
        }
        if (records != null && isPositiveInt(records)) {
            this.records = Integer.parseInt(records);
        }
        return this;
    }

    /**
     * @return complete query to use in DAO to obtain list of Entities
     */
    public String getQuery() {
        return getFilterQuery() + getGroupByQuery() + getSortQuery() + getLimitQuery();
    }

    /**
     * @return filter query to use in DAO to obtain number of records
     */
    public String getRecordQuery() {
        return getFilterQuery();
    }

    private String getFilterQuery() {
        StringJoiner stringJoiner = new StringJoiner(" AND ", " WHERE ", " ").setEmptyValue("");
        filters.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    /**
     * Should be implemented in subclasses
     * @return group by some field or empty
     */
    protected abstract String getGroupByQuery();

    private String getSortQuery() {
       return " ORDER BY " + sortField + " " + order;
    }

    private String getLimitQuery() {
        return " LIMIT " + offset + ", " + records;
    }

    /**
     * Should be implemented in subclasses
     *
     * @param sortField - field to sort entities
     * @return sort field if it's suitable or default
     */
    protected abstract String checkSortField(String sortField);

    private boolean isPositiveInt(String intString) {
        try {
            int i = Integer.parseInt(intString);
            if (i < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}