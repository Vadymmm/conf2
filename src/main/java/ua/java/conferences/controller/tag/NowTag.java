package ua.java.conferences.controller.tag;

import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.time.LocalDate;

/**
 * NowTag  class.
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
public class NowTag extends SimpleTagSupport {

    /**
     *  Writes to JspWriter formatted current date.
     */
    @Override
    public void doTag() throws IOException {
        final JspWriter writer = getJspContext().getOut();
        writer.print(LocalDate.now().plusDays(1));
    }
}