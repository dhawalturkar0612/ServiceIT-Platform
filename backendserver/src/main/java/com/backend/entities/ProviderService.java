package com.backend.entities;

import java.math.BigDecimal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
        name = "provider_services",

        // Prevents a provider from adding the same service multiple times.
        // Example:
        // Rahul -> Electrician ✅
        // Rahul -> Electrician ❌ (Duplicate)
        // Amit  -> Electrician ✅
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_provider_service",
                        columnNames = {"provider_id", "service_id"}
                )
        }
)
@AttributeOverride(
        // Rename inherited "id" column from BaseEntity to "provider_service_id"
        name = "id",
        column = @Column(name = "provider_service_id")
)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"provider", "service"})
public class ProviderService extends BaseEntity {

    // Many ProviderService records can belong to one ServiceProvider.
    // Example:
    // Rahul ->
    //      Electrician
    //      Plumbing
    //      AC Repair
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    // Many providers can offer the same service.
    // Example:
    // Electrician ->
    //      Rahul
    //      Amit
    //      Rakesh
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceCatalog service;

    // BigDecimal is preferred over double for monetary values
    // to avoid floating-point precision issues.
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @Digits(integer = 8, fraction = 2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Estimated time required to complete this service (in minutes).
    @NotNull(message = "Estimated duration is required")
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration;

    // Provider-specific description.
    // Example:
    // "Includes installation, wiring and switchboard repair."
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    // Indicates whether the provider is currently offering this service.
    // If false, customers won't be able to book it.
    @Column(name = "is_available", nullable = false)
    private Boolean available = true;

    public ProviderService(ServiceProvider provider,
                           ServiceCatalog service,
                           BigDecimal price,
                           Integer estimatedDuration,
                           String description) {

        this.provider = provider;
        this.service = service;
        this.price = price;
        this.estimatedDuration = estimatedDuration;
        this.description = description;
        this.available = true;
    }
}