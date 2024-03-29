package org.estoque.estoque.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDTO {
    @NotEmpty(message = "The username can't be empty.")
    private String username;

    @NotEmpty(message = "The password can't be empty.")
    @Size(min = 3, message = "The password must have at least 3 characters.")
    private String password;

}
