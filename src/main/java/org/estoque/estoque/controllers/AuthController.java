package org.estoque.estoque.controllers;

import org.estoque.estoque.dto.TokenDTOOutput;
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
    public ResponseEntity<TokenDTOOutput> generate() {
        String token = service.generate();
        TokenDTOOutput tokenDTOOutput = TokenDTOOutput.builder()
                .token(token)
                .build();
        System.out.println(tokenDTOOutput);
        return ResponseEntity.ok(tokenDTOOutput);
    }

}
