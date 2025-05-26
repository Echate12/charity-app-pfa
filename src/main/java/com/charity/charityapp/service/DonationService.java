package com.charity.charityapp.service;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.model.Donation;
import com.charity.charityapp.repository.CharityActionRepository;
import com.charity.charityapp.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {

    private final DonationRepository donationRepo;
    private final CharityActionRepository actionRepo;
    private final ModelMapper mapper;

    /**
     * List all donations without pagination.
     */
    @Transactional(readOnly = true)
    public List<DonationDto> getAll() {
        return donationRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * List donations with pagination.
     */
    @Transactional(readOnly = true)
    public Page<DonationDto> getAll(Pageable pageable) {
        return donationRepo.findAll(pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public DonationDto getById(Long id) {
        Donation donation = donationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
        return mapToDto(donation);
    }

    @Transactional(readOnly = true)
    public List<DonationDto> getByAction(Long actionId) {
        return donationRepo.findByCharityAction_Id(actionId).stream()
                .map(this::mapToDto)
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
        Donation saved = donationRepo.save(donation);
        action.setCollectedAmount(action.getCollectedAmount().add(saved.getAmount()));
        actionRepo.save(action);
        return mapToDto(saved);
    }

    public DonationDto update(Long id, DonationDto dto) {
        Donation existing = donationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
        if (!existing.getCharityAction().getId().equals(dto.getCharityActionId())) {
            CharityAction action = actionRepo.findById(dto.getCharityActionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
            existing.setCharityAction(action);
        }
        existing.setAmount(dto.getAmount());
        Donation updated = donationRepo.save(existing);
        return mapToDto(updated);
    }

    public void delete(Long id) {
        if (!donationRepo.existsById(id)) {
            throw new ResourceNotFoundException("Donation not found");
        }
        donationRepo.deleteById(id);
    }

    public long countAll() {
        return donationRepo.count();
    }

    private DonationDto mapToDto(Donation donation) {
        DonationDto dto = mapper.map(donation, DonationDto.class);
        dto.setDonorEmail(donation.getDonorEmail());
        dto.setDonatedAt(donation.getDonatedAt());
        dto.setCharityActionId(donation.getCharityAction().getId());
        dto.setCharityActionName(donation.getCharityAction().getTitle());
        dto.setOrganizationName(donation.getCharityAction().getOrganization().getName());
        return dto;
    }
}
