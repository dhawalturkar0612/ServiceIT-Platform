package com.backend.dtos;

import com.backend.constants.ValidationConstants;
import com.backend.entities.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used to receive user registration details from the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDTO {

    @NotBlank(message = "Full name is required.")
    @Size(min = 3, max = 100,
            message = "Full name must be between 3 and 100 characters.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = ValidationConstants.PHONE_REGEX,
            message = "Phone number must be exactly 10 digits and start with 6, 7, 8, or 9."
    )
    private String phone;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = ValidationConstants.PASSWORD_REGEX,
            message = "Password must be at least 8 characters long and contain uppercase, lowercase, number and special character."
    )
    private String password;

    @NotNull(message = "User role is required.")
    private UserRole role;

}