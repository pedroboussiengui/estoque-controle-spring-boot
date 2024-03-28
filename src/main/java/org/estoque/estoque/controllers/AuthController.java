package org.estoque.estoque.controllers;

import org.estoque.estoque.dto.TokenResponseDTO;
import org.estoque.estoque.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class AuthController {

    private final JwtService service;

    public AuthController(JwtService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<TokenResponseDTO> generate() {
        String token = service.generate();
        TokenResponseDTO tokenDTOOutput = TokenResponseDTO.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(tokenDTOOutput);
    }

}
