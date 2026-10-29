package com.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "service_providers")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "user")
public class ServiceProvider extends BaseEntity {

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "provider_id", nullable = false)
    @MapsId
    private User user;

    @NotBlank(message = "Business Name is required")
    @Size(max = 100)
    @Column(name = "business_name", nullable = false, length = 100)
    private String businessName;

    @Column(length = 500)
    private String description;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false, length = 100)
    private String state;

    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Invalid Pincode"
    )
    @Column(nullable = false, length = 6)
    private String pincode;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 60, message = "Invalid Experience")
    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;


    public ServiceProvider(String businessName,
                           String description,
                           String address,
                           String city,
                           String state,
                           String pincode,
                           Integer experienceYears) {

        this.businessName = businessName;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.experienceYears = experienceYears;
    }
    
}