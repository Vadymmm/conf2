package ua.java.conferences.utils;

import org.junit.jupiter.api.Test;
import ua.java.conferences.utils.query.EventQueryBuilder;
import ua.java.conferences.utils.query.QueryBuilder;
import ua.java.conferences.utils.query.UserQueryBuilder;
import ua.java.conferences.utils.query.VisitorEventQueryBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static ua.java.conferences.Constants.*;
import static ua.java.conferences.utils.QueryBuilderUtil.*;

class QueryBuilderTest {

    @Test
    void testUserQueryBuilder() {
        assertInstanceOf(UserQueryBuilder.class, userQueryBuilder());
    }

    @Test
    void testEventQueryBuilder() {
        assertInstanceOf(EventQueryBuilder.class, eventQueryBuilder());
    }

    @Test
    void testVisitorEventQueryBuilder() {
        assertInstanceOf(VisitorEventQueryBuilder.class, visitorEventQueryBuilder());
    }

    @Test
    void testSetIdFilter() {
        String query = eventQueryBuilder().setUserIdFilter(10).getQuery();
        assertTrue(query.contains(" user_id=" + TEN + " "));
    }

    @Test
    void testSetIdFilterNoFilter() {
        String query = eventQueryBuilder().getQuery();
        assertFalse(query.contains(" user_id="));
    }

    @Test
    void testSetIdFilterWrongClass() {
        QueryBuilder queryBuilder = userQueryBuilder();
        assertThrows(UnsupportedOperationException.class, () -> queryBuilder.setUserIdFilter(10));
    }

    @Test
    void testSetDateFilterNoDate() {
        String query = eventQueryBuilder().getQuery();
        assertFalse(query.contains("date"));
    }

    @Test
    void testSetDateFilterPassed() {
        String query = eventQueryBuilder().setDateFilter(PASSED).getQuery();
        assertTrue(query.contains(" date < now() "));
    }

    @Test
    void testSetDateFilterUpcoming() {
        String query = eventQueryBuilder().setDateFilter(UPCOMING).getQuery();
        assertTrue(query.contains(" date > now() "));
    }

    @Test
    void testSetDateFilterWrongClass() {
        QueryBuilder queryBuilder = userQueryBuilder();
        assertThrows(UnsupportedOperationException.class, () -> queryBuilder.setDateFilter(PASSED));
    }

    @Test
    void testSetRoleFilter() {
        String query = userQueryBuilder().setRoleFilter("4").getQuery();
        assertTrue(query.contains(" role_id=4 "));
    }

    @Test
    void testSetNegativeRoleFilter() {
        String query = userQueryBuilder().setRoleFilter("-4").getQuery();
        assertFalse(query.contains("role_id="));
    }

    @Test
    void testSetRoleFilterBadFilter() {
        String query = userQueryBuilder().setRoleFilter("a").getQuery();
        assertFalse(query.contains("role_id"));
    }

    @Test
    void testSetRoleFilterNoRole() {
        String query = userQueryBuilder().getQuery();
        assertFalse(query.contains("role_id"));
    }

    @Test
    void testSetRoleFilterWrongClass() {
        QueryBuilder queryBuilder = eventQueryBuilder();
        assertThrows(UnsupportedOperationException.class, () -> queryBuilder.setRoleFilter("4"));
    }

    @Test
    void testSetSortField() {
        String query = userQueryBuilder().setSortField(EMAIL_FIELD).getQuery();
        assertTrue(query.contains(" ORDER BY email ASC "));
    }


    @Test
    void testSetSortFieldForEvent() {
        String query = eventQueryBuilder().setSortField(TITLE_FIELD).getQuery();
        assertTrue(query.contains(" ORDER BY title ASC "));
    }

    @Test
    void testSetSortFieldEmpty() {
        String query = userQueryBuilder().getQuery();
        assertTrue(query.contains(" ORDER BY id ASC "));
    }

    @Test
    void testSetSortFieldEmptyForEvent() {
        String query = eventQueryBuilder().getQuery();
        assertTrue(query.contains(" ORDER BY event.id ASC "));
    }

    @Test
    void testSetWrongSortField() {
        String query = userQueryBuilder().setSortField(TITLE_FIELD).getQuery();
        assertTrue(query.contains(" ORDER BY id ASC "));
        assertFalse(query.contains(TITLE_FIELD));
    }

    @Test
    void testSetWrongSortFieldForEvent() {
        String query = eventQueryBuilder().setSortField(EMAIL_FIELD).getQuery();
        assertTrue(query.contains(" ORDER BY event.id ASC "));
        assertFalse(query.contains(EMAIL_FIELD));
    }

    @Test
    void testSetOrder() {
        String query = userQueryBuilder().setOrder(DESC).getQuery();
        assertTrue(query.contains(" ORDER BY id DESC "));
    }

    @Test
    void testSetOrderForEvent() {
        String query = eventQueryBuilder().setOrder(DESC).getQuery();
        assertTrue(query.contains(" ORDER BY event.id DESC "));
    }

    @Test
    void testSetLimitsNoLimits() {
        String query = userQueryBuilder().getQuery();
        assertTrue(query.contains(" LIMIT 0, 5"));
    }

    @Test
    void testSetLimits() {
        String query = userQueryBuilder().setLimits("5", "20").getQuery();
        assertTrue(query.contains(" LIMIT 5, 20"));
    }

    @Test
    void testSetWrongLimits() {
        String query = userQueryBuilder().setLimits("a", "a").getQuery();
        assertTrue(query.contains(" LIMIT 0, 5"));
    }

    @Test
    void testSetNegativeLimits() {
        String query = userQueryBuilder().setLimits("-5", "-10").getQuery();
        assertTrue(query.contains(" LIMIT 0, 5"));
    }

    @Test
    void testGetQuery() {
        String check = " WHERE role_id=3 ORDER BY name DESC LIMIT 3, 3";
        String query = userQueryBuilder()
                .setRoleFilter("3")
                .setSortField(NAME_FIELD)
                .setOrder(DESC)
                .setLimits("3", "3")
                .getQuery()
                .replaceAll("\\s+", " ");
        assertEquals(check, query);
    }

    @Test
    void testGetQueryForEvent() {
        String check = " WHERE user_id=3 AND date < now() GROUP BY event.id ORDER BY title DESC LIMIT 3, 3";
        String query = eventQueryBuilder()
                .setUserIdFilter(3)
                .setDateFilter(PASSED)
                .setSortField(TITLE_FIELD)
                .setOrder(DESC)
                .setLimits("3", "3")
                .getQuery()
                .replaceAll("\\s+", " ");
        assertEquals(check, query);
    }

    @Test
    void testGetQueryForVisitorEvent() {
        String check = " WHERE user_id=3 AND date < now() ORDER BY title DESC LIMIT 3, 3";
        String query = visitorEventQueryBuilder()
                .setUserIdFilter(3)
                .setDateFilter(PASSED)
                .setSortField(TITLE_FIELD)
                .setOrder(DESC)
                .setLimits("3", "3")
                .getQuery()
                .replaceAll("\\s+", " ");
        assertEquals(check, query);
    }

    @Test
    void testGetRecordQuery() {
        String check = " WHERE role_id=3 ";
        String query = userQueryBuilder()
                .setRoleFilter("3")
                .getRecordQuery()
                .replaceAll("\\s+", " ");
        assertEquals(check, query);
    }


    @Test
    void testGetRecordQueryForEvent() {
        String check = " WHERE user_id=3 AND date < now() ";
        String query = eventQueryBuilder()
                .setUserIdFilter(3)
                .setDateFilter(PASSED)
                .getRecordQuery()
                .replaceAll("\\s+", " ");
        assertEquals(check, query);
    }
}