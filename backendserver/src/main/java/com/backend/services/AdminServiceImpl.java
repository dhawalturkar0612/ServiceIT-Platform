package com.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.BookingResponseDTO;
import com.backend.dtos.ConsumerResponseDTO;
import com.backend.dtos.PaymentResponseDTO;
import com.backend.dtos.ServiceCatalogRequestDTO;
import com.backend.dtos.ServiceCatalogResponseDTO;
import com.backend.dtos.UserResponseDTO;
import com.backend.entities.User;
import com.backend.entities.UserRole;
import com.backend.entities.UserStatus;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final ServiceCatalogService serviceCatalogService;
    private final ConsumerService consumerService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AuditLogService auditLogService;
    private final EmailService emailService;

    // ==========================
    // Service Catalog Management
    // ==========================

    @Override
    public ServiceCatalogResponseDTO createServiceCatalog(ServiceCatalogRequestDTO request) {
        return serviceCatalogService.createService(request);
    }

    @Override
    public ServiceCatalogResponseDTO updateServiceCatalog(Long serviceId,
            ServiceCatalogRequestDTO request) {
        return serviceCatalogService.updateService(serviceId, request);
    }

    @Override
    public void deleteServiceCatalog(Long serviceId) {
        serviceCatalogService.deleteService(serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceCatalogResponseDTO getServiceCatalogById(Long serviceId) {
        return serviceCatalogService.getServiceById(serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDTO> getAllServiceCatalogs() {
        return serviceCatalogService.getAllServices();
    }

	 // ==========================
	 // Service Provider Management
	 // ==========================
	
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllProviders() {

        return userRepository.findByRole(UserRole.PROVIDER)
                .stream()
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .toList();
    }
	
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getPendingProviders() {

        return userRepository.findByRoleAndStatus(
                        UserRole.PROVIDER,
                        UserStatus.PENDING)
                .stream()
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .toList();
    }
	
    @Override
    public UserResponseDTO approveProvider(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID : " + userId));

        if (user.getRole() != UserRole.PROVIDER) {
            throw new InvalidOperationException(
                    "Selected user is not a provider.");
        }

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Provider is already approved.");
        }

        user.setStatus(UserStatus.ACTIVE);

        User updatedUser = userRepository.save(user);

        auditLogService.log(
                updatedUser.getEmail(),
                "ADMIN",
                "PROVIDER_APPROVED",
                "Service Provider account for " + updatedUser.getFullName() + " (" + updatedUser.getEmail() + ") was APPROVED by Admin."
        );

        emailService.sendProviderApprovalEmail(
                updatedUser.getEmail(),
                updatedUser.getFullName(),
                updatedUser.getFullName()
        );

        return mapper.map(updatedUser, UserResponseDTO.class);
    }
	
    @Override
    public UserResponseDTO rejectProvider(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID : " + userId));

        if (user.getRole() != UserRole.PROVIDER) {
            throw new InvalidOperationException(
                    "Selected user is not a provider.");
        }

        if (user.getStatus() == UserStatus.REJECTED) {
            throw new InvalidOperationException(
                    "Provider is already rejected.");
        }

        user.setStatus(UserStatus.REJECTED);

        User updatedUser = userRepository.save(user);

        auditLogService.log(
                updatedUser.getEmail(),
                "ADMIN",
                "PROVIDER_REJECTED",
                "Service Provider account for " + updatedUser.getFullName() + " (" + updatedUser.getEmail() + ") was REJECTED by Admin."
        );

        return mapper.map(updatedUser, UserResponseDTO.class);
    }

    // ==========================
    // Consumer Management
    // ==========================

    @Override
    @Transactional(readOnly = true)
    public List<ConsumerResponseDTO> getAllConsumers() {
        return consumerService.getAllConsumers();
    }

    // ==========================
    // Booking Management
    // ==========================

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // ==========================
    // Payment Management
    // ==========================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

}