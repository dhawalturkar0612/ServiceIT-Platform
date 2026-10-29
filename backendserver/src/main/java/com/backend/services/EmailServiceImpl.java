package com.backend.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        String subject = "Verify Your Email - SERVICEiT";
        String body = """
                Dear User,

                Welcome to SERVICEiT - Your On-Demand Home & Professional Services Platform!

                Please click the link below to verify your email address and activate your account:
                %s

                If you did not create an account on SERVICEiT, please ignore this email.

                Best regards,
                The SERVICEiT Team
                """.formatted(verificationLink);

        sendMailAsync(to, subject, body);
    }

    @Override
    public void sendWelcomeEmail(String to, String fullName, String role) {
        String subject = "Welcome to SERVICEiT - Registration Successful! 🎉";
        String body = """
                Dear %s,

                Congratulations! Your %s account on SERVICEiT has been successfully verified.

                You can now log in to the portal at http://localhost:5173/login to explore services, book appointments, and manage your account.

                Thank you for choosing SERVICEiT!

                Best regards,
                The SERVICEiT Team
                """.formatted(fullName, role);

        sendMailAsync(to, subject, body);
    }

    @Override
    public void sendPasswordResetEmail(String to, String fullName, String resetLink) {
        String subject = "Reset Your Password - SERVICEiT 🔒";
        String body = """
                Dear %s,

                We received a request to reset your password for your SERVICEiT account.

                Please click the link below to set a new password:
                %s

                ⏰ IMPORTANT: This password reset link is valid for 1 hour.

                If you did not request a password reset, please ignore this email or contact support. Your password will remain unchanged.

                Best regards,
                The SERVICEiT Security Team
                """.formatted(fullName != null ? fullName : "User", resetLink);

        sendMailAsync(to, subject, body);
    }

    @Override
    public void sendProviderApprovalEmail(String to, String fullName, String businessName) {
        String subject = "SERVICEiT - Congratulations! Your Provider Account is Approved 🎉";
        String body = """
                Dear %s,

                Great news! Your service provider registration for "%s" has been verified and APPROVED by the Administrator.

                Your account is now fully active. You can now:
                1. Log in to your Provider Portal: http://localhost:5173/login
                2. Set up your business profile and services
                3. Receive and manage customer bookings

                We are excited to have you on board!

                Best regards,
                The SERVICEiT Administration Team
                """.formatted(fullName, businessName != null ? businessName : "your business");

        sendMailAsync(to, subject, body);
    }

    @Override
    public void sendConsumerBookingConfirmationEmail(
            String to,
            String consumerName,
            String providerName,
            String serviceName,
            LocalDate date,
            LocalTime time,
            BigDecimal amount,
            String address
    ) {
        String dateStr = date != null ? date.format(DATE_FMT) : "N/A";
        String timeStr = time != null ? time.format(TIME_FMT) : "N/A";

        String subject = "SERVICEiT - Booking Confirmed: " + serviceName + " 🛠️";
        String body = """
                Dear %s,

                Your booking has been successfully confirmed and scheduled!

                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                📋 BOOKING DETAILS
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                • Service:          %s
                • Provider:         %s
                • Scheduled Date:   %s
                • Scheduled Time:   %s
                • Total Paid:       ₹%s
                • Service Address:  %s
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                ⏰ IMPORTANT REMINDER:
                Please ensure someone is available at the provided address at the scheduled date and time.

                You can view and track your booking status anytime in your dashboard:
                http://localhost:5173/consumer/bookings

                Thank you for choosing SERVICEiT!

                Best regards,
                The SERVICEiT Team
                """.formatted(consumerName, serviceName, providerName, dateStr, timeStr, amount != null ? amount.toString() : "0.00", address != null ? address : "On file");

        sendMailAsync(to, subject, body);
    }

    @Override
    public void sendProviderNewBookingNotificationEmail(
            String to,
            String providerName,
            String consumerName,
            String consumerPhone,
            String serviceName,
            LocalDate date,
            LocalTime time,
            BigDecimal amount,
            String address
    ) {
        String dateStr = date != null ? date.format(DATE_FMT) : "N/A";
        String timeStr = time != null ? time.format(TIME_FMT) : "N/A";

        String subject = "SERVICEiT - New Service Booking Received! 📦";
        String body = """
                Dear %s,

                You have received a new service booking on SERVICEiT!

                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                📋 NEW BOOKING ASSIGNMENT
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                • Service:           %s
                • Customer Name:     %s
                • Customer Contact:  %s
                • Scheduled Date:    %s
                • Scheduled Time:    %s
                • Amount:            ₹%s
                • Service Location:  %s
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                📌 ACTION REQUIRED:
                Please review this booking in your Provider Dashboard and ensure prompt service delivery on the scheduled date and time:
                http://localhost:5173/provider/bookings

                Best regards,
                The SERVICEiT Team
                """.formatted(providerName, serviceName, consumerName, consumerPhone != null ? consumerPhone : "N/A", dateStr, timeStr, amount != null ? amount.toString() : "0.00", address != null ? address : "Customer Address");

        sendMailAsync(to, subject, body);
    }

    @Override
    public void sendServiceCompletedEmail(
            String to,
            String consumerName,
            String providerName,
            String serviceName,
            LocalDate date
    ) {
        String dateStr = date != null ? date.format(DATE_FMT) : "today";

        String subject = "SERVICEiT - Service Completed Successfully! ✨";
        String body = """
                Dear %s,

                We are happy to inform you that your service for "%s" by %s on %s has been marked as COMPLETED!

                We hope you had a great experience and we were delighted to serve you at your place.

                If you have any feedback or need further assistance, please feel free to reach out to us.

                Thank you for trusting SERVICEiT!

                Best regards,
                The SERVICEiT Team
                """.formatted(consumerName, serviceName, providerName, dateStr);

        sendMailAsync(to, subject, body);
    }

    private void sendMailAsync(String to, String subject, String body) {
        // Run in background thread to avoid blocking HTTP request
        new Thread(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);

                mailSender.send(message);
                log.info("[EMAIL SENT] Successfully sent email '{}' to {}", subject, to);
            } catch (Exception ex) {
                log.warn("[EMAIL FAILED] Could not send email to {}: {}", to, ex.getMessage());
            }
        }).start();
    }
}