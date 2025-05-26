package com.charity.charityapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationDto {
    private Long id;
    private BigDecimal amount;
    private String donorEmail;
    private LocalDateTime donatedAt;
    private Long charityActionId;
    private String charityActionName;
    private String organizationName;


    private UserDto donor;
    private CharityActionDto charityAction;
    private LocalDateTime createdAt;


    private String status;
    private Long actionId;


}