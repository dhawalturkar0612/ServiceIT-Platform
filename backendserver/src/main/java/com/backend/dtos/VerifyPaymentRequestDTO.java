package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO received from React after successful Razorpay payment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPaymentRequestDTO {

    /**
     * Booking ID.
     */
    @NotNull(message = "Booking ID is required.")
    private Long bookingId;

    /**
     * Razorpay Order ID.
     */
    @NotBlank(message = "Razorpay Order ID is required.")
    private String razorpayOrderId;

    /**
     * Razorpay Payment ID.
     */
    @NotBlank(message = "Razorpay Payment ID is required.")
    private String razorpayPaymentId;

    /**
     * Razorpay Signature.
     */
    @NotBlank(message = "Razorpay Signature is required.")
    private String razorpaySignature;
}