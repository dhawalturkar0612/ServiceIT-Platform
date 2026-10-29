package com.backend.services;

import java.util.List;

import com.backend.dtos.BookingRequestDTO;
import com.backend.dtos.BookingResponseDTO;

public interface BookingService {

    /**
     * Creates a new booking.
     *
     * @param request Booking request details
     * @return created booking
     */
    BookingResponseDTO createBooking(BookingRequestDTO request);

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId Booking ID
     * @return booking details
     */
    BookingResponseDTO getBookingById(Long bookingId);

    /**
     * Retrieves all bookings made by a consumer.
     *
     * @param consumerId Consumer ID
     * @return list of bookings
     */
    List<BookingResponseDTO> getMyBookings();

    /**
     * Retrieves all bookings for a provider service.
     *
     * @param providerServiceId Provider Service ID
     * @return list of bookings
     */
    List<BookingResponseDTO> getMyProviderBookings();

    /**
     * Cancels an existing booking.
     *
     * @param bookingId Booking ID
     * @return updated booking
     */
    BookingResponseDTO cancelBooking(Long bookingId);
    
    //for admin purpose
    List<BookingResponseDTO> getAllBookings();

    BookingResponseDTO completeBooking(Long bookingId);

}