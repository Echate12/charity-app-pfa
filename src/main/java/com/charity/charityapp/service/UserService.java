package com.charity.charityapp.service;

import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.enums.Role;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.User;
import com.charity.charityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    public UserDto create(UserDto dto) {
        User entity = mapper.map(dto, User.class);
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setRole(Role.USER);
        User saved = userRepo.save(entity);
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
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
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
}

