package com.flynest.user_service.mapper;

// ── UserMapper ───────────────────────────────────────────────────────────────

import com.flynest.payload.dto.UserDto;
import com.flynest.payload.response.AuthResponse;
import com.flynest.user_service.model.User;

public class UserMapper {

    public static User toEntity(UserDto dto, String encodedPassword) {
        return User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(encodedPassword)          // never raw password
                .phone(dto.getPhone())
                .role(dto.getRole())
                .verified(false)
                .build();
    }

    public static UserDto toResponse(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .verified(user.isVerified())
                .lastLogined(user.getLastLogined())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static AuthResponse toAuthResponse(User user, String token, String title) {
        return AuthResponse.builder()
                .token(token)
                .title("Welcome back, " + user.getEmail() + "!")
                .message(title)
                .user(toResponse(user))
                .build();
    }
}
