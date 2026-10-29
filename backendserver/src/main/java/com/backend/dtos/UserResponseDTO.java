package com.backend.dtos;

import com.backend.entities.UserRole;
import com.backend.entities.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used to send user information back to the client.
 * It contains only safe, non-sensitive user details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    /**
     * Unique identifier of the user.
     */
    private Long id;

    /**
     * User's full name.
     */
    private String fullName;

    /**
     * User's registered email address.
     */
    private String email;

    /**
     * User's registered mobile number.
     */
    private String phone;

    /**
     * Role assigned to the user.
     */
    private UserRole role;

    /**
     * Current account status.
     */
    private UserStatus status;

}