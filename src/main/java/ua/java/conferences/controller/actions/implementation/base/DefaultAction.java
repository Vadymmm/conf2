package ua.java.conferences.controller.actions.implementation.base;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.controller.actions.Action;

import static ua.java.conferences.controller.actions.constants.Pages.INDEX_PAGE;

/**
 * This is DefaultAction class. Usually called if there is a mistake in action name.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class DefaultAction implements Action {

    /**
     * @return index page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        log.info("For some reason default action was called");
        return INDEX_PAGE;
    }
}