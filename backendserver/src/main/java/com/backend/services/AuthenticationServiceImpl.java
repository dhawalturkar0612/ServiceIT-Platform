package com.backend.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.backend.dtos.LoginResponseDTO;
import com.backend.dtos.UserLoginRequestDTO;
import com.backend.entities.User;
import com.backend.entities.UserStatus;
import com.backend.exceptions.AccountStatusException;
import com.backend.security.CustomUserDetails;
import com.backend.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final AuditLogService auditLogService;

    @Override
    public LoginResponseDTO authenticate(UserLoginRequestDTO request) {

        // Authenticate user using Spring Security
        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));

        // Retrieve authenticated user
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();
        
        if (!user.isEmailVerified()) {
            throw new AccountStatusException(
                    "Please verify your email before logging in.");
        }
        
        if (user.getStatus() == UserStatus.PENDING) {
            throw new AccountStatusException(
                    "Your registration is under review by the administrator. You can log in after your account has been approved.");
        }

        if (user.getStatus() == UserStatus.REJECTED) {
            throw new AccountStatusException(
                    "Your registration request has been rejected. Please contact the administrator.");
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AccountStatusException(
                    "Your account has been blocked. Please contact the administrator.");
        }

        // Generate JWT
        String token = jwtService.generateToken(user);

        // Record Audit Log
        auditLogService.log(
                user.getEmail(),
                user.getRole().name(),
                "USER_LOGIN",
                user.getFullName() + " (" + user.getRole() + ") logged in successfully."
        );

        // Build Response
        return new LoginResponseDTO(

                token,

                "Bearer",

                user.getId(),

                user.getFullName(),

                user.getEmail(),

                user.getRole());
    }
}