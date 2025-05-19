package com.charity.charityapp.dto;

import com.charity.charityapp.enums.ActionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharityActionDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private double amountRaised;
    private double fundingGoal;
    private ActionCategory category;
    private Long organizationId;

}
