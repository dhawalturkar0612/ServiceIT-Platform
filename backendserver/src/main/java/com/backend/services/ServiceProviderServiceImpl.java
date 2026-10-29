package com.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.entities.UserStatus;
import com.backend.dtos.ServiceProviderRequestDTO;
import com.backend.dtos.ServiceProviderResponseDTO;
import com.backend.entities.ServiceProvider;
import com.backend.entities.User;
import com.backend.entities.UserRole;
import com.backend.exceptions.DuplicateResourceException;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.ServiceProviderRepository;
import com.backend.repositories.UserRepository;
import com.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AuditLogService auditLogService;

    @Override
    public ServiceProviderResponseDTO createServiceProvider(ServiceProviderRequestDTO request) {

    	Long userId = SecurityUtils.getCurrentUserId();

    	User user = userRepository.findById(userId)
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException("User not found."));

        if (user.getRole() != UserRole.PROVIDER) {
            throw new InvalidOperationException(
                    "Selected user is not registered as a Service Provider.");
        }
        
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Your account is awaiting admin approval.");
        }

        if (serviceProviderRepository.existsByUserId(user.getId())) {
            throw new DuplicateResourceException(
                    "Service Provider profile already exists.");
        }

        ServiceProvider serviceProvider = mapper.map(request, ServiceProvider.class);

        serviceProvider.setUser(user);


        ServiceProvider savedProvider =
                serviceProviderRepository.save(serviceProvider);

        auditLogService.log(
                user.getEmail(),
                "PROVIDER",
                "PROFILE_CREATED",
                "Provider " + savedProvider.getBusinessName() + " (Owner: " + user.getFullName() + ") created business profile."
        );

        return mapToResponse(savedProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceProviderResponseDTO getServiceProviderById(Long providerId) {

        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service Provider not found with ID : " + providerId));

        return mapToResponse(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceProviderResponseDTO> getAllServiceProviders() {

        return serviceProviderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ServiceProviderResponseDTO updateCurrentProvider(
            ServiceProviderRequestDTO request) {

        ServiceProvider provider = serviceProviderRepository
                .findByUserId(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Provider profile not found."));

        provider.setBusinessName(request.getBusinessName());
        provider.setDescription(request.getDescription());
        provider.setAddress(request.getAddress());
        provider.setCity(request.getCity());
        provider.setState(request.getState());
        provider.setPincode(request.getPincode());
        provider.setExperienceYears(request.getExperienceYears());

        ServiceProvider updatedProvider =
                serviceProviderRepository.save(provider);

        auditLogService.log(
                provider.getUser().getEmail(),
                "PROVIDER",
                "PROFILE_UPDATED",
                "Provider " + updatedProvider.getBusinessName() + " updated business profile in " + updatedProvider.getCity() + "."
        );

        return mapToResponse(updatedProvider);
    }

    @Override
    public void deleteCurrentProvider() {

        ServiceProvider provider = serviceProviderRepository
                .findByUserId(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Provider profile not found."));

        serviceProviderRepository.delete(provider);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ServiceProviderResponseDTO getCurrentProvider() {

        ServiceProvider provider =
                serviceProviderRepository
                        .findByUserId(SecurityUtils.getCurrentUserId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Provider profile not found."));

        return mapToResponse(provider);
    }

    /**
     * Maps ServiceProvider entity to ServiceProviderResponseDTO.
     */
    private ServiceProviderResponseDTO mapToResponse(ServiceProvider provider) {

        ServiceProviderResponseDTO response =
                mapper.map(provider, ServiceProviderResponseDTO.class);

        // Primary Key
        response.setProviderId(provider.getId());

        // User Details
        response.setUserId(provider.getUser().getId());
        response.setFullName(provider.getUser().getFullName());
        response.setEmail(provider.getUser().getEmail());
        response.setPhone(provider.getUser().getPhone());
        response.setStatus(provider.getUser().getStatus());

        return response;
    }

}