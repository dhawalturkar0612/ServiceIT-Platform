package com.backend.services;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.BookingRequestDTO;
import com.backend.dtos.BookingResponseDTO;
import com.backend.entities.Booking;
import com.backend.entities.BookingStatus;
import com.backend.entities.Consumer;
import com.backend.entities.ProviderService;
import com.backend.entities.User;
import com.backend.entities.UserRole;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.BookingRepository;
import com.backend.repositories.ConsumerRepository;
import com.backend.repositories.ProviderServiceRepository;
import com.backend.repositories.UserRepository;
import com.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ConsumerRepository consumerRepository;

    private final ProviderServiceRepository providerServiceRepository;

    private final ModelMapper mapper;
    
    private final UserRepository userRepository;

    private final AuditLogService auditLogService;

    private final EmailService emailService;

    /**
     * Creates a new booking.
     */
    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {

    	Long userId = SecurityUtils.getCurrentUserId();

    	Consumer consumer = consumerRepository
    	        .findByUserId(userId)
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException(
    	                        "Consumer profile not found."));

        ProviderService providerService =
                providerServiceRepository.findById(request.getProviderServiceId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Provider Service not found with ID : "
                                                + request.getProviderServiceId()));

        // Provider must be offering this service.
        if (!providerService.getAvailable()) {
            throw new InvalidOperationException(
                    "Selected service is currently unavailable.");
        }

        // 1. Operating Hours Constraint: 8:00 AM (08:00) to 10:00 PM (22:00)
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(22, 0);
        LocalTime newStartTime = request.getServiceTime();

        if (newStartTime.isBefore(openingTime) || newStartTime.isAfter(closingTime)) {
            throw new InvalidOperationException("Service bookings are only accepted between 08:00 AM and 10:00 PM.");
        }

        int newDuration = providerService.getEstimatedDuration() != null ? providerService.getEstimatedDuration() : 60;
        LocalTime newEndTime = newStartTime.plusMinutes(newDuration);

        if (newEndTime.isAfter(closingTime) || (newEndTime.isBefore(newStartTime) && !newEndTime.equals(LocalTime.MIDNIGHT))) {
            throw new InvalidOperationException("This service requires " + newDuration + " minutes and cannot finish before the 10:00 PM closing limit. Please choose an earlier time slot.");
        }

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("hh:mm a");

        // 2. Provider Overlap Check across ALL services with Estimated Duration + 1-Hour Buffer
        Long providerId = providerService.getProvider().getId();
        List<Booking> providerBookings = bookingRepository
                .findByProviderServiceProviderIdAndServiceDateAndBookingStatusNot(
                        providerId,
                        request.getServiceDate(),
                        BookingStatus.CANCELLED);

        for (Booking existing : providerBookings) {
            LocalTime existingStart = existing.getServiceTime();
            int existingDuration = existing.getProviderService().getEstimatedDuration() != null
                    ? existing.getProviderService().getEstimatedDuration()
                    : 60;
            // Existing service occupies existingDuration + 60 minutes buffer for travel/fulfillment
            LocalTime busyEnd = existingStart.plusMinutes(existingDuration + 60);

            // Interval overlap condition between [newStartTime, newEndTime] and [existingStart, busyEnd]
            boolean overlaps = newStartTime.isBefore(busyEnd) && newEndTime.isAfter(existingStart);

            if (overlaps) {
                LocalTime serviceFinishTime = existingStart.plusMinutes(existingDuration);
                throw new InvalidOperationException(
                        "Provider '" + providerService.getProvider().getBusinessName()
                                + "' is unavailable at this time due to an existing booking for '"
                                + existing.getProviderService().getService().getServiceName() + "' from "
                                + existingStart.format(timeFmt) + " to " + serviceFinishTime.format(timeFmt)
                                + " (+ 1-hour travel buffer until " + busyEnd.format(timeFmt) + "). "
                                + "Please select a time slot after " + busyEnd.format(timeFmt) + ".");
            }
        }

        // 3. Consumer Overlap Check
        List<Booking> consumerBookings = bookingRepository
                .findByConsumerIdAndServiceDateAndBookingStatusNot(
                        consumer.getId(),
                        request.getServiceDate(),
                        BookingStatus.CANCELLED);

        for (Booking existing : consumerBookings) {
            LocalTime existingStart = existing.getServiceTime();
            int existingDuration = existing.getProviderService().getEstimatedDuration() != null
                    ? existing.getProviderService().getEstimatedDuration()
                    : 60;
            LocalTime existingEnd = existingStart.plusMinutes(existingDuration);

            boolean overlaps = newStartTime.isBefore(existingEnd) && newEndTime.isAfter(existingStart);
            if (overlaps) {
                throw new InvalidOperationException(
                        "You already have a booking for '" + existing.getProviderService().getService().getServiceName()
                                + "' from " + existingStart.format(timeFmt) + " to " + existingEnd.format(timeFmt)
                                + " on this date. Please select a different time slot.");
            }
        }

        Booking booking = mapper.map(request, Booking.class);

        booking.setConsumer(consumer);
        booking.setProviderService(providerService);

        // Copy price snapshot
        booking.setTotalAmount(providerService.getPrice());

        // Initial status
        booking.setBookingStatus(BookingStatus.PENDING_PAYMENT);

        Booking savedBooking = bookingRepository.save(booking);

        auditLogService.log(
                consumer.getUser().getEmail(),
                "CONSUMER",
                "BOOKING_CREATED",
                "Consumer " + consumer.getUser().getFullName() + " booked '" + providerService.getService().getServiceName()
                        + "' from " + providerService.getProvider().getBusinessName() + " for ₹" + savedBooking.getTotalAmount()
                        + " on " + savedBooking.getServiceDate() + "."
        );

        return mapToResponse(savedBooking);
    }

    /**
     * Retrieves booking by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with ID : " + bookingId));

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN:
                break;

            case CONSUMER:
                validateConsumerOwnership(booking);
                break;

            case PROVIDER:
                validateProviderOwnership(booking);
                break;

            default:
                throw new AccessDeniedException("Access Denied.");
        }

        return mapToResponse(booking);
    }

    /**
     * Retrieves bookings of a consumer.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getMyBookings() {

        Long userId = SecurityUtils.getCurrentUserId();

        return bookingRepository.findByConsumerUserIdOrderByIdDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Retrieves bookings for a provider service.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getMyProviderBookings() {

        Long userId = SecurityUtils.getCurrentUserId();

        return bookingRepository
                .findByProviderServiceProviderUserIdOrderByIdDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Cancels an existing booking.
     */
    @Override
    public BookingResponseDTO cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found."));

        // Only the booking owner can cancel it
        validateConsumerOwnership(booking);

        // Cannot cancel completed booking
        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new InvalidOperationException(
                    "Completed booking cannot be cancelled.");
        }

        // Optional: Prevent cancelling an already cancelled booking
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidOperationException(
                    "Booking is already cancelled.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);

        Booking updatedBooking = bookingRepository.save(booking);

        auditLogService.log(
                booking.getConsumer().getUser().getEmail(),
                "CONSUMER",
                "BOOKING_CANCELLED",
                "Booking #" + booking.getId() + " for '" + booking.getProviderService().getService().getServiceName()
                        + "' was cancelled by " + booking.getConsumer().getUser().getFullName() + "."
        );

        return mapToResponse(updatedBooking);
    }
    
    /**
     * Admin can find all the bookings
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {

        return bookingRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Provider or Admin can mark a booking as COMPLETED.
     */
    @Override
    public BookingResponseDTO completeBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found."));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.PROVIDER) {
            validateProviderOwnership(booking);
        } else if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You are not allowed to complete this booking.");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidOperationException("Cancelled booking cannot be marked as completed.");
        }

        // Ensure service date and time has arrived before allowing completion
        if (booking.getServiceDate() != null) {
            java.time.LocalTime time = booking.getServiceTime() != null ? booking.getServiceTime() : java.time.LocalTime.MIDNIGHT;
            java.time.LocalDateTime scheduledDateTime = java.time.LocalDateTime.of(booking.getServiceDate(), time);
            if (java.time.LocalDateTime.now().isBefore(scheduledDateTime)) {
                throw new InvalidOperationException(
                        "Cannot mark service as completed before its scheduled date and time (" + booking.getServiceDate() + " at " + time + ").");
            }
        }

        booking.setBookingStatus(BookingStatus.COMPLETED);

        Booking updatedBooking = bookingRepository.save(booking);

        auditLogService.log(
                currentUser.getEmail(),
                currentUser.getRole().name(),
                "BOOKING_COMPLETED",
                "Booking #" + booking.getId() + " for '" + booking.getProviderService().getService().getServiceName()
                        + "' was marked as COMPLETED by " + currentUser.getFullName() + "."
        );

        // Send service completed email to Consumer
        emailService.sendServiceCompletedEmail(
                booking.getConsumer().getUser().getEmail(),
                booking.getConsumer().getUser().getFullName(),
                booking.getProviderService().getProvider().getBusinessName(),
                booking.getProviderService().getService().getServiceName(),
                booking.getServiceDate()
        );

        return mapToResponse(updatedBooking);
    }
    
    
    
    private User getCurrentUser() {

        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    private void validateConsumerOwnership(Booking booking) {

        if (!booking.getConsumer().getUser().getId()
                .equals(SecurityUtils.getCurrentUserId())) {

            throw new AccessDeniedException(
                    "You are not allowed to access this booking.");
        }
    }

    private void validateProviderOwnership(Booking booking) {

        if (!booking.getProviderService()
                .getProvider()
                .getUser()
                .getId()
                .equals(SecurityUtils.getCurrentUserId())) {

            throw new AccessDeniedException(
                    "You are not allowed to access this booking.");
        }
    }

    /**
     * Maps Booking entity to BookingResponseDTO.
     */
    private BookingResponseDTO mapToResponse(Booking booking) {

        BookingResponseDTO response =
                mapper.map(booking, BookingResponseDTO.class);

        // Booking
        response.setBookingId(booking.getId());

        // Consumer
        response.setConsumerId(booking.getConsumer().getId());
        response.setConsumerName(
                booking.getConsumer().getUser().getFullName());

        // Provider
        response.setProviderId(
                booking.getProviderService().getProvider().getId());
        response.setProviderName(
                booking.getProviderService().getProvider().getBusinessName());

        // Provider Service
        response.setProviderServiceId(
                booking.getProviderService().getId());

        // Service
        response.setServiceId(
                booking.getProviderService().getService().getId());
        response.setServiceName(
                booking.getProviderService().getService().getServiceName());

        return response;
    }

}