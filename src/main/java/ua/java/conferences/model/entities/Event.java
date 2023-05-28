package ua.java.conferences.model.entities;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Event entity class. Matches table 'event' in database.
 * Use Event.builder().fieldName(fieldValue).build() to create an instance
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Data
@EqualsAndHashCode(of = {"id", "title", "date", "location"})
@Builder
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String title;
    private LocalDate date;
    private String location;
    private String description;
    private int reports;
    private int registrations;
    private int visitors;
}