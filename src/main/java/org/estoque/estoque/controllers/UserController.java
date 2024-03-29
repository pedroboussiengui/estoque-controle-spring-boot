package org.estoque.estoque.controllers;

import jakarta.validation.Valid;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.dto.UserResponseDTO;
import org.estoque.estoque.exception.MessagesException;
import org.estoque.estoque.exception.UserAlreadyExistsException;
import org.estoque.estoque.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            .orElseThrow(() -> new UserAlreadyExistsException(MessagesException.USER_ALREADY_EXISTS));
    }

    @GetMapping
    public List<UserResponseDTO> list(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size)
    {
        //TODO: validation for params page and size
        return service.list();
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable Long id) {
        return service.find(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }


    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> disableById(@PathVariable Long id) {
        service.changeStatus(id, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enableById(@PathVariable Long id) {
        service.changeStatus(id, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

}
