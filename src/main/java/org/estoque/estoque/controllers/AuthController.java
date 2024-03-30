package org.estoque.estoque.controllers;

import org.estoque.estoque.dto.LoginRequestDTO;
import org.estoque.estoque.dto.TokenResponseDTO;
import org.estoque.estoque.services.JwtService;
import org.estoque.estoque.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class AuthController {
    private final JwtService service;
    private final UserService userService;

    public AuthController(JwtService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<TokenResponseDTO> generate(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication auth = userService.authenticate(loginRequestDTO);
        if (auth.isAuthenticated()) {
            String token = service.generate(loginRequestDTO.getUsername());
            TokenResponseDTO tokenDTOOutput = TokenResponseDTO.builder()
                    .token(token)
                    .build();
            return ResponseEntity.status(HttpStatus.OK.value()).body(tokenDTOOutput);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
    }
}
