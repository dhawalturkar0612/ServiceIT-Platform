package com.backend.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumerResponseDTO {

    private Long consumerId;

    private Long userId;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;
}