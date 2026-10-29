package com.backend.services;

import java.util.List;

import com.backend.dtos.ProviderServiceRequestDTO;
import com.backend.dtos.ProviderServiceResponseDTO;

public interface ProviderServiceService {

    /**
     * Creates a new Provider Service.
     *
     * @param request Provider Service details.
     * @return Created Provider Service details.
     */
    ProviderServiceResponseDTO createProviderService(ProviderServiceRequestDTO request);

    /**
     * Retrieves a Provider Service by its ID.
     *
     * @param providerServiceId Provider Service ID.
     * @return Provider Service details.
     */
    ProviderServiceResponseDTO getProviderServiceById(Long providerServiceId);

    /**
     * Retrieves all services offered by a specific provider.
     *
     * @param providerId Service Provider ID.
     * @return List of services offered by the provider.
     */
    List<ProviderServiceResponseDTO> getProviderServicesByProvider(Long providerId);

    /**
     * Retrieves all providers offering a specific service.
     *
     * @param serviceId Service Catalog ID.
     * @return List of providers offering the specified service.
     */
    List<ProviderServiceResponseDTO> getProviderServicesByService(Long serviceId);

    /**
     * Updates an existing Provider Service.
     *
     * @param providerServiceId Provider Service ID.
     * @param request Updated Provider Service details.
     * @return Updated Provider Service details.
     */
    ProviderServiceResponseDTO updateProviderService(Long providerServiceId,
                                                     ProviderServiceRequestDTO request);

    /**
     * Deletes a Provider Service.
     *
     * @param providerServiceId Provider Service ID.
     */
    void deleteProviderService(Long providerServiceId);
    
    /**
     * Searches provider services dynamically based on optional
     * service name and city, and sorts the results by price.
     *
     * Search Examples:
     * - service = Plumber
     * - city = Pune
     * - service = Plumber & city = Pune
     *
     * Sort Options:
     * - priceAsc
     * - priceDesc
     *
     * @param serviceName Name of the service (optional)
     * @param city Name of the city (optional)
     * @param sort Sorting option (optional)
     * @return List of matching provider services
     */
    List<ProviderServiceResponseDTO> searchProviderServices(
            String serviceName,
            String city,
            String sort);

}