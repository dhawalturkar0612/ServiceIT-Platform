package com.backend.dtos;

import com.backend.entities.UserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceProviderResponseDTO {

    // ServiceProvider Details
    private Long providerId;
    private Long userId;

    // User Details
    private String fullName;
    private String email;
    private String phone;

    // Business Details
    private String businessName;
    private String description;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private Integer experienceYears;
    
    private UserStatus status;

}