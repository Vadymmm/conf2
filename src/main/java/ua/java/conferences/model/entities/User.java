package ua.java.conferences.model.entities;

import lombok.*;
import java.io.Serializable;

/**
 * User entity class. Matches table 'user' in database.
 * Use User.builder().fieldName(fieldValue).build() to create an instance
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Data
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String email;
    private transient String password;
    private String name;
    private String surname;
    @EqualsAndHashCode.Exclude private int roleId;
}