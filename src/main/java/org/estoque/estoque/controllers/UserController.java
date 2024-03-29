package org.estoque.estoque.controllers;

import jakarta.validation.Valid;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.models.User;
import org.estoque.estoque.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return service.add(userRequestDTO);
    }
}
