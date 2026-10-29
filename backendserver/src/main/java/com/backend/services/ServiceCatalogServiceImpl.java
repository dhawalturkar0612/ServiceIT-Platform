package com.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.ServiceCatalogRequestDTO;
import com.backend.dtos.ServiceCatalogResponseDTO;
import com.backend.entities.ServiceCatalog;
import com.backend.exceptions.DuplicateResourceException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.ServiceCatalogRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceCatalogRepository serviceCatalogRepository;
    private final ModelMapper mapper;

    /**
     * Creates a new Service Catalog entry.
     */
    @Override
    public ServiceCatalogResponseDTO createService(ServiceCatalogRequestDTO request) {

        if (serviceCatalogRepository.existsByServiceName(request.getServiceName())) {
            throw new DuplicateResourceException("Service already exists.");
        }

        ServiceCatalog serviceCatalog = mapper.map(request, ServiceCatalog.class);

        ServiceCatalog savedService = serviceCatalogRepository.save(serviceCatalog);

        return mapToResponse(savedService);
    }

    /**
     * Retrieves a Service Catalog entry by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ServiceCatalogResponseDTO getServiceById(Long serviceId) {

        ServiceCatalog serviceCatalog = serviceCatalogRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found."));

        return mapToResponse(serviceCatalog);
    }

    /**
     * Retrieves all Service Catalog entries.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDTO> getAllServices() {

    	return serviceCatalogRepository.findAll()
    	        .stream()
    	        .map(this::mapToResponse)
    	        .toList();
    }

    /**
     * Updates an existing Service Catalog entry.
     */
    @Override
    public ServiceCatalogResponseDTO updateService(Long serviceId, ServiceCatalogRequestDTO request) {

        ServiceCatalog serviceCatalog = serviceCatalogRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found."));

        if (!serviceCatalog.getServiceName().equals(request.getServiceName())
                && serviceCatalogRepository.existsByServiceName(request.getServiceName())) {

            throw new DuplicateResourceException("Service already exists.");
        }

        mapper.map(request, serviceCatalog);

        ServiceCatalog updatedService = serviceCatalogRepository.save(serviceCatalog);

        return mapToResponse(updatedService);
    }

    /**
     * Deletes a Service Catalog entry.
     */
    @Override
    public void deleteService(Long serviceId) {

        ServiceCatalog serviceCatalog = serviceCatalogRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found."));

        serviceCatalogRepository.delete(serviceCatalog);
    }
    
    /**
     * Maps ServiceCatalog entity to ServiceCatalogResponseDTO.
     */
    private ServiceCatalogResponseDTO mapToResponse(ServiceCatalog serviceCatalog) {

        ServiceCatalogResponseDTO response =
                mapper.map(serviceCatalog, ServiceCatalogResponseDTO.class);

        // Map primary key manually
        response.setServiceId(serviceCatalog.getId());

        return response;
    }
}