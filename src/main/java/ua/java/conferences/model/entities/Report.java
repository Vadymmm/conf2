package ua.java.conferences.model.entities;

import lombok.*;
import java.io.Serializable;

/**
 * Report entity class. Matches table 'report' in database.
 * Use Report.builder().fieldName(fieldValue).build() to create an instance
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Data
@Builder
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String topic;
    @EqualsAndHashCode.Exclude private Event event;
    @EqualsAndHashCode.Exclude private User speaker;
}