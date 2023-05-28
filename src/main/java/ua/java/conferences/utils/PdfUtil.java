package ua.java.conferences.utils;

import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import ua.java.conferences.dto.*;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Create required pdf docs with itext pdf library
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Slf4j
public class PdfUtil {
    private final ServletContext servletContext;

    /** Use this font fo cyrillic */
    private static final String FONT = "fonts/arial.ttf";
    private static final Color LIGHT_GREY = new DeviceRgb(220, 220, 220);
    private static final int TITLE_SIZE = 20;
    private static final Paragraph LINE_SEPARATOR = new Paragraph(new Text("\n"));
    private static final String USER_TITLE = "users";
    private static final String[] USER_CELLS = new String[]{"id", "email", "name", "surname", "role"};
    private static final String EVENT_TITLE = "events";
    private static final String[] EVENT_CELLS =
            new String[]{"title", "date", "location", "reports", "registrations", "visitors"};

    /**
     * @param servletContext to properly define way to font file
     */
    public PdfUtil(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Creates pdf document with Users' info. Creates resourceBundle to localize table fields
     * @param users - list of users to be placed in the document
     * @param locale - for localization purpose
     * @return outputStream to place in response
     */
    public ByteArrayOutputStream createUsersPdf(List<UserDTO> users, String locale) {
        ResourceBundle resourceBundle = getBundle(locale);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = getDocument(output);
        document.add(getTableTitle(resourceBundle.getString(USER_TITLE).toUpperCase()));
        document.add(LINE_SEPARATOR);
        document.add(getUserTable(users, resourceBundle));
        document.close();
        return output;
    }

    /**
     * Creates pdf document with Events' info. Creates resourceBundle to localize table fields
     * @param events - list of events to be placed in the document
     * @param locale - for localization purpose
     * @return outputStream to place in response
     */
    public ByteArrayOutputStream createEventsPdf(List<EventDTO> events, String locale) {
        ResourceBundle resourceBundle = getBundle(locale);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = getDocument(output);
        document.add(getTableTitle(resourceBundle.getString(EVENT_TITLE).toUpperCase()));
        document.add(LINE_SEPARATOR);
        document.add(getEventTable(events, resourceBundle));
        document.close();
        return output;
    }

    /**
     * Will create document with album orientation and font that supports cyrillic
     * @param output to create Document based on output
     * @return Document
     */
    private Document getDocument(ByteArrayOutputStream output) {
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        PdfFont font = getPdfFont();
        if (font != null) {
            document.setFont(font);
        }
        return document;
    }

    private static Paragraph getTableTitle(String tableTitle) {
        return new Paragraph(new Text(tableTitle))
                .setFontSize(TITLE_SIZE)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Table getUserTable(List<UserDTO> users, ResourceBundle resourceBundle) {
        Table table = new Table(new float[]{4, 12, 6, 6, 6});
        table.setWidth(UnitValue.createPercentValue(100));
        addTableHeader(table, USER_CELLS, resourceBundle);
        addUserTableRows(table, users);
        return table;
    }

    private Table getEventTable(List<EventDTO> events, ResourceBundle resourceBundle) {
        Table table = new Table(new float[]{7, 3, 3, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));
        addTableHeader(table, EVENT_CELLS, resourceBundle);
        addEventTableRows(table, events);
        return table;
    }

    private void addTableHeader(Table table, String[] cells, ResourceBundle resourceBundle) {
        Stream.of(cells)
                .forEach(columnTitle -> {
                    Cell header = new Cell();
                    header.setBackgroundColor(LIGHT_GREY);
                    header.setBorder(new SolidBorder(1));
                    header.add(new Paragraph(resourceBundle.getString(columnTitle)));
                    table.addCell(header);
                });
    }

    private void addUserTableRows(Table table, List<UserDTO> users) {
        users.forEach(user ->
                {
                    table.addCell(String.valueOf(user.getId()));
                    table.addCell(user.getEmail());
                    table.addCell(user.getName());
                    table.addCell(user.getSurname());
                    table.addCell(user.getRole());
                }
        );
    }

    private void addEventTableRows(Table table, List<EventDTO> events) {
        events.forEach(event -> {
            table.addCell(event.getTitle());
            table.addCell(event.getDate());
            table.addCell(event.getLocation());
            table.addCell(String.valueOf(event.getReports()));
            table.addCell(String.valueOf(event.getRegistrations()));
            table.addCell(String.valueOf(event.getVisitors()));
        }
        );
    }

    /**
     * @return font that support cyrillic or null if not able to load it
     */
    private PdfFont getPdfFont() {
        try {
            URL resource = servletContext.getResource(FONT);
            String fontUrl = resource.getFile();
            return PdfFontFactory.createFont(fontUrl);
        } catch (IOException e) {
            log.warn(String.format("Couldn't load font, will use default one because of %s", e.getMessage()));
            return null;
        }
    }

    /**
     * Obtains ResourceBundle based on locale. Works for any type - short - 'en', long - 'uk_UA'
     * @param locale to set ResourceBundle
     * @return ResourceBundle
     */
    private ResourceBundle getBundle(String locale) {
        String resources = "resources";
        if (locale.contains("_")) {
            String[] splitLocale = locale.split("_");
            return ResourceBundle.getBundle(resources, new Locale(splitLocale[0], splitLocale[1]));
        } else {
            return ResourceBundle.getBundle(resources, new Locale(locale));
        }
    }
}