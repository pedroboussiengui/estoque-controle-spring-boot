package org.estoque.estoque.services;

import org.estoque.estoque.dto.LoginRequestDTO;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.models.User;
import org.estoque.estoque.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ModelMapper mapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
    }

    public User add(UserRequestDTO userRequestDTO) {
        User userEntity = this.mapper.map(userRequestDTO, User.class);
        userEntity.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        return repository.save(userEntity);
    }

    public Authentication authenticate(LoginRequestDTO loginRequestDTO) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()
        ));
    }

    public User find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
