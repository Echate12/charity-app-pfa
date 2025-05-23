package com.charity.charityapp.service;

import com.charity.charityapp.dto.OrganizationDto;
import com.charity.charityapp.enums.OrganizationStatus;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.Organization;
import com.charity.charityapp.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {

    private final OrganizationRepository repo;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<OrganizationDto> getAll() {
        return repo.findAll().stream()
                .map(o -> mapper.map(o, OrganizationDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrganizationDto getById(Long id) {
        Organization org = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        return mapper.map(org, OrganizationDto.class);
    }

    public OrganizationDto create(OrganizationDto dto) {
        Organization org = mapper.map(dto, Organization.class);
        org.setStatus(OrganizationStatus.PENDING);
        Organization saved = repo.save(org);
        return mapper.map(saved, OrganizationDto.class);
    }

    public OrganizationDto update(Long id, OrganizationDto dto) {
        Organization existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        mapper.map(dto, existing);
        Organization saved = repo.save(existing);
        return mapper.map(saved, OrganizationDto.class);
    }

    public OrganizationDto approve(Long id) {
        Organization org = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        org.setStatus(OrganizationStatus.APPROVED);
        return mapper.map(repo.save(org), OrganizationDto.class);
    }

    public OrganizationDto reject(Long id) {
        Organization org = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        org.setStatus(OrganizationStatus.REJECTED);
        return mapper.map(repo.save(org), OrganizationDto.class);
    }

    public OrganizationDto suspend(Long id) {
        Organization org = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        org.setStatus(OrganizationStatus.SUSPENDED);
        return mapper.map(repo.save(org), OrganizationDto.class);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Organization not found");
        }
        repo.deleteById(id);
    }
}

