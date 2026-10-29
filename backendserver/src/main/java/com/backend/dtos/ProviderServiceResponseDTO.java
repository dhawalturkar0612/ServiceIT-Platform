package com.backend.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderServiceResponseDTO {

    /**
     * Unique ID of the ProviderService record.
     */
    private Long providerServiceId;

    /**
     * Service Provider details.
     */
    private Long providerId;
    private String providerName;

    /**
     * Service Catalog details.
     */
    private Long serviceId;
    private String serviceName;

    /**
     * Price charged by the provider.
     */
    private BigDecimal price;

    /**
     * Estimated time (in minutes) required to complete the service.
     */
    private Integer estimatedDuration;

    /**
     * Provider-specific description.
     */
    private String description;

    /**
     * Indicates whether the service is currently available.
     */
    private Boolean available;

}