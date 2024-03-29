package org.estoque.estoque.controllers;

import jakarta.validation.Valid;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.dto.UserResponseDTO;
import org.estoque.estoque.exception.UserAlreadyExistsException;
import org.estoque.estoque.models.User;
import org.estoque.estoque.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return service.add(userRequestDTO).map(
            user -> ResponseEntity.status(HttpStatus.OK.value()).body(
                        UserResponseDTO.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .enabled(user.getEnabled())
                            .Role(user.getRole().name())
                            .build()
            ))
            .orElseThrow(() -> new UserAlreadyExistsException("User with given username already exists."));
    }

    @PutMapping("/disable/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> disableById(@PathVariable Long id) {
        service.changeStatus(id, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

    @PutMapping("/enable/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enableById(@PathVariable Long id) {
        service.changeStatus(id, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

}
