package com.backend.services;

import java.util.List;

import com.backend.dtos.BookingResponseDTO;
import com.backend.dtos.ConsumerResponseDTO;
import com.backend.dtos.PaymentResponseDTO;
import com.backend.dtos.ServiceCatalogRequestDTO;
import com.backend.dtos.ServiceCatalogResponseDTO;
import com.backend.dtos.UserResponseDTO;

public interface AdminService {

    // ==========================
    // Service Catalog Management
    // ==========================

    ServiceCatalogResponseDTO createServiceCatalog(ServiceCatalogRequestDTO request);

    ServiceCatalogResponseDTO updateServiceCatalog(Long serviceId,
                                                   ServiceCatalogRequestDTO request);

    void deleteServiceCatalog(Long serviceId);

    ServiceCatalogResponseDTO getServiceCatalogById(Long serviceId);

    List<ServiceCatalogResponseDTO> getAllServiceCatalogs();

    // ==========================
    // Service Provider Management
    // ==========================

    List<UserResponseDTO> getAllProviders();

    List<UserResponseDTO> getPendingProviders();

    UserResponseDTO approveProvider(Long userId);

    UserResponseDTO rejectProvider(Long userId);

    // ==========================
    // Consumer Management
    // ==========================

    List<ConsumerResponseDTO> getAllConsumers();

    // ==========================
    // Booking Management
    // ==========================

    List<BookingResponseDTO> getAllBookings();

    // ==========================
    // Payment Management
    // ==========================

    List<PaymentResponseDTO> getAllPayments();

}