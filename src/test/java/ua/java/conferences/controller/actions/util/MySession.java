package ua.java.conferences.controller.actions.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.*;

public class MySession implements HttpSession {
    private final Map<String, Object> attributes = new HashMap<>();

    public MySession(){}

    @Override
    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void invalidate() {}
    @Override
    public boolean isNew() {
        return false;
    }
    @Override
    public long getCreationTime() {
        return 0;
    }
    @Override
    public String getId() {
        return null;
    }
    @Override
    public long getLastAccessedTime() {
        return 0;
    }
    @Override
    public ServletContext getServletContext() {
        return null;
    }
    @Override
    public void setMaxInactiveInterval(int i) {}
    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }
    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }
}