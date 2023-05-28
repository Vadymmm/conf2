package ua.java.conferences.controller.filters.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

import static ua.java.conferences.controller.actions.constants.Pages.*;

/**
 * Contains pages sets for anonymous user and different roles logged user. Defines if user has access to the page
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainPagesSets {
    @Getter private static final Set<String> anonymousUserPages = new HashSet<>();
    private static final Set<String> loggedUserPages = new HashSet<>();
    @Getter private static final Set<String> visitorPages = new HashSet<>();
    @Getter private static final Set<String> speakerPages = new HashSet<>();
    @Getter private static final Set<String> moderatorPages = new HashSet<>();
    @Getter private static final Set<String> adminPages = new HashSet<>();

    static {
        anonymousUserPages.add(CONTROLLER_PAGE);
        anonymousUserPages.add(INDEX_PAGE);
        anonymousUserPages.add(ABOUT_PAGE);
        anonymousUserPages.add(CONTACTS_PAGE);
        anonymousUserPages.add(ERROR_PAGE);
        anonymousUserPages.add(SIGN_IN_PAGE);
        anonymousUserPages.add(SIGN_UP_PAGE);
        anonymousUserPages.add(RESET_PASSWORD_PAGE);
    }

    static {
        loggedUserPages.addAll(anonymousUserPages);
        loggedUserPages.add(PROFILE_PAGE);
        loggedUserPages.add(EDIT_PROFILE_PAGE);
        loggedUserPages.add(CHANGE_PASSWORD_PAGE);
    }

    static {
        visitorPages.addAll(loggedUserPages);
    }

    static {
        speakerPages.addAll(loggedUserPages);
    }

    static {
        moderatorPages.addAll(loggedUserPages);
        moderatorPages.add(CREATE_EVENT_PAGE);
        moderatorPages.add(SEARCH_EVENT_PAGE);
        moderatorPages.add(VIEW_EVENT_PAGE);
    }

    static {
        adminPages.addAll(loggedUserPages);
        adminPages.add(VIEW_USERS_PAGE);
        adminPages.add(SEARCH_USER_PAGE);
    }
}