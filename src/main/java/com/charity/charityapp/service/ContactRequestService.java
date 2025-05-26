package com.charity.charityapp.service;

import com.charity.charityapp.dto.ContactRequestDto;
import com.charity.charityapp.enums.ContactRequestStatus;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.ContactRequest;
import com.charity.charityapp.model.User;
import com.charity.charityapp.repository.ContactRequestRepository;
import com.charity.charityapp.repository.UserRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ContactRequestService {

    private final ContactRequestRepository repo;
    private final UserRepository userRepo;
    private final ModelMapper mapper;

    @PersistenceContext
    private EntityManager em;



    @Transactional(readOnly = true)
    public List<ContactRequestDto> getAll() {
        log.info("SERVICE getAll()");
        List<ContactRequest> entities = fetchAllWithUser();
        log.info(" → repo returned {} entities", entities.size());
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContactRequestDto> getPending() {
        log.info("SERVICE getPending()");
        List<ContactRequest> entities =
                fetchAllWithUser().stream()
                        .filter(r -> r.getStatus() == ContactRequestStatus.PENDING)
                        .toList();
        log.info(" → {} pending", entities.size());
        return entities.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ContactRequestDto> getByUserId(Long userId) {
        return repo.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }



    public ContactRequestDto create(ContactRequestDto dto, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ContactRequest entity = mapper.map(dto, ContactRequest.class);
        entity.setUser(user);
        entity.setStatus(ContactRequestStatus.PENDING);
        return toDto(repo.save(entity));
    }

    public ContactRequestDto approve(Long id, String response) {
        ContactRequest entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        entity.setStatus(ContactRequestStatus.APPROVED);
        entity.setAdminResponse(response);
        return toDto(repo.save(entity));
    }

    public ContactRequestDto reject(Long id, String response) {
        ContactRequest entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        entity.setStatus(ContactRequestStatus.REJECTED);
        entity.setAdminResponse(response);
        return toDto(repo.save(entity));
    }



    private ContactRequestDto toDto(ContactRequest r) {
        ContactRequestDto dto = mapper.map(r, ContactRequestDto.class);
        dto.setUserId(r.getUser() != null ? r.getUser().getId() : null);
        if (r.getUser() != null)
            dto.setUserName(r.getUser().getFirstName() + " " + r.getUser().getLastName());
        return dto;
    }

    /**
     * Fetch everything with the associated User in a single query
     * to avoid LazyInitializationException outside the txn.
     */
    @SuppressWarnings("unchecked")
    private List<ContactRequest> fetchAllWithUser() {
        EntityGraph<ContactRequest> graph =
                (EntityGraph<ContactRequest>) em.getEntityGraph("ContactRequest.withUser");
        return em.createQuery("select c from ContactRequest c", ContactRequest.class)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .getResultList();
    }



    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }

    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return repo.countByStatus(ContactRequestStatus.valueOf(status));
    }
}
