package com.flynest.user_service.service.impl;

import com.flynest.payload.dto.UserDto;
import com.flynest.user_service.mapper.UserMapper;
import com.flynest.user_service.model.User;
import com.flynest.user_service.repository.UserRepository;
import com.flynest.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

// ── UserServiceImpl ──────────────────────────────────────────────────────────

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        @Override
        public UserDto getUserByEmail(String email) throws Exception {
            log.debug("Fetching user by email: {}", email);
            User user = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new Exception("User not found with email: " + email));
            return UserMapper.toResponse(user);
        }

        @Override
        public UserDto getUserById(Long id) throws Exception {
            log.debug("Fetching user by id: {}", id);
            User user = userRepository.findById(id)
                    .orElseThrow(() ->
                            new Exception("User not found with id: " + id));
            return UserMapper.toResponse(user);
        }

        @Override
        public List<UserDto> getAllUsers() {
            log.debug("Fetching all users");
            return userRepository.findAll()
                    .stream()
                    .map(UserMapper::toResponse)
                    .toList();
        }

    @Override
    public UserDto getProfile() throws Exception {
        // Spring Security already validated the token via JwtAuthenticationFilter
        // email is stored in SecurityContext — no need to re-parse the token
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();                         // getName() returns the email (subject)

        log.debug("Fetching profile for authenticated user: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new Exception("User not found with email: " + email));

        return UserMapper.toResponse(user);
    }
}
