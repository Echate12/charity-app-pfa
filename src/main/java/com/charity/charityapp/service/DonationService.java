package com.charity.charityapp.service;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.model.Donation;
import com.charity.charityapp.repository.CharityActionRepository;
import com.charity.charityapp.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {

    private final DonationRepository repo;
    private final CharityActionRepository actionRepo;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<DonationDto> getAll() {
        return repo.findAll().stream()
                .map(d -> mapper.map(d, DonationDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DonationDto getById(Long id) {
        Donation donation = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
        return mapper.map(donation, DonationDto.class);
    }

    @Transactional(readOnly = true)
    public List<DonationDto> getByAction(Long actionId) {
        return repo.findByCharityAction_Id(actionId).stream()
                .map(d -> mapper.map(d, DonationDto.class))
                .collect(Collectors.toList());
    }

    public DonationDto create(DonationDto dto) {
        CharityAction action = actionRepo.findById(dto.getCharityActionId())
                .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
        Donation donation = mapper.map(dto, Donation.class);
        donation.setCharityAction(action);
        if (donation.getDonatedAt() == null) {
            donation.setDonatedAt(LocalDateTime.now());
        }
        Donation saved = repo.save(donation);
        action.setCollectedAmount(action.getCollectedAmount().add(saved.getAmount()));
        actionRepo.save(action);
        return mapper.map(saved, DonationDto.class);
    }

    public DonationDto update(Long id, DonationDto dto) {
        Donation existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
        if (!existing.getCharityAction().getId().equals(dto.getCharityActionId())) {
            CharityAction action = actionRepo.findById(dto.getCharityActionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
            existing.setCharityAction(action);
        }
        existing.setAmount(dto.getAmount());
        Donation updated = repo.save(existing);
        return mapper.map(updated, DonationDto.class);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Donation not found");
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return repo.count();
    }
}

