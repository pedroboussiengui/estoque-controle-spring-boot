package org.estoque.estoque.controllers;

import jakarta.validation.Valid;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.exception.UserAlreadyExistsException;
import org.estoque.estoque.models.User;
import org.estoque.estoque.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return service.add(userRequestDTO).map(
            user -> ResponseEntity.status(HttpStatus.OK.value()).body(user))
            .orElseThrow(() -> new UserAlreadyExistsException("User with given username already exists."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        service.disable(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

}
