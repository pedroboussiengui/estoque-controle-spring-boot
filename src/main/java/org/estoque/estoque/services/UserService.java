package org.estoque.estoque.services;

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
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    public Page<User> list(int page, int size, String sort) {
        String[] sortingFields = sort.split(" ");

        if (sortingFields.length != 2) {
            throw new InvalidParameterException(MessagesException.INVALID_PARAMETERS);
        }

        String field = sortingFields[0];
        String dir = sortingFields[1];

        Sort.Direction direction;
        if (Objects.equals(dir, "DESC")) {
            direction = Sort.Direction.DESC;
        } else if (Objects.equals(dir, "ASC")) {
            direction = Sort.Direction.ASC;
        } else {
            throw new InvalidParameterException(MessagesException.INVALID_PARAMETERS);
        }

        Sort sorting = Sort.by(direction, field);

        Pageable paging = PageRequest.of(page, size, sorting);
        return repository.findAll(paging);
    }

    public void remove(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(MessagesException.USER_NOT_FOUND));
        repository.delete(user);
    }
}
