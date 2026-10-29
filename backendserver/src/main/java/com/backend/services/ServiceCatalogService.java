package com.backend.services;

import java.util.List;

import com.backend.dtos.ServiceCatalogRequestDTO;
import com.backend.dtos.ServiceCatalogResponseDTO;

public interface ServiceCatalogService {

    /**
     * Creates a new Service.
     *
     * @param request Service details.
     * @return Created Service details.
     */
    ServiceCatalogResponseDTO createService(ServiceCatalogRequestDTO request);

    /**
     * Retrieves a Service by ID.
     *
     * @param serviceId Service ID.
     * @return Service details.
     */
    ServiceCatalogResponseDTO getServiceById(Long serviceId);

    /**
     * Retrieves all Services.
     *
     * @return List of all Services.
     */
    List<ServiceCatalogResponseDTO> getAllServices();

    /**
     * Updates an existing Service.
     *
     * @param serviceId Service ID.
     * @param request Updated Service details.
     * @return Updated Service details.
     */
    ServiceCatalogResponseDTO updateService(Long serviceId, ServiceCatalogRequestDTO request);

    /**
     * Deletes a Service.
     *
     * @param serviceId Service ID.
     */
    void deleteService(Long serviceId);

}