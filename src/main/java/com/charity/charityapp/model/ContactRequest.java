package com.charity.charityapp.model;

import com.charity.charityapp.enums.ContactRequestStatus;
import com.charity.charityapp.enums.ContactRequestType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "ContactRequest.withUser",
        attributeNodes = @NamedAttributeNode("user")
)
@Getter @Setter @NoArgsConstructor
public class ContactRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ContactRequestType type;

    private String title;
    private String description;
    private String contactEmail;
    private String contactPhone;
    private String address;

    @Enumerated(EnumType.STRING)
    private ContactRequestStatus status = ContactRequestStatus.PENDING;

    private String adminResponse;

    @CreatedDate  private LocalDateTime createdAt;
    @LastModifiedDate private LocalDateTime updatedAt;
}
