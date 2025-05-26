package com.charity.charityapp.service;

import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.model.User;
import com.charity.charityapp.enums.Role;
import com.charity.charityapp.repository.UserRepository;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    @Transactional
    public UserDto create(UserDto dto) {
        log.debug("Creating new user with email: {}", dto.getEmail());
        
        // Validate email and username uniqueness
        if (userRepo.existsByEmail(dto.getEmail())) {
            log.warn("Email already exists: {}", dto.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepo.existsByUsername(dto.getUsername())) {
            log.warn("Username already exists: {}", dto.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }

        // Create user entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(Role.USER);

        log.debug("Saving new user: {}", user.getEmail());
        User saved = userRepo.save(user);
        log.info("Created new user with ID: {}", saved.getId());
        
        return mapper.map(saved, UserDto.class);
    }

    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapper.map(user, UserDto.class);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepo.findAll().stream()
                .map(u -> mapper.map(u, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserDto update(Long id, UserDto dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Update basic fields
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        
        // Update password only if provided
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }
        
        User saved = userRepo.save(user);
        return mapper.map(saved, UserDto.class);
    }

    public UserDto updateRole(Long id, String newRole) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(Role.valueOf(newRole.toUpperCase()));
        User saved = userRepo.save(user);
        return mapper.map(saved, UserDto.class);
    }

    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public long count() {
        return userRepo.count();
    }
}

