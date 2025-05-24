package com.charity.charityapp.dto;

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
    private String status;
}
