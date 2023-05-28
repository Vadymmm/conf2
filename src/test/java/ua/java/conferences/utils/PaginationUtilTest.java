package ua.java.conferences.utils;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.constants.Parameters.*;
import static ua.java.conferences.utils.PaginationUtil.paginate;

class PaginationUtilTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void testPaginate() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateZeroTotalRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(0, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(0, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(0, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateWrongOffset() {
        when(request.getParameter(OFFSET)).thenReturn("a");
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateWrongRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn("a");
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateNegativeOffset() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(-3));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateNegativeRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(-3));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateZeroRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(0));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateNull() {
        when(request.getParameter(OFFSET)).thenReturn(null);
        when(request.getParameter(RECORDS)).thenReturn(null);
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateOffsetNotZero() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(5));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(PAGES));
        assertEquals(2, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateSmallerRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(2));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(2, myRequest.getAttribute(RECORDS));
        assertEquals(50, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateSmallerTotal() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(10, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(2, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(2, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateSmallerTotal2() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(5));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(10, myRequest);
        assertEquals(5, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(2, myRequest.getAttribute(PAGES));
        assertEquals(2, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(2, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateSmallerTotal3() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(10));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(15, myRequest);
        assertEquals(10, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(3, myRequest.getAttribute(PAGES));
        assertEquals(3, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(3, myRequest.getAttribute(END));
    }

    @Test
    void testPaginateSmallerTotal4() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(5, myRequest);
        assertEquals(0, myRequest.getAttribute(OFFSET));
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(1, myRequest.getAttribute(PAGES));
        assertEquals(1, myRequest.getAttribute(CURRENT_PAGE));
        assertEquals(1, myRequest.getAttribute(START));
        assertEquals(1, myRequest.getAttribute(END));
    }

    private static class MyRequest extends HttpServletRequestWrapper {
        private final Map<String, Object> attributes = new HashMap<>();
        public MyRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public void setAttribute(String name, Object object) {
            attributes.put(name, object);
        }

        @Override
        public Object getAttribute(String name) {
            return attributes.get(name);
        }
    }
}