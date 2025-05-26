package com.charity.charityapp.dto;

import com.charity.charityapp.enums.OrganizationStatus;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {
    private Long id;
    private String name;
    private String contactEmail;
    private String address;
    private OrganizationStatus status;
}
