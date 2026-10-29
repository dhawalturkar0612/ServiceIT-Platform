package com.backend.controllers;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dtos.ForgotPasswordRequestDTO;
import com.backend.dtos.LoginResponseDTO;
import com.backend.dtos.ResetPasswordRequestDTO;
import com.backend.dtos.UserLoginRequestDTO;
import com.backend.services.AuthenticationService;
import com.backend.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Handles user authentication, verification, and password reset APIs.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication Controller", description = "APIs for Login, Email Verification, and Password Reset")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    
    @GetMapping("/verify-email")
    @Operation(summary = "Verify user email from link")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam String token) {

        try {
            userService.verifyEmail(token);
        } catch (Exception ex) {
            // Already verified or token expired
        }

        // Redirect browser to React frontend Login Page
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:5173/login?verified=true"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Authenticates user and returns JWT token.
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody UserLoginRequestDTO request) {

        LoginResponseDTO response =
                authenticationService.authenticate(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Requests a password reset link sent via email.
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Send password reset email link")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO request) {

        userService.forgotPassword(request);

        return ResponseEntity.ok(Map.of(
                "message", "Password reset link has been sent to your email address."
        ));
    }

    /**
     * Resets password using the received token.
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using token")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        userService.resetPassword(request);

        return ResponseEntity.ok(Map.of(
                "message", "Password has been reset successfully. You can now log in."
        ));
    }
}