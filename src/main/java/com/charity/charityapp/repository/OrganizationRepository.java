package com.charity.charityapp.repository;

import com.charity.charityapp.enums.OrganizationStatus;
import com.charity.charityapp.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByStatus(OrganizationStatus status);
}