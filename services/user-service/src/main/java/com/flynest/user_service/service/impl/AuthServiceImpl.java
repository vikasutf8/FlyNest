package com.flynest.user_service.service.impl;

import com.flynest.enums.UserRole;
import com.flynest.payload.dto.UserDto;
import com.flynest.payload.request.LoginRequest;
import com.flynest.payload.response.AuthResponse;
import com.flynest.user_service.model.User;
import com.flynest.user_service.repository.AuthRepository;
import com.flynest.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) throws Exception {
        String email =request.getEmail();
        String password =request.getPassword();
        log.debug("Login attempt for email: {}", email);
//        ResourceNotFoundException
         User user = authRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new Exception("No account found with email: " + email));
//BadCredentialsException
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }

        // ── Update last login timestamp
        user.setLastLogined(LocalDateTime.now());
        authRepository.save(user);

//        String token = jwtService.generateToken(user);
//
//        log.info("User logged in successfully: {}", user.getId());
//        return UserMapper.toAuthResponse(user, token);

        return null;
    }

    @Override
    public AuthResponse register(UserDto request) throws Exception {
        log.debug("Registering user with email: {}", request.getEmail());

        // ── Guard: ROLE_SYSTEM_ADMIN cannot be assigned via API
        //ForbiddenException
        if (request.getRole() == UserRole.ROLE_SYSTEM_ADMIN) {
            throw new Exception("ROLE_SYSTEM_ADMIN cannot be assigned via registration");
        }

        // ── Guard: duplicate email
        //DuplicateResourceException
        if (authRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new Exception(
                    "User already exists with email: " + request.getEmail());
        }

        String encoded = passwordEncoder.encode(request.getPassword());


        return null;
    }
}
