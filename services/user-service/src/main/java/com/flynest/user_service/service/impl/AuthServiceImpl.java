package com.flynest.user_service.service.impl;

import com.flynest.enums.UserRole;
import com.flynest.payload.dto.UserDto;
import com.flynest.payload.request.LoginRequest;
import com.flynest.payload.response.AuthResponse;
import com.flynest.user_service.mapper.UserMapper;
import com.flynest.user_service.model.User;
import com.flynest.user_service.repository.AuthRepository;
import com.flynest.user_service.security.JwtService;
import com.flynest.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional()
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) throws Exception {

                String email    = request.getEmail();
                String password = request.getPassword();
                log.debug("Login attempt for email: {}", email);

                User user = authRepository.findByEmailIgnoreCase(email)
                        .orElseThrow(() ->
                                new Exception("No account found with email: " + email));

                if (!passwordEncoder.matches(password, user.getPassword())) {
                    throw new BadCredentialsException("Invalid password");
                }

                user.setLastLogined(LocalDateTime.now());
                authRepository.save(user);

                String token = jwtService.generateToken(user);
                log.info("User logged in successfully: {}", user.getId());

                return UserMapper.toAuthResponse(user, token, "Login Successful");
    }

    @Override
    public AuthResponse register(UserDto request) throws Exception {
        log.debug("Registering user with email: {}", request.getEmail());
//ForbiddenException
        if (request.getRole() == UserRole.ROLE_SYSTEM_ADMIN) {
            throw new Exception("ROLE_SYSTEM_ADMIN cannot be assigned via registration");
        }
//        DuplicateResourceException
        if (authRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new Exception(
                    "User already exists with email: " + request.getEmail());
        }

        String encoded  = passwordEncoder.encode(request.getPassword());
        User   saved    = authRepository.save(UserMapper.toEntity(request, encoded));
        String token    = jwtService.generateToken(saved);

        log.info("User registered successfully with id: {}", saved.getId());

        return UserMapper.toAuthResponse(saved, token, "Registration Successful");
    }
}
