package com.backend.dtos;

import com.backend.constants.ValidationConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDTO {

    @NotBlank(message = "Reset token is required.")
    private String token;

    @NotBlank(message = "New password is required.")
    @Pattern(
            regexp = ValidationConstants.PASSWORD_REGEX,
            message = "Password must be at least 8 characters long and contain uppercase, lowercase, number and special character."
    )
    private String newPassword;

}
