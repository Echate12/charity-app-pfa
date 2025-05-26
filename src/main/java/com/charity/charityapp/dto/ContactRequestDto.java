package com.charity.charityapp.dto;

import com.charity.charityapp.enums.ContactRequestStatus;
import com.charity.charityapp.enums.ContactRequestType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactRequestDto {
    private Long id;

    // who asked
    private Long userId;
    private String userName;

    // request details
    private ContactRequestType type;
    private String title;
    private String description;
    private String contactEmail;
    private String contactPhone;
    private String address;


    private ContactRequestStatus status;
    private String adminResponse;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
