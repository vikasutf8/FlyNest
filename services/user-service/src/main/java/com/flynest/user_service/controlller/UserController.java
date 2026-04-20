package com.flynest.user_service.controlller;

// ── UserController ───────────────────────────────────────────────────────────

import com.flynest.payload.dto.UserDto;
import com.flynest.payload.response.ApiResponse;
import com.flynest.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) throws Exception {
        log.info("REST request to get user by id: {}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(
            @PathVariable  String email) throws Exception {
        log.info("REST request to get user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        log.info("REST request to get all users");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // ── Controller — no @RequestHeader needed at all
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getProfile() throws Exception {
        log.info("REST request to get current user profile");
        UserDto user = userService.getProfile();
        return ResponseEntity.ok(ApiResponse.success(user));
    }

}