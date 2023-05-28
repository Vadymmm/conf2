package ua.java.conferences.controller.filters.domain;

import ua.java.conferences.model.entities.role.Role;

import java.util.Set;

/**
 * Checks if user can access page or use action
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class Domain {
    /** contains page name */
    private final String servletPath;

    /** contains action name */
    private final String action;

    /** contains user allowed pages */
    private Set<String> domainPages;

    /** contains user allowed actions */
    private Set<String> domainActions;

    private Domain(String servletPath, String action) {
        this.servletPath = servletPath;
        this.action = action;
        setDomains();
    }

    private Domain(String servletPath, String action, String role) {
        this.servletPath = servletPath;
        this.action = action;
        setDomains(role);
    }

    /**
     * Obtains Domain for anonymous user
     * @param servletPath - contains page to access
     * @param action - contains action to call
     * @return Domain
     */
    public static Domain getDomain(String servletPath, String action) {
        return new Domain(servletPath, action);
    }

    /**
     * Obtains Domain for logged user
     * @param servletPath - contains page to access
     * @param action - contains action to call
     * @param role - user's role
     * @return Domain
     */
    public static Domain getDomain(String servletPath, String action, String role) {
        return new Domain(servletPath, action, role);
    }

    private void setDomains() {
        domainPages = DomainPagesSets.getAnonymousUserPages();
        domainActions = DomainActionsSets.getAnonymousUserActions();
    }

    private void setDomains(String role) {
        Role roleValue = Role.valueOf(role);
        switch (roleValue) {
            case SPEAKER: domainPages = DomainPagesSets.getSpeakerPages();
                          domainActions = DomainActionsSets.getSpeakerActions();
                          break;
            case MODERATOR: domainPages = DomainPagesSets.getModeratorPages();
                            domainActions = DomainActionsSets.getModeratorActions();
                            break;
            case ADMIN: domainPages = DomainPagesSets.getAdminPages();
                        domainActions = DomainActionsSets.getAdminActions();
                        break;
            default: domainPages = DomainPagesSets.getVisitorPages();
                     domainActions = DomainActionsSets.getVisitorActions();
        }
    }

    /**
     * Checks if user allowed to access page or call action
     * @return false if not allowed
     */
    public boolean checkAccess() {
        return !checkPages() || !checkActions();
    }

    private boolean checkPages() {
        if (servletPath != null) {
            return domainPages.contains(servletPath.substring(1));
        }
        return true;
    }

    private boolean checkActions() {
        if (action != null) {
            return domainActions.contains(action);
        }
        return true;
    }
}