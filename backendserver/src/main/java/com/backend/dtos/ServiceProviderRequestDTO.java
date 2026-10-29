package com.backend.dtos;

import com.backend.constants.ValidationConstants;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceProviderRequestDTO {

    @NotBlank(message = "Business Name is required.")
    @Size(max = 100, message = "Business Name cannot exceed 100 characters.")
    private String businessName;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;

    @NotBlank(message = "City is required.")
    @Size(max = 100, message = "City cannot exceed 100 characters.")
    private String city;

    @NotBlank(message = "State is required.")
    @Size(max = 100, message = "State cannot exceed 100 characters.")
    private String state;

    @NotBlank(message = "Pincode is required.")
    @Pattern(
            regexp = ValidationConstants.PINCODE_REGEX,
            message = "Invalid Pincode"
    )
    private String pincode;

    @NotNull(message = "Experience is required.")
    @Min(value = 0, message = "Experience cannot be negative.")
    @Max(value = 60, message = "Invalid Experience.")
    private Integer experienceYears;

}