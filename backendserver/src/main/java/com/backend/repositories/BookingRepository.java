package com.backend.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Booking;
import com.backend.entities.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Show all bookings made by a consumer.
    List<Booking> findByConsumerId(Long consumerId);

    // Show all bookings for a provider's service.
    List<Booking> findByProviderServiceId(Long providerServiceId);

    // Filter bookings by status.
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    // Check whether the consumer already has a booking at the same date & time.
    boolean existsByConsumerIdAndServiceDateAndServiceTimeAndBookingStatusNot(
            Long consumerId,
            LocalDate serviceDate,
            LocalTime serviceTime,
            BookingStatus bookingStatus);

    // Check whether the provider service is already booked at the same date & time.
    boolean existsByProviderServiceIdAndServiceDateAndServiceTimeAndBookingStatusNot(
            Long providerServiceId,
            LocalDate serviceDate,
            LocalTime serviceTime,
            BookingStatus bookingStatus);

    // Find all active bookings for a provider on a specific date (across all services offered by provider)
    List<Booking> findByProviderServiceProviderIdAndServiceDateAndBookingStatusNot(
            Long providerId,
            LocalDate serviceDate,
            BookingStatus bookingStatus);

    // Find all active bookings for a consumer on a specific date
    List<Booking> findByConsumerIdAndServiceDateAndBookingStatusNot(
            Long consumerId,
            LocalDate serviceDate,
            BookingStatus bookingStatus);

    List<Booking> findByConsumerUserIdOrderByIdDesc(Long userId);
    List<Booking> findByProviderServiceProviderUserIdOrderByIdDesc(Long userId);
    List<Booking> findAllByOrderByIdDesc();
}