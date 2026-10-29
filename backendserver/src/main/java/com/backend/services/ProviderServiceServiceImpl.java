package com.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.ProviderServiceRequestDTO;
import com.backend.dtos.ProviderServiceResponseDTO;
import com.backend.entities.ProviderService;
import com.backend.entities.ServiceCatalog;
import com.backend.entities.ServiceProvider;
import com.backend.entities.UserStatus;
import com.backend.exceptions.DuplicateResourceException;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.ProviderServiceRepository;
import com.backend.repositories.ServiceCatalogRepository;
import com.backend.repositories.ServiceProviderRepository;
import com.backend.security.SecurityUtils;
import com.backend.specification.ProviderServiceSpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProviderServiceServiceImpl implements ProviderServiceService {

    private final ProviderServiceRepository providerServiceRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;
    private final ModelMapper mapper;

    /**
     * Creates a new Provider Service.
     */
    @Override
    public ProviderServiceResponseDTO createProviderService(ProviderServiceRequestDTO request) {

        // Validate Service Provider
    	ServiceProvider provider =
    	        serviceProviderRepository
    	                .findByUserId(SecurityUtils.getCurrentUserId())
    	                .orElseThrow(() ->
    	                        new ResourceNotFoundException(
    	                                "Provider profile not found."));

        // Only ACTIVE providers can offer services
        if (provider.getUser().getStatus() != UserStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Provider account has not been approved by the admin.");
        }

        // Validate Service Catalog
        ServiceCatalog serviceCatalog = serviceCatalogRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found."));

        // Prevent duplicate provider-service combinations
        if (providerServiceRepository.existsByProviderIdAndServiceId(
        		provider.getId(), request.getServiceId())) {

            throw new DuplicateResourceException("This service is already added by the provider.");
        }

        // Map DTO to Entity
        ProviderService providerService = mapper.map(request, ProviderService.class);

        providerService.setProvider(provider);
        providerService.setService(serviceCatalog);

        ProviderService savedProviderService = providerServiceRepository.save(providerService);

        return mapToResponse(savedProviderService);
    }

    /**
     * Retrieves a Provider Service by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ProviderServiceResponseDTO getProviderServiceById(Long providerServiceId) {

        ProviderService providerService = providerServiceRepository.findById(providerServiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider Service not found."));

        return mapToResponse(providerService);
    }

    /**
     * Retrieves all services offered by a specific provider.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProviderServiceResponseDTO> getProviderServicesByProvider(Long providerId) {

        return providerServiceRepository.findByProviderId(providerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Retrieves all providers offering a specific service.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProviderServiceResponseDTO> getProviderServicesByService(Long serviceId) {

        return providerServiceRepository.findByServiceId(serviceId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Updates an existing Provider Service.
     */
    @Override
    public ProviderServiceResponseDTO updateProviderService(
            Long providerServiceId,
            ProviderServiceRequestDTO request) {

        // Fetch Provider Service
        ProviderService providerService = providerServiceRepository.findById(providerServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider Service not found."));

        // Verify ownership
        if (!providerService.getProvider().getUser().getId()
                .equals(SecurityUtils.getCurrentUserId())) {

            throw new AccessDeniedException(
                    "You are not allowed to modify this service.");
        }

        // Update fields
        providerService.setPrice(request.getPrice());
        providerService.setEstimatedDuration(request.getEstimatedDuration());
        providerService.setDescription(request.getDescription());
        providerService.setAvailable(request.getAvailable());

        ProviderService updatedProviderService =
                providerServiceRepository.save(providerService);

        return mapToResponse(updatedProviderService);
    }

    /**
     * Deletes a Provider Service.
     */
    @Override
    public void deleteProviderService(Long providerServiceId) {

        ProviderService providerService = providerServiceRepository.findById(providerServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider Service not found."));

        // Verify ownership
        if (!providerService.getProvider().getUser().getId()
                .equals(SecurityUtils.getCurrentUserId())) {

            throw new AccessDeniedException(
                    "You are not allowed to delete this service.");
        }

        providerServiceRepository.delete(providerService);
    }

    /**
     * Maps ProviderService entity to ProviderServiceResponseDTO.
     *
     * @param providerService ProviderService entity
     * @return mapped ProviderServiceResponseDTO
     */
    private ProviderServiceResponseDTO mapToResponse(ProviderService providerService) {

        ProviderServiceResponseDTO response =
                mapper.map(providerService, ProviderServiceResponseDTO.class);

        // Primary Key
        response.setProviderServiceId(providerService.getId());

        // Provider Details
        response.setProviderId(providerService.getProvider().getId());
        response.setProviderName(providerService.getProvider().getBusinessName());

        // Service Details
        response.setServiceId(providerService.getService().getId());
        response.setServiceName(providerService.getService().getServiceName());

        return response;
    }
    
    /**
     * Searches provider services dynamically based on service name,
     * city and optional sorting.
     *
     * @param serviceName Name of the service (optional)
     * @param city Provider city (optional)
     * @param sort priceAsc or priceDesc
     * @return List of matching provider services
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProviderServiceResponseDTO> searchProviderServices(
            String serviceName,
            String city,
            String sort) {

        // Build dynamic search conditions
        Specification<ProviderService> specification =
                ProviderServiceSpecification.searchProviderServices(
                        serviceName,
                        city);

        // Determine sorting order
        Sort sorting = Sort.unsorted();

        if (sort != null && !sort.isBlank()) {

            switch (sort.toLowerCase()) {

                case "priceasc":
                    sorting = Sort.by(Sort.Direction.ASC, "price");
                    break;

                case "pricedesc":
                    sorting = Sort.by(Sort.Direction.DESC, "price");
                    break;

                default:
                    sorting = Sort.unsorted();
            }
        }

        // Fetch matching provider services
        List<ProviderService> providerServices =
                providerServiceRepository.findAll(specification, sorting);

        // Convert entities to response DTOs
        return providerServices.stream()
                .map(this::mapToResponse)
                .toList();
    }

}