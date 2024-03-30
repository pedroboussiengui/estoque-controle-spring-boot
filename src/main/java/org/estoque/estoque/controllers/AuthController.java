package org.estoque.estoque.controllers;

import org.estoque.estoque.dto.LoginRequestDTO;
import org.estoque.estoque.dto.TokenResponseDTO;
import org.estoque.estoque.services.AuthService;
import org.estoque.estoque.services.JwtService;
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
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<TokenResponseDTO> generate(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication auth = authService.authenticate(loginRequestDTO);
        if (auth.isAuthenticated()) {
            String token = jwtService.generate(loginRequestDTO.getUsername());
            TokenResponseDTO tokenDTOOutput = TokenResponseDTO.builder()
                    .token(token)
                    .build();
            return ResponseEntity.status(HttpStatus.OK.value()).body(tokenDTOOutput);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
    }
}
