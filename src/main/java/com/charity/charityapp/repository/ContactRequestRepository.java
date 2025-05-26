package com.charity.charityapp.repository;

import com.charity.charityapp.model.ContactRequest;
import com.charity.charityapp.enums.ContactRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {

    @EntityGraph(value = "ContactRequest.withUser")          // ← add
    List<ContactRequest> findAll();

    @EntityGraph(value = "ContactRequest.withUser")          // ← add
    List<ContactRequest> findByStatus(ContactRequestStatus status);

    @EntityGraph(value = "ContactRequest.withUser")          // ← add
    List<ContactRequest> findByUserId(Long userId);

    long countByStatus(ContactRequestStatus status);
}
