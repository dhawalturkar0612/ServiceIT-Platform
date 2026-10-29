package com.backend.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderServiceRequestDTO {

    /**
     * ID of the Service from the Service Catalog.
     */
    @NotNull(message = "Service ID is required.")
    private Long serviceId;

    /**
     * Price charged by the provider for this service.
     */
    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format.")
    private BigDecimal price;

    /**
     * Estimated time (in minutes) required to complete the service.
     */
    @NotNull(message = "Estimated duration is required.")
    @Min(value = 1, message = "Estimated duration must be at least 1 minute.")
    private Integer estimatedDuration;

    /**
     * Provider-specific description.
     * Example:
     * "Includes installation, wiring, and switchboard repair."
     */
    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    /**
     * Indicates whether the provider is currently offering this service.
     */
    @NotNull(message = "Availability status is required.")
    private Boolean available;

}