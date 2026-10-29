package com.backend.dtos;

import com.backend.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDTO {

    @NotBlank(message = "Full Name is required")
    @Size(min = 3, max = 100)
    private String fullName;

    @Email(message = "Invalid Email")
    private String email;

    @Pattern(
            regexp = ValidationConstants.PHONE_REGEX,
            message = "Invalid Mobile Number"
    )
    private String phone;
}