package com.charity.charityapp.model;


import com.charity.charityapp.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String legalAddress;
    private String taxId;
    private String contactEmail;
    private String contactPhone;
    private String description;
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<CharityAction> actions = new ArrayList<>();


}

