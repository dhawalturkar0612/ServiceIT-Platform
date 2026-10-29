package com.backend.services;

import java.util.List;

import com.backend.dtos.ForgotPasswordRequestDTO;
import com.backend.dtos.ResetPasswordRequestDTO;
import com.backend.dtos.UserRegisterRequestDTO;
import com.backend.dtos.UserResponseDTO;
import com.backend.dtos.UserUpdateRequestDTO;

public interface UserService {

    UserResponseDTO registerUser(UserRegisterRequestDTO request);
    
    void verifyEmail(String token);

    void forgotPassword(ForgotPasswordRequestDTO request);

    void resetPassword(ResetPasswordRequestDTO request);

    UserResponseDTO getUserById(Long userId);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO request);

    void deleteUser(Long userId);
    
    UserResponseDTO getCurrentUser();
}