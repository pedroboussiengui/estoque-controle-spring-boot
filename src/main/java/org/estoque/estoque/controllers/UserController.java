package org.estoque.estoque.controllers;

import jakarta.validation.Valid;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.dto.UserResponseDTO;
import org.estoque.estoque.exception.MessagesException;
import org.estoque.estoque.exception.UserAlreadyExistsException;
import org.estoque.estoque.models.User;
import org.estoque.estoque.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final ModelMapper mapper;

    public UserController(UserService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id ASC") String sort)
    {
        Page<User> users = service.list(page, size, sort);
        Map<String, Object> response = new HashMap<>();
        response.put("content", users.getContent()
                .stream().map(user -> this.mapper.map(user, UserResponseDTO.class)).toList());
        response.put("currentPage", users.getNumber());
        response.put("totalItems", users.getTotalElements());
        response.put("totalPages", users.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findById(@PathVariable Long id) {
        return service.find(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
