package com.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.entities.User;
import com.backend.entities.UserRole;
import com.backend.entities.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email.
    // Optional is returned because a user with the given email may or may not exist.
    Optional<User> findByEmail(String email);

    // Check if an email is already registered.
    // Used during user registration to prevent duplicate accounts.
    boolean existsByEmail(String email);

    // Check if a phone number is already registered.
    // Used during user registration to prevent duplicate phone numbers.
    boolean existsByPhone(String phone);
    
    List<User> findByRole(UserRole role);

    List<User> findByRoleAndStatus(UserRole role, UserStatus status);
    
    Optional<User> findByVerificationToken(String verificationToken);

    boolean existsByVerificationToken(String verificationToken);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);

}