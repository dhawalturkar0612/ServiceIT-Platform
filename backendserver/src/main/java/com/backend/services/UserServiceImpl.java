package com.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.ForgotPasswordRequestDTO;
import com.backend.dtos.ResetPasswordRequestDTO;
import com.backend.dtos.UserRegisterRequestDTO;
import com.backend.dtos.UserResponseDTO;
import com.backend.dtos.UserUpdateRequestDTO;
import com.backend.entities.User;
import com.backend.entities.UserStatus;
import com.backend.entities.ProviderService;
import com.backend.entities.Booking;
import com.backend.exceptions.DuplicateResourceException;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.UserRepository;
import com.backend.repositories.ConsumerRepository;
import com.backend.repositories.ServiceProviderRepository;
import com.backend.repositories.ProviderServiceRepository;
import com.backend.repositories.BookingRepository;
import com.backend.repositories.PaymentRepository;
import com.backend.security.SecurityUtils;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ProviderServiceRepository providerServiceRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper mapper;
    private final AuditLogService auditLogService;

    @Override
    public UserResponseDTO registerUser(UserRegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered.");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number already registered.");
        }

        User user = mapper.map(request, User.class);
        
        // Generate unique verification token
        String verificationToken = UUID.randomUUID().toString();

        user.setVerificationToken(verificationToken);
        user.setEmailVerified(false);

        // Password will be encoded due Spring Security integration
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign default status based on role
        switch (user.getRole()) {

            case CONSUMER:
                user.setStatus(UserStatus.ACTIVE);
                break;

            case PROVIDER:
                user.setStatus(UserStatus.PENDING);
                break;

            case ADMIN:
                user.setStatus(UserStatus.ACTIVE);
                break;

            default:
                throw new IllegalArgumentException("Invalid User Role.");
        }

        User savedUser = userRepository.save(user);
        
        auditLogService.log(
                savedUser.getEmail(),
                savedUser.getRole().name(),
                "USER_REGISTER",
                "New " + savedUser.getRole() + " account created for " + savedUser.getFullName() + "."
        );

        String verificationLink =
                "http://localhost:8080/api/auth/verify-email?token="
                        + verificationToken;

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                verificationLink);

        return mapper.map(savedUser, UserResponseDTO.class);
    }
    
    @Override
    public void verifyEmail(String token) {

        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid or expired verification link."));

        if (user.isEmailVerified()) {
            throw new InvalidOperationException(
                    "Email is already verified.");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);

        auditLogService.log(
                user.getEmail(),
                user.getRole().name(),
                "EMAIL_VERIFIED",
                "Email address verified successfully for " + user.getFullName() + "."
        );

        emailService.sendWelcomeEmail(
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
        );
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with email: " + request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), resetLink);

        auditLogService.log(
                user.getEmail(),
                user.getRole().name(),
                "FORGOT_PASSWORD_REQUEST",
                "Password reset link requested for " + user.getFullName() + "."
        );
    }

    @Override
    public void resetPassword(ResetPasswordRequestDTO request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired password reset link."));

        if (user.getResetPasswordTokenExpiry() != null && user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException("Password reset link has expired. Please request a new link.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        userRepository.save(user);

        auditLogService.log(
                user.getEmail(),
                user.getRole().name(),
                "PASSWORD_RESET",
                "Password updated successfully for " + user.getFullName() + "."
        );
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID : " + userId));

        return mapper.map(user, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID : " + userId));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered.");
        }

        if (!user.getPhone().equals(request.getPhone())
                && userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number already registered.");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        return mapper.map(updatedUser, UserResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID : " + userId));

        // 1. Clean up Consumer profile & bookings if present
        consumerRepository.findByUserId(userId).ifPresent(consumer -> {
            List<Booking> consumerBookings = bookingRepository.findByConsumerId(consumer.getId());
            for (Booking booking : consumerBookings) {
                paymentRepository.findByBookingId(booking.getId()).ifPresent(paymentRepository::delete);
                bookingRepository.delete(booking);
            }
            consumerRepository.delete(consumer);
        });

        // 2. Clean up ServiceProvider profile, services & bookings if present
        serviceProviderRepository.findByUserId(userId).ifPresent(provider -> {
            List<ProviderService> providerServices = providerServiceRepository.findByProviderId(provider.getId());
            for (ProviderService ps : providerServices) {
                List<Booking> serviceBookings = bookingRepository.findByProviderServiceId(ps.getId());
                for (Booking booking : serviceBookings) {
                    paymentRepository.findByBookingId(booking.getId()).ifPresent(paymentRepository::delete);
                    bookingRepository.delete(booking);
                }
                providerServiceRepository.delete(ps);
            }
            serviceProviderRepository.delete(provider);
        });

        // 3. Delete User entity
        userRepository.delete(user);

        auditLogService.log(
                user.getEmail(),
                user.getRole().name(),
                "USER_DELETED",
                "User " + user.getFullName() + " (" + user.getEmail() + ") was deleted by Admin."
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser() {

        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        return mapper.map(user, UserResponseDTO.class);
    }
    
}