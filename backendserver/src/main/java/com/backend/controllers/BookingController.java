package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.BookingRequestDTO;
import com.backend.dtos.BookingResponseDTO;
import com.backend.services.BookingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Creates a new booking.
     *
     * @param request Booking details
     * @return created booking
     */
    @PreAuthorize("hasRole('CONSUMER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDTO createBooking(
            @Valid @RequestBody BookingRequestDTO request) {

        return bookingService.createBooking(request);
    }

    /**
     * Retrieves booking by ID.
     *
     * @param bookingId Booking ID
     * @return booking details
     */
    @PreAuthorize("hasAnyRole('ADMIN','CONSUMER','PROVIDER')")
    @GetMapping("/{bookingId}")
    public BookingResponseDTO getBookingById(
            @PathVariable Long bookingId) {

        return bookingService.getBookingById(bookingId);
    }

    /**
     * Retrieves all bookings of a consumer.
     *
     * @param consumerId Consumer ID
     * @return list of bookings
     */
    @PreAuthorize("hasRole('CONSUMER')")
    @GetMapping("/my-bookings")
    public List<BookingResponseDTO> getMyBookings() {

        return bookingService.getMyBookings();
    }

    /**
     * Retrieves all bookings assigned to the logged-in provider.
     *
     * @return list of provider bookings
     */
    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider")
    public ResponseEntity<List<BookingResponseDTO>> getMyProviderBookings() {

        return ResponseEntity.ok(
                bookingService.getMyProviderBookings());
    }

    /** 
     * Cancels an existing booking.
     *
     * @param bookingId Booking ID
     * @return updated booking
     */
    @PreAuthorize("hasRole('CONSUMER')")
    @PutMapping("/{bookingId}/cancel")
    public BookingResponseDTO cancelBooking(
            @PathVariable Long bookingId) {

        return bookingService.cancelBooking(bookingId);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {

        return bookingService.getAllBookings();
    }

    /**
     * Marks a booking as COMPLETED.
     */
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    @PutMapping("/{bookingId}/complete")
    public BookingResponseDTO completeBooking(
            @PathVariable Long bookingId) {

        return bookingService.completeBooking(bookingId);
    }

}