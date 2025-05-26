package com.charity.charityapp.service;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.enums.ActionCategory;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.model.Organization;
import com.charity.charityapp.repository.CharityActionRepository;
import com.charity.charityapp.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing CharityAction operations for both admin and users.
 */
@Service
@RequiredArgsConstructor
public class CharityActionService {
    private static final Logger log = LoggerFactory.getLogger(CharityActionService.class);

    private final CharityActionRepository actionRepo;
    private final OrganizationRepository organizationRepo;
    private final ModelMapper modelMapper;

    // ----- Admin methods -----

    /**
     * List all charity actions with pagination (for admin).
     */
    public List<CharityActionDto> getAll(Pageable pageable) {
        log.info("Fetching all charity actions with pagination: {}", pageable);
        return actionRepo.findAll(pageable).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a charity action by ID (for admin).
     */
    public CharityActionDto getById(Long id) {
        CharityAction action = actionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CharityAction not found: " + id));
        return mapToDto(action);
    }

    /**
     * Create a new charity action (for admin).
     */
    @Transactional
    public CharityActionDto create(CharityActionDto dto) {
        CharityAction action = new CharityAction();
        action.setTitle(dto.getTitle());
        action.setDescription(dto.getDescription());
        action.setCategory(dto.getCategory());
        // link organization
        Organization org = organizationRepo.findById(dto.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization not found: " + dto.getOrganizationId()));
        action.setOrganization(org);
        action.setCollectedAmount(BigDecimal.ZERO);
        action.setCreatedAt(LocalDateTime.now());
        action.setUpdatedAt(LocalDateTime.now());

        CharityAction saved = actionRepo.save(action);
        return mapToDto(saved);
    }

    /**
     * Update an existing charity action (for admin).
     */
    @Transactional
    public CharityActionDto update(Long id, CharityActionDto dto) {
        CharityAction existing = actionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CharityAction not found: " + id));
        modelMapper.map(dto, existing);
        if (dto.getOrganizationId() != null) {
            Organization org = organizationRepo.findById(dto.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Organization not found: " + dto.getOrganizationId()));
            existing.setOrganization(org);
        }
        existing.setUpdatedAt(LocalDateTime.now());
        CharityAction updated = actionRepo.save(existing);
        return mapToDto(updated);
    }

    /**
     * Delete a charity action by ID (for admin).
     */
    public void delete(Long id) {
        if (!actionRepo.existsById(id)) {
            throw new ResourceNotFoundException("CharityAction not found: " + id);
        }
        actionRepo.deleteById(id);
    }

    /**
     * Get all action categories.
     */
    public List<ActionCategory> getAllCategories() {
        return Arrays.asList(ActionCategory.values());
    }

    // ----- User-facing methods -----

    /**
     * List all charity actions available to users.
     */
    public List<CharityActionDto> getAllForUser() {
        log.info("Fetching all charity actions for user");
        return actionRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Search charity actions by title for users.
     */
    public List<CharityActionDto> searchForUser(String keyword) {
        log.info("Searching charity actions for user with keyword: {}", keyword);
        return actionRepo.findByTitleContainingIgnoreCase(keyword).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific charity action for users.
     */
    public CharityActionDto getByIdForUser(Long id) {
        log.info("Fetching charity action for user with id: {}", id);
        return getById(id);
    }

    // ----- Mapping helper -----

    private CharityActionDto mapToDto(CharityAction action) {
        CharityActionDto dto = modelMapper.map(action, CharityActionDto.class);
        if (action.getOrganization() != null) {
            dto.setOrganizationId(action.getOrganization().getId());
            dto.setOrganizationName(action.getOrganization().getName());
        }
        return dto;
    }
}
