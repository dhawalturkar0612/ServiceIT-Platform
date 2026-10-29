package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.ServiceCatalogRequestDTO;
import com.backend.dtos.ServiceCatalogResponseDTO;
import com.backend.services.ServiceCatalogService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/services")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    /**
     * Create a new Service.
     */
    @PostMapping
    public ResponseEntity<ServiceCatalogResponseDTO> createService(
            @Valid @RequestBody ServiceCatalogRequestDTO request) {

        ServiceCatalogResponseDTO service =
                serviceCatalogService.createService(request);

        return new ResponseEntity<>(service, HttpStatus.CREATED);
    }

    /**
     * Get Service by ID.
     */
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceCatalogResponseDTO> getServiceById(
            @PathVariable Long serviceId) {

        ServiceCatalogResponseDTO service =
                serviceCatalogService.getServiceById(serviceId);

        return ResponseEntity.ok(service);
    }

    /**
     * Get all Services.
     */
    @GetMapping
    public ResponseEntity<List<ServiceCatalogResponseDTO>> getAllServices() {

        List<ServiceCatalogResponseDTO> services =
                serviceCatalogService.getAllServices();

        return ResponseEntity.ok(services);
    }

    /**
     * Update Service.
     */
    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceCatalogResponseDTO> updateService(
            @PathVariable Long serviceId,
            @Valid @RequestBody ServiceCatalogRequestDTO request) {

        ServiceCatalogResponseDTO service =
                serviceCatalogService.updateService(serviceId, request);

        return ResponseEntity.ok(service);
    }

    /**
     * Delete Service.
     */
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<String> deleteService(
            @PathVariable Long serviceId) {

        serviceCatalogService.deleteService(serviceId);

        return ResponseEntity.ok("Service deleted successfully.");
    }

}