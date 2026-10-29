package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.ProviderServiceRequestDTO;
import com.backend.dtos.ProviderServiceResponseDTO;
import com.backend.services.ProviderServiceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/provider-services")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProviderServiceController {

    private final ProviderServiceService providerServiceService;

    /**
     * Creates a new Provider Service.
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @PostMapping
    public ResponseEntity<ProviderServiceResponseDTO> createProviderService(
            @Valid @RequestBody ProviderServiceRequestDTO request) {

        ProviderServiceResponseDTO providerService =
                providerServiceService.createProviderService(request);

        return new ResponseEntity<>(providerService, HttpStatus.CREATED);
    }

    /**
     * Retrieves a Provider Service by ID.
     */
    @GetMapping("/{providerServiceId}")
    public ResponseEntity<ProviderServiceResponseDTO> getProviderServiceById(
            @PathVariable Long providerServiceId) {

        ProviderServiceResponseDTO providerService =
                providerServiceService.getProviderServiceById(providerServiceId);

        return ResponseEntity.ok(providerService);
    }

    /**
     * Retrieves all services offered by a specific provider.
     */
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ProviderServiceResponseDTO>> getProviderServicesByProvider(
            @PathVariable Long providerId) {

        List<ProviderServiceResponseDTO> providerServices =
                providerServiceService.getProviderServicesByProvider(providerId);

        return ResponseEntity.ok(providerServices);
    }

//    /**
//     * Retrieves all providers offering a specific service.
//     */
//    @GetMapping("/service/{serviceId}")
//    public ResponseEntity<List<ProviderServiceResponseDTO>> getProviderServicesByService(
//            @PathVariable Long serviceId) {
//
//        List<ProviderServiceResponseDTO> providerServices =
//                providerServiceService.getProviderServicesByService(serviceId);
//
//        return ResponseEntity.ok(providerServices);
//    }

    /**
     * Updates an existing Provider Service.
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/{providerServiceId}")
    public ResponseEntity<ProviderServiceResponseDTO> updateProviderService(
            @PathVariable Long providerServiceId,
            @Valid @RequestBody ProviderServiceRequestDTO request) {

        ProviderServiceResponseDTO providerService =
                providerServiceService.updateProviderService(providerServiceId, request);

        return ResponseEntity.ok(providerService);
    }

    /**
     * Deletes a Provider Service.
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @DeleteMapping("/{providerServiceId}")
    public ResponseEntity<String> deleteProviderService(
            @PathVariable Long providerServiceId) {

        providerServiceService.deleteProviderService(providerServiceId);

        return ResponseEntity.ok("Provider Service deleted successfully.");
    }
    
    /**
     * Searches provider services dynamically based on optional
     * service name and city, with optional price sorting.
     *
     * Example Requests:
     * GET /api/provider-services/search
     * GET /api/provider-services/search?service=Plumber
     * GET /api/provider-services/search?city=Pune
     * GET /api/provider-services/search?service=Plumber&city=Pune
     * GET /api/provider-services/search?service=Plumber&sort=priceAsc
     * GET /api/provider-services/search?service=Plumber&city=Pune&sort=priceDesc
     *
     * @param service Service name (optional)
     * @param city Provider city (optional)
     * @param sort Sorting option (priceAsc / priceDesc)
     * @return List of matching provider services
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProviderServiceResponseDTO>> searchProviderServices(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sort) {

        List<ProviderServiceResponseDTO> response =
                providerServiceService.searchProviderServices(
                        service,
                        city,
                        sort);

        return ResponseEntity.ok(response);
    }

}