package com.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payments")
@AttributeOverride(
        name = "id",
        column = @Column(name = "payment_id")
)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "booking")
public class Payment extends BaseEntity {

    // Every payment belongs to exactly one booking.
    // A booking can have at most one payment.
	// Load the associated Booking only when it is actually needed.
	//
	// Why LAZY?
	// Most payment operations (viewing payment status, transaction ID, amount)
	// don't require the complete Booking details.
	//
	// Using LAZY avoids unnecessary database queries and improves performance
	// by preventing Hibernate from loading the entire Booking object every time
	// a Payment is fetched.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    // Payment amount.
    // Usually copied from Booking.totalAmount.
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // Mode of payment.
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    // Current payment status.
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // Transaction ID returned by the payment gateway.
    // For Cash payments this can be "CASH" or generated internally.
    @Column(name = "transaction_id",unique = true, length = 100)
    private String transactionId;

    // Date and time when payment was completed.
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "razorpay_order_id", unique = true, length = 100)
    private String razorpayOrderId;

    public Payment(
            Booking booking,
            BigDecimal amount,
            PaymentMethod paymentMethod,
            String transactionId) {

        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;

        this.paymentStatus = PaymentStatus.PENDING;
    }
}