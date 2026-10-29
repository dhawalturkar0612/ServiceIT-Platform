package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//import com.backend.dtos.UserLoginRequestDTO;
import com.backend.dtos.UserRegisterRequestDTO;
import com.backend.dtos.UserResponseDTO;
import com.backend.dtos.UserUpdateRequestDTO;
import com.backend.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(
        name = "User Controller",
        description = "APIs for User Management"
)
public class UserController {

    private final UserService userService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {

        return ResponseEntity.ok(
                userService.getCurrentUser());
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRegisterRequestDTO request) {

        UserResponseDTO response = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @PostMapping("/login")
//    @Operation(summary = "Authenticate user")
//    public ResponseEntity<UserResponseDTO> loginUser(
//            @Valid @RequestBody UserLoginRequestDTO request) {
//
//        UserResponseDTO response = userService.loginUser(request);
//
//        return ResponseEntity.ok(response);
//    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long userId) {

        UserResponseDTO response = userService.getUserById(userId);

        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> response = userService.getAllUsers();

        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDTO request) {

        UserResponseDTO response = userService.updateUser(userId, request);

        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok("User deleted successfully.");
    }
}