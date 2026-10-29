package com.backend.services;

import java.util.List;

import com.backend.dtos.CreateOrderRequestDTO;
import com.backend.dtos.CreateOrderResponseDTO;
import com.backend.dtos.PaymentResponseDTO;
import com.backend.dtos.VerifyPaymentRequestDTO;

public interface PaymentService {

    //create payment order
	CreateOrderResponseDTO createOrder(CreateOrderRequestDTO request);
	
	
	//verify payment signature
	PaymentResponseDTO verifyPayment(VerifyPaymentRequestDTO request);

    /**
     * Retrieves payment details by payment ID.
     *
     * @param paymentId payment ID
     * @return payment details
     */
    PaymentResponseDTO getPaymentById(Long paymentId);

    /**
     * Retrieves payment details using booking ID.
     *
     * @param bookingId booking ID
     * @return payment details
     */
    PaymentResponseDTO getPaymentByBookingId(Long bookingId);

    /**
     * Retrieves all payments.
     *
     * @return list of payments
     */
    List<PaymentResponseDTO> getAllPayments();
}