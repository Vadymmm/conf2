package ua.java.conferences.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import static ua.java.conferences.controller.actions.constants.Parameters.*;

/**
 * Configure request to set all required for page surfing attributes
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationUtil {

    /**
     * Calculates required attributes and sets them to request. In case of wrong offset and/or records - sets
     * default values (as for now 0 and 5)
     * @param totalRecords - total number of records
     * @param request - request to get offset, records and to set other attributes
     */
    public static void paginate(int totalRecords, HttpServletRequest request) {
        int offset = getInt(request.getParameter(OFFSET), 0, 0);
        int records = getInt(request.getParameter(RECORDS), 1, 5);
        setAttributes(request, totalRecords, records, offset);
    }

    private static void setAttributes(HttpServletRequest request, int totalRecords, int records, int offset) {
        int pages = totalRecords / records + (totalRecords % records != 0 ? 1 : 0);
        int currentPage = offset / records + 1;
        int startPage = currentPage == pages ? Math.max(currentPage - 2, 1)
                                             : Math.max(currentPage - 1, 1);
        int endPage = Math.min(startPage + 2, pages);
        request.setAttribute(OFFSET, offset);
        request.setAttribute(RECORDS, records);
        request.setAttribute(PAGES, pages);
        request.setAttribute(CURRENT_PAGE, currentPage);
        request.setAttribute(START, startPage);
        request.setAttribute(END, endPage);
    }

    private static int getInt(String value, int min, int defaultValue) {
        try {
            int records = Integer.parseInt(value);
            if (records >= min) {
                return records;
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return defaultValue;
    }
}