package com.flynest.airline_core_service.controller;

// ── AirlineController ────────────────────────────────────────────────────────

import com.flynest.airline_core_service.service.AirlineService;
import com.flynest.enums.AirlineStatus;
import com.flynest.payload.request.AirlineRequest;
import com.flynest.payload.response.AirlineDropdownItem;
import com.flynest.payload.response.AirlineResponse;
import com.flynest.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airlines")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AirlineController {

    private final AirlineService airlineService;

    // ── ROLE_AIRLINE_ADMIN: create their airline ─────────────────────────────

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<AirlineResponse>> createAirline(
            @Validated @RequestBody AirlineRequest request) {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to create airline by owner: {}", ownerId);
        AirlineResponse response = airlineService.createAirline(request, ownerId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Airline created successfully", response));
    }

    @PutMapping("/{airlineId}")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<AirlineResponse>> updateAirline(
            @PathVariable Long airlineId,
            @Validated @RequestBody AirlineRequest request) {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to update airline id: {}", airlineId);
        AirlineResponse response = airlineService.updateAirline(airlineId, request, ownerId);
        return ResponseEntity.ok(ApiResponse.success("Airline updated successfully", response));
    }

    @DeleteMapping("/{airlineId}")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long airlineId) {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to delete airline id: {}", airlineId);
        airlineService.deleteAirline(airlineId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-airline")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<AirlineResponse>> getMyAirline() {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to get airline for owner: {}", ownerId);
        AirlineResponse response = airlineService.getAirlineByOwnerId(ownerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ── ROLE_SYSTEM_ADMIN: status management ─────────────────────────────────

    @PatchMapping("/{airlineId}/status")
//    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<AirlineResponse>> changeStatus(
            @PathVariable Long airlineId,
            @RequestParam AirlineStatus status) {
        log.info("REST request to change status of airline id: {} to {}", airlineId, status);
        AirlineResponse response = airlineService.changeStatusByAdmin(airlineId, status);
        return ResponseEntity.ok(ApiResponse.success("Airline status updated", response));
    }

    // ── Public / Authenticated ────────────────────────────────────────────────

    @GetMapping("/{airlineId}")
    public ResponseEntity<ApiResponse<AirlineResponse>> getAirlineById(
            @PathVariable Long airlineId) {
        log.info("REST request to get airline by id: {}", airlineId);
        AirlineResponse response = airlineService.getAirlineById(airlineId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Page<AirlineResponse>>> getAllAirlines(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        log.info("REST request to get all airlines");
        Page<AirlineResponse> airlines = airlineService.getAllAirlines(pageable);
        return ResponseEntity.ok(ApiResponse.success(airlines));
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponse<List<AirlineDropdownItem>>> getDropdown() {
        log.info("REST request to get airline dropdown");
        List<AirlineDropdownItem> items = airlineService.getAirlineDropdown();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    // ── SecurityContext Helper ────────────────────────────────────────────────

    private Long getAuthenticatedUserId() {
        // reads userId claim stored in JWT by JwtAuthenticationFilter
//        Object principal = SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//
//        if (principal instanceof UserDetails userDetails) {
//            // fetch id from DB using email — or store userId directly in JWT claims
//            throw new ForbiddenException("Override this with userId from JWT claims");
//        }
//        return (Long) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getCredentials();
        return null;
    }
}
