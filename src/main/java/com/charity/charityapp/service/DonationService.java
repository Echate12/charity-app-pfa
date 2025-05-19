package com.charity.charityapp.service;

import com.charity.charityapp.model.Donation;
import com.charity.charityapp.repository.DonationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DonationService {

    private final DonationRepository donationRepository;

    public List<Donation> getAll() {
        return donationRepository.findAll();
    }

    public Donation getById(Long id) {
        return donationRepository.findById(id).orElse(null);
    }

    public Donation save(Donation Donation) {
        return donationRepository.save(Donation);
    }

    public Donation update(Donation Donation) {
        return donationRepository.save(Donation);
    }
}
