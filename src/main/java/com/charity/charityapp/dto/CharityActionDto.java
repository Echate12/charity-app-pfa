package com.charity.charityapp.dto;

import com.charity.charityapp.enums.ActionCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharityActionDto {
    private Long id;
    private String title;
    private String description;
    private ActionCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal collectedAmount;


    private Long organizationId;
    private String organizationName;
}
