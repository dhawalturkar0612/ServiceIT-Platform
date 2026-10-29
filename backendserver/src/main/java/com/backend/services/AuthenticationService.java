package com.backend.services;

import com.backend.dtos.LoginResponseDTO;
import com.backend.dtos.UserLoginRequestDTO;

public interface AuthenticationService {

    LoginResponseDTO authenticate(UserLoginRequestDTO request);

}