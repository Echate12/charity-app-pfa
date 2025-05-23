package com.charity.charityapp.service;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.repository.CharityActionRepository;
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
public class CharityActionService {

    private final CharityActionRepository repo;
    private final OrganizationRepository orgRepo;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<CharityActionDto> getAll() {
        return repo.findAll().stream()
                .map(action -> mapper.map(action, CharityActionDto.class))
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
}
