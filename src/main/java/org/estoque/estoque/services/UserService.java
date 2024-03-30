package org.estoque.estoque.services;

import org.estoque.estoque.dto.LoginRequestDTO;
import org.estoque.estoque.dto.UserRequestDTO;
import org.estoque.estoque.dto.UserResponseDTO;
import org.estoque.estoque.exception.MessagesException;
import org.estoque.estoque.exception.UserNotFoundException;
import org.estoque.estoque.models.User;
import org.estoque.estoque.models.types.Role;
import org.estoque.estoque.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
            userEntity.setEnabled(true); // enable by default
            userEntity.setRole(Role.USER); // USER Role by default
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

    public UserResponseDTO find(Long id) {
        return repository.findById(id)
                .map(user -> this.mapper.map(user, UserResponseDTO.class))
                .orElseThrow(() -> new UserNotFoundException(MessagesException.USER_NOT_FOUND));
    }

    public void changeStatus(Long id, boolean status) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(MessagesException.USER_NOT_FOUND));
        user.setEnabled(status);
        repository.save(user);
    }

    public Page<User> list(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return repository.findAll(paging);
    }

    public void remove(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(MessagesException.USER_NOT_FOUND));
        repository.delete(user);
    }
}
