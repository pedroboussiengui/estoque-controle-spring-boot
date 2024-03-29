package org.estoque.estoque.services;

import org.estoque.estoque.dto.LoginRequestDTO;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.exception.UserNotFoundException;
import org.estoque.estoque.models.User;
import org.estoque.estoque.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<User> add(UserRequestDTO userRequestDTO) {
        User user = repository.findByUsername(userRequestDTO.getUsername()).orElse(null);
        if (user == null) {
            User userEntity = this.mapper.map(userRequestDTO, User.class);
            userEntity.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            userEntity.setEnabled(true);
            return Optional.of(repository.save(userEntity));
        }
        return Optional.empty();
    }

    public Authentication authenticate(LoginRequestDTO loginRequestDTO) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()
        ));
    }

    public User find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public void changeStatus(Long id, boolean status) {
        User user = find(id);
        user.setEnabled(status);
        repository.save(user);
    }
}
