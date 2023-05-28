package ua.java.conferences.dto;

import lombok.*;
import java.io.Serializable;

/**
 * UserDTO class. Password field is absent.
 * Use UserDTO.builder().fieldName(fieldValue).build() to create an instance
 *
 * @author Vitalii Kalinchyk
 * @version 1.0
 */
@Data
@EqualsAndHashCode(of = {"email", "name", "surname"})
@Builder
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String email;
    private String name;
    private String surname;
    private String role;
}