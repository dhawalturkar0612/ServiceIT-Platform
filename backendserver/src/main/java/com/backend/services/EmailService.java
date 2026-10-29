package com.backend.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public interface EmailService {

    /**
     * Sends email verification link.
     */
    void sendVerificationEmail(String to, String verificationLink);

    /**
     * Sends Welcome email after registration/verification.
     */
    void sendWelcomeEmail(String to, String fullName, String role);

    /**
     * Sends password reset email link.
     */
    void sendPasswordResetEmail(String to, String fullName, String resetLink);

    /**
     * Sends approval notification to Service Provider when Admin approves account.
     */
    void sendProviderApprovalEmail(String to, String fullName, String businessName);

    /**
     * Sends booking confirmation email to Consumer.
     */
    void sendConsumerBookingConfirmationEmail(
            String to,
            String consumerName,
            String providerName,
            String serviceName,
            LocalDate date,
            LocalTime time,
            BigDecimal amount,
            String address
    );

    /**
     * Sends new booking alert to Service Provider.
     */
    void sendProviderNewBookingNotificationEmail(
            String to,
            String providerName,
            String consumerName,
            String consumerPhone,
            String serviceName,
            LocalDate date,
            LocalTime time,
            BigDecimal amount,
            String address
    );

    /**
     * Sends service completion thank-you email to Consumer.
     */
    void sendServiceCompletedEmail(
            String to,
            String consumerName,
            String providerName,
            String serviceName,
            LocalDate date
    );
}