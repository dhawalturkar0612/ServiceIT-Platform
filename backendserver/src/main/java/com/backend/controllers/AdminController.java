package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.BookingResponseDTO;
import com.backend.dtos.ConsumerResponseDTO;
import com.backend.dtos.PaymentResponseDTO;
import com.backend.dtos.ServiceCatalogRequestDTO;
import com.backend.dtos.ServiceCatalogResponseDTO;
import com.backend.dtos.UserResponseDTO;
import com.backend.services.AdminService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ==========================
    // Service Catalog Management
    // ==========================

    /**
     * Creates a new service.
     */
    @PostMapping("/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceCatalogResponseDTO createService(
            @Valid @RequestBody ServiceCatalogRequestDTO request) {

        return adminService.createServiceCatalog(request);
    }

    /**
     * Updates an existing service.
     */
    @PutMapping("/services/{serviceId}")
    public ServiceCatalogResponseDTO updateService(
            @PathVariable Long serviceId,
            @Valid @RequestBody ServiceCatalogRequestDTO request) {

        return adminService.updateServiceCatalog(serviceId, request);
    }

    /**
     * Deletes a service.
     */
    @DeleteMapping("/services/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable Long serviceId) {

        adminService.deleteServiceCatalog(serviceId);
    }

    /**
     * Retrieves a service by ID.
     */
    @GetMapping("/services/{serviceId}")
    public ServiceCatalogResponseDTO getServiceById(
            @PathVariable Long serviceId) {

        return adminService.getServiceCatalogById(serviceId);
    }

    /**
     * Retrieves all services.
     */
    @GetMapping("/services")
    public List<ServiceCatalogResponseDTO> getAllServices() {

        return adminService.getAllServiceCatalogs();
    }

    // ==========================
    // Service Provider Management
    // ==========================

    /**
     * Retrieves all service providers.
     */
    @GetMapping("/providers")
    public ResponseEntity<List<UserResponseDTO>> getAllProviders() {
        return ResponseEntity.ok(adminService.getAllProviders());
    }

    /**
     * Retrieves all pending providers.
     */
    @GetMapping("/providers/pending")
    public ResponseEntity<List<UserResponseDTO>> getPendingProviders() {
        return ResponseEntity.ok(adminService.getPendingProviders());
    }

    /**
     * Approves a service provider.
     */
    @PutMapping("/providers/{userId}/approve")
    public ResponseEntity<UserResponseDTO> approveProvider(
            @PathVariable Long userId) {

        return ResponseEntity.ok(adminService.approveProvider(userId));
    }

    /**
     * Rejects a service provider.
     */
    @PutMapping("/providers/{userId}/reject")
    public ResponseEntity<UserResponseDTO> rejectProvider(
            @PathVariable Long userId) {

        return ResponseEntity.ok(adminService.rejectProvider(userId));
    }

    // ==========================
    // Consumer Management
    // ==========================

    /**
     * Retrieves all consumers.
     */
    @GetMapping("/consumers")
    public List<ConsumerResponseDTO> getAllConsumers() {

        return adminService.getAllConsumers();
    }

    // ==========================
    // Booking Management
    // ==========================

    /**
     * Retrieves all bookings.
     */
    @GetMapping("/bookings")
    public List<BookingResponseDTO> getAllBookings() {

        return adminService.getAllBookings();
    }

    // ==========================
    // Payment Management
    // ==========================

    /**
     * Retrieves all payments.
     */
    @GetMapping("/payments")
    public List<PaymentResponseDTO> getAllPayments() {

        return adminService.getAllPayments();
    }

}