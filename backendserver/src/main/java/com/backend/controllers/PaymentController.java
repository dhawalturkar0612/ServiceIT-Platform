package com.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.CreateOrderRequestDTO;
import com.backend.dtos.CreateOrderResponseDTO;
import com.backend.dtos.PaymentResponseDTO;
import com.backend.dtos.VerifyPaymentRequestDTO;
import com.backend.services.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates a Razorpay Order.
     */
    @PreAuthorize("hasRole('CONSUMER')")
    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO request) {

        return ResponseEntity.ok(
                paymentService.createOrder(request));
    }
    
    @PostMapping("/verify")
    @PreAuthorize("hasRole('CONSUMER')")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequestDTO request) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(request));
    }

    /**
     * Retrieves payment by payment ID.
     *
     * @param paymentId payment ID
     * @return payment details
     */
    @PreAuthorize("hasAnyRole('ADMIN','CONSUMER','PROVIDER')")
    @GetMapping("/{paymentId}")
    public PaymentResponseDTO getPaymentById(
            @PathVariable Long paymentId) {

        return paymentService.getPaymentById(paymentId);
    }

    /**
     * Retrieves payment by booking ID.
     *
     * @param bookingId booking ID
     * @return payment details
     */
    @PreAuthorize("hasAnyRole('ADMIN','CONSUMER','PROVIDER')")
    @GetMapping("/booking/{bookingId}")
    public PaymentResponseDTO getPaymentByBookingId(
            @PathVariable Long bookingId) {

        return paymentService.getPaymentByBookingId(bookingId);
    }

    /**
     * Retrieves all payments.
     *
     * @return list of payments
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentService.getAllPayments();
    }

}