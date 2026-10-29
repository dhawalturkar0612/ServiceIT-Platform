package com.backend.entities;

import com.backend.constants.ValidationConstants;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "password")
public class User extends BaseEntity {

    @NotBlank(message = "Full Name is required")
    @Size(min = 3, max = 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Email(message = "Invalid Email")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Pattern(
            regexp = ValidationConstants.PHONE_REGEX,
            message = "Invalid Mobile Number"
    )
    @Column(nullable = false, unique = true, length = 10)
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255)
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;
    
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "verification_token", unique = true, length = 100)
    private String verificationToken;

    @Column(name = "reset_password_token", length = 100)
    private String resetPasswordToken;

    @Column(name = "reset_password_token_expiry")
    private java.time.LocalDateTime resetPasswordTokenExpiry;

    public User(String fullName, String email, String phone,
                String password, UserRole role, UserStatus status) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}