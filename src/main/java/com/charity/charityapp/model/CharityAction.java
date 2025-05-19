package com.charity.charityapp.model;

import com.charity.charityapp.enums.ActionCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "charity_actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharityAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private double fundingGoal;
    private double amountRaised;

    @Enumerated(EnumType.STRING)
    private ActionCategory category;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "charityAction", cascade = CascadeType.ALL)
    private List<Donation> donations = new ArrayList<>();

    // Getters and setters
}
