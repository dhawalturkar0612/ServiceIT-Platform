package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCatalogRequestDTO {

    @NotBlank(message = "Service name is required.")
    @Size(max = 100, message = "Service name cannot exceed 100 characters.")
    private String serviceName;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

}