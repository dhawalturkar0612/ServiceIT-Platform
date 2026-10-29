package com.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.backend.entities.BookingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDTO {

    // Booking Details
    private Long bookingId;

    // Consumer Details
    private Long consumerId;
    private String consumerName;

    // Provider Details
    private Long providerId;
    private String providerName;

    // Service Details
    private Long providerServiceId;
    private Long serviceId;
    private String serviceName;

    // Booking Details
    private LocalDate serviceDate;
    private LocalTime serviceTime;
    private String serviceAddress;
    private String specialInstructions;

    // Payment Details
    private BigDecimal totalAmount;

    // Booking Status
    private BookingStatus bookingStatus;

}