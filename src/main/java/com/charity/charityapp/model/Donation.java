package com.charity.charityapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Donation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String donorEmail;
    private LocalDateTime donatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_action_id")
    private CharityAction charityAction;


}
