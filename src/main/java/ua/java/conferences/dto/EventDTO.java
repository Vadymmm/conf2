package ua.java.conferences.dto;

import lombok.*;
import java.io.Serializable;

/**
 * EventDTO class. Fields are similar to Event entity, except date which is string
 * Use EventDTO.builder().fieldName(fieldValue).build() to create an instance
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Data
@EqualsAndHashCode(of = {"title", "date", "location"})
@Builder
public class EventDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String title;
    private String date;
    private String location;
    private String description;
    private int reports;
    private int registrations;
    private int visitors;
}