package com.backend.services;

import java.util.List;

import com.backend.dtos.ServiceProviderRequestDTO;
import com.backend.dtos.ServiceProviderResponseDTO;

public interface ServiceProviderService {

    /**
     * Creates a new Service Provider profile.
     *
     * @param request Service Provider details.
     * @return Created Service Provider details.
     */
    ServiceProviderResponseDTO createServiceProvider(ServiceProviderRequestDTO request);

    /**
     * Retrieves a Service Provider by Provider ID.
     *
     * @param providerId Provider ID.
     * @return Service Provider details.
     */
    ServiceProviderResponseDTO getServiceProviderById(Long providerId);

    /**
     * Retrieves all Service Providers.
     *
     * @return List of all Service Providers.
     */
    List<ServiceProviderResponseDTO> getAllServiceProviders();

    /**
     * Updates an existing Service Provider profile.
     *
     * @param providerId Provider ID.
     * @param request Updated Service Provider details.
     * @return Updated Service Provider details.
     */
    ServiceProviderResponseDTO updateCurrentProvider(
            ServiceProviderRequestDTO request);



    /**
     * Deletes a Service Provider profile.
     *
     * @param providerId Provider ID.
     */
    void deleteCurrentProvider();
    
    //currentService Provider
    ServiceProviderResponseDTO getCurrentProvider();

}