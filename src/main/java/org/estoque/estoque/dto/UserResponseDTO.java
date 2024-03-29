package org.estoque.estoque.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class UserResponseDTO {
    private Long id;
    private String username;
    private Boolean enabled;
    private String Role;
}
