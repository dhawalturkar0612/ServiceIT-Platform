package com.backend.dtos;

import com.backend.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response returned after successful authentication.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    /**
     * JWT Token
     */
    private String token;

    /**
     * Token Type
     */
    private String tokenType;

    /**
     * Logged in User Details
     */
    private Long userId;

    private String fullName;

    private String email;

    private UserRole role;
}