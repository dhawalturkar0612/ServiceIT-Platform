package com.backend.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;

import com.razorpay.Order;
import com.razorpay.Utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.CreateOrderRequestDTO;
import com.backend.dtos.CreateOrderResponseDTO;

import com.backend.dtos.VerifyPaymentRequestDTO;
import com.backend.dtos.PaymentResponseDTO;
import com.backend.entities.Booking;
import com.backend.entities.Payment;
import com.backend.entities.PaymentMethod;
import com.backend.entities.PaymentStatus;
import com.backend.entities.BookingStatus;
import com.backend.entities.User;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.BookingRepository;
import com.backend.repositories.PaymentRepository;
import com.backend.repositories.UserRepository;
import com.backend.security.SecurityUtils;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
	
	private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AuditLogService auditLogService;
    private final EmailService emailService;
    
    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;
    
    /**
     * Creates a Razorpay order for the selected booking.
     *
     * Flow:
     * 1. Validate booking.
     * 2. Verify logged-in consumer owns the booking.
     * 3. Booking must be awaiting payment.
     * 4. Prevent duplicate payment.
     * 5. Create Razorpay Order.
     */
    

    
    @Override
    public CreateOrderResponseDTO createOrder(
            CreateOrderRequestDTO request) {

        // Fetch booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found."));

        // Only booking owner can pay
        validateConsumerOwnership(booking);

        // Booking must be awaiting payment
        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {

            throw new InvalidOperationException(
                    "This booking is not awaiting payment.");
        }

        // Prevent duplicate payment
        if (paymentRepository.existsByBookingId(booking.getId())) {

            throw new InvalidOperationException(
                    "Payment already exists for this booking.");
        }

        // Amount in paise (Razorpay requirement)
        int amountInPaise = booking.getTotalAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValue();

        JSONObject options = new JSONObject();

        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "BOOKING_" + booking.getId());

        Order order;

        try {
            order = razorpayClient.orders.create(options);
        } catch (RazorpayException e) {
            throw new InvalidOperationException(
                    "Unable to create Razorpay order.");
        }

        return new CreateOrderResponseDTO(

                order.get("id"),

                order.get("currency"),

                booking.getTotalAmount(),

                razorpayKey);
    }
    
    @Override
    public PaymentResponseDTO verifyPayment(VerifyPaymentRequestDTO request) {

        // Fetch booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found."));

        // Only booking owner can verify payment
        validateConsumerOwnership(booking);

        // Booking must be awaiting payment
        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new InvalidOperationException(
                    "Booking is not awaiting payment.");
        }

        // Prevent duplicate payment
        if (paymentRepository.existsByBookingId(booking.getId())) {
            throw new InvalidOperationException(
                    "Payment already exists.");
        }

        // Verify Razorpay Signature
        JSONObject attributes = new JSONObject();

        attributes.put("razorpay_order_id",
                request.getRazorpayOrderId());

        attributes.put("razorpay_payment_id",
                request.getRazorpayPaymentId());

        attributes.put("razorpay_signature",
                request.getRazorpaySignature());

        boolean verified;

        try {
            verified = Utils.verifyPaymentSignature(
                    attributes,
                    razorpaySecret);
        } catch (RazorpayException e) {
            throw new InvalidOperationException(
                    "Payment verification failed.");
        }

        if (!verified) {
            throw new InvalidOperationException(
                    "Invalid payment signature.");
        }

        // Save Payment
        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(PaymentMethod.ONLINE);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(request.getRazorpayPaymentId());
        payment.setPaymentDate(LocalDateTime.now());

        // If you added razorpayOrderId field
        // payment.setRazorpayOrderId(request.getRazorpayOrderId());

        Payment savedPayment = paymentRepository.save(payment);

        // Update booking status
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        auditLogService.log(
                booking.getConsumer().getUser().getEmail(),
                "CONSUMER",
                "PAYMENT_SUCCESS",
                "Payment of ₹" + savedPayment.getAmount() + " verified successfully via Razorpay (Txn ID: "
                        + savedPayment.getTransactionId() + ") for Booking #" + booking.getId() + "."
        );

        // 1. Send confirmation email to Consumer
        emailService.sendConsumerBookingConfirmationEmail(
                booking.getConsumer().getUser().getEmail(),
                booking.getConsumer().getUser().getFullName(),
                booking.getProviderService().getProvider().getBusinessName(),
                booking.getProviderService().getService().getServiceName(),
                booking.getServiceDate(),
                booking.getServiceTime(),
                savedPayment.getAmount(),
                booking.getConsumer().getAddress() + ", " + booking.getConsumer().getCity()
        );

        // 2. Send new booking alert email to Provider
        emailService.sendProviderNewBookingNotificationEmail(
                booking.getProviderService().getProvider().getUser().getEmail(),
                booking.getProviderService().getProvider().getBusinessName(),
                booking.getConsumer().getUser().getFullName(),
                booking.getConsumer().getUser().getPhone(),
                booking.getProviderService().getService().getServiceName(),
                booking.getServiceDate(),
                booking.getServiceTime(),
                savedPayment.getAmount(),
                booking.getConsumer().getAddress() + ", " + booking.getConsumer().getCity()
        );

        return mapToResponse(savedPayment);
    }
    

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long paymentId) {

    	Payment payment = paymentRepository.findById(paymentId)
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException(
    	                        "Payment not found with ID : " + paymentId));

    	Booking booking = payment.getBooking();

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

    	return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByBookingId(Long bookingId) {

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for Booking ID : " + bookingId));

        Booking booking = payment.getBooking();

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

        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    
    
    //helper methods
    private User getCurrentUser() {

        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }
    
    private void validateConsumerOwnership(Booking booking) {

        if (!booking.getConsumer()
                .getUser()
                .getId()
                .equals(SecurityUtils.getCurrentUserId())) {

            throw new AccessDeniedException(
                    "You are not allowed to access this payment.");
        }
    }
    
    private void validateProviderOwnership(Booking booking) {

        if (!booking.getProviderService()
                .getProvider()
                .getUser()
                .getId()
                .equals(SecurityUtils.getCurrentUserId())) {

            throw new AccessDeniedException(
                    "You are not allowed to access this payment.");
        }
    }
    
    
    /**
     * Converts Payment entity into PaymentResponseDTO.
     *
     * @param payment Payment entity
     * @return PaymentResponseDTO
     */
    private PaymentResponseDTO mapToResponse(Payment payment) {

        PaymentResponseDTO response =
                mapper.map(payment, PaymentResponseDTO.class);

        response.setPaymentId(payment.getId());
        response.setBookingId(payment.getBooking().getId());

        return response;
    }
}