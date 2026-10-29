package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.ServiceProviderRequestDTO;
import com.backend.dtos.ServiceProviderResponseDTO;
import com.backend.services.ServiceProviderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/service-providers")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;
     
    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/me")
    public ResponseEntity<ServiceProviderResponseDTO> getCurrentProvider() {

        return ResponseEntity.ok(
                serviceProviderService.getCurrentProvider());
    }

    /**
     * Create a new Service Provider profile.
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @PostMapping
    public ResponseEntity<ServiceProviderResponseDTO> createServiceProvider(
            @Valid @RequestBody ServiceProviderRequestDTO request) {

        ServiceProviderResponseDTO provider =
                serviceProviderService.createServiceProvider(request);

        return new ResponseEntity<>(provider, HttpStatus.CREATED);
    }

    /**
     * Get Service Provider by ID.
     */
    @GetMapping("/{providerId}")
    public ResponseEntity<ServiceProviderResponseDTO> getServiceProviderById(
            @PathVariable Long providerId) {

        ServiceProviderResponseDTO provider =
                serviceProviderService.getServiceProviderById(providerId);

        return ResponseEntity.ok(provider);
    }

    /**
     * Get all Service Providers.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ServiceProviderResponseDTO>> getAllServiceProviders() {

        List<ServiceProviderResponseDTO> providers =
                serviceProviderService.getAllServiceProviders();

        return ResponseEntity.ok(providers);
    }

    /**
     * Update Service Provider profile.
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/me")
    public ResponseEntity<ServiceProviderResponseDTO> updateCurrentProvider(
            @Valid @RequestBody ServiceProviderRequestDTO request) {

        ServiceProviderResponseDTO provider =
                serviceProviderService.updateCurrentProvider(request);

        return ResponseEntity.ok(provider);
    }

    
    /**
     * Delete Service Provider profile.
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentProvider() {

        serviceProviderService.deleteCurrentProvider();

        return ResponseEntity.ok("Service Provider profile deleted successfully.");
    }

}