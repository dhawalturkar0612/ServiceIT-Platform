package com.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bookings")
@AttributeOverride(
        name = "id",
        column = @Column(name = "booking_id")
)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"consumer", "providerService"})
public class Booking extends BaseEntity {

    // One consumer can make multiple bookings.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    // Booking is made for a specific service offered by a provider.
    // We store ProviderService instead of ServiceProvider because
    // it contains service details like price and duration.
    // load type lazy because we don't need to load all record associated with main entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_service_id", nullable = false)
    private ProviderService providerService;

    // Customer-selected service date.
    @NotNull(message = "Service date is required")
    @FutureOrPresent(message = "Service date cannot be in the past")
    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    // Customer-selected service time.
    @NotNull(message = "Service time is required")
    @Column(name = "service_time", nullable = false)
    private LocalTime serviceTime;

    // Address where the provider will perform the service.
    @NotBlank(message = "Service address is required")
    @Size(max = 255)
    @Column(name = "service_address", nullable = false, length = 255)
    private String serviceAddress;

    // Optional notes from the customer.
    // Example:
    // "Please bring a ladder."
    @Size(max = 500)
    @Column(name = "special_instructions", length = 500)
    private String specialInstructions;

    // Store the booking amount.
    // Even if the provider changes the service price later,
    // old bookings should keep the original amount.
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Current booking status.
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus = BookingStatus.PENDING_PAYMENT;

    public Booking(
            Consumer consumer,
            ProviderService providerService,
            LocalDate serviceDate,
            LocalTime serviceTime,
            String serviceAddress,
            String specialInstructions,
            BigDecimal totalAmount) {

        this.consumer = consumer;
        this.providerService = providerService;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceAddress = serviceAddress;
        this.specialInstructions = specialInstructions;
        this.totalAmount = totalAmount;

        // Every new booking starts as PENDING.
        this.bookingStatus = BookingStatus.PENDING_PAYMENT;
    }
}