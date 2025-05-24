// src/main/java/com/charity/charityapp/service/CharityActionService.java
package com.charity.charityapp.service;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.model.User;
import com.charity.charityapp.repository.CharityActionRepository;
import com.charity.charityapp.repository.OrganizationRepository;
import com.charity.charityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CharityActionService {

    private final CharityActionRepository repo;
    private final OrganizationRepository orgRepo;
    private final UserRepository userRepo;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<CharityActionDto> getAll() {
        return repo.findAll().stream()
                .map(a -> mapper.map(a, CharityActionDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CharityActionDto getById(Long id) {
        CharityAction action = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
        return mapper.map(action, CharityActionDto.class);
    }

    public CharityActionDto create(CharityActionDto dto) {
        CharityAction action = mapper.map(dto, CharityAction.class);
        action.setOrganization(orgRepo.findById(dto.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found")));
        CharityAction saved = repo.save(action);
        return mapper.map(saved, CharityActionDto.class);
    }

    public CharityActionDto update(Long id, CharityActionDto dto) {
        CharityAction existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
        mapper.map(dto, existing);
        existing.setOrganization(orgRepo.findById(dto.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found")));
        CharityAction updated = repo.save(existing);
        return mapper.map(updated, CharityActionDto.class);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Action not found");
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CharityActionDto> search(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword).stream()
                .map(a -> mapper.map(a, CharityActionDto.class))
                .collect(Collectors.toList());
    }

    public void follow(Long actionId, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CharityAction action = repo.findById(actionId)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
        user.getFollowedActions().add(action);
        userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public List<CharityActionDto> getTracked(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getFollowedActions().stream()
                .map(a -> mapper.map(a, CharityActionDto.class))
                .collect(Collectors.toList());
    }
}
