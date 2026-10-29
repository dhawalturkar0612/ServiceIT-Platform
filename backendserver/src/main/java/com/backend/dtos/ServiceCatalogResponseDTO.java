package com.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCatalogResponseDTO {

    private Long serviceId;

    private String serviceName;

    private String description;

}