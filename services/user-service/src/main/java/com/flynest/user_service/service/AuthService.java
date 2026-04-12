package com.flynest.user_service.service;

import com.flynest.payload.dto.UserDto;
import com.flynest.payload.request.LoginRequest;
import com.flynest.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
    AuthResponse register(UserDto request);
}
