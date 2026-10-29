package com.backend.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "Provider Service ID is required")
    private Long providerServiceId;

    @NotNull(message = "Service date is required")
    @FutureOrPresent(message = "Service date cannot be in the past")
    private LocalDate serviceDate;

    @NotNull(message = "Service time is required")
    private LocalTime serviceTime;

    @NotBlank(message = "Service address is required")
    @Size(max = 255, message = "Service address cannot exceed 255 characters")
    private String serviceAddress;

    @Size(max = 500, message = "Special instructions cannot exceed 500 characters")
    private String specialInstructions;

}