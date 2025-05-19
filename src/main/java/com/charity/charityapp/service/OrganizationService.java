package com.charity.charityapp.service;

import com.charity.charityapp.model.Organization;
import com.charity.charityapp.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public Organization getById(Long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization update(Organization organization) {
        return organizationRepository.save(organization);
    }
}
