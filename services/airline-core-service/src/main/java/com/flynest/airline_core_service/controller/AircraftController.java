package com.flynest.airline_core_service.controller;

import com.flynest.airline_core_service.service.AircraftService;
import com.flynest.payload.request.AircraftRequest;
import com.flynest.payload.response.AircraftResponse;
import com.flynest.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/aircrafts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<AircraftResponse>> createAircraft(
            @Validated @RequestBody AircraftRequest request) {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to create aircraft with code: {}", request.getCode());
        AircraftResponse response = aircraftService.createAircraft(request, ownerId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Aircraft created successfully", response));
    }

    @PutMapping
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<AircraftResponse>> updateAircraft(
            @Validated @RequestBody AircraftRequest request) {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to update aircraft by owner: {}", ownerId);
        AircraftResponse response = aircraftService.updateAircraft(request, ownerId);
        return ResponseEntity.ok(ApiResponse.success("Aircraft updated successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AircraftResponse>> getAircraftById(
            @PathVariable Long id) {
        log.info("REST request to get aircraft by id: {}", id);
        AircraftResponse response = aircraftService.getAircraftById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<List<AircraftResponse>>> getAllAircrafts() {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to get all aircrafts for owner: {}", ownerId);
        List<AircraftResponse> aircrafts = aircraftService.getAllAircrafts(ownerId);
        return ResponseEntity.ok(ApiResponse.success(aircrafts));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        Long ownerId = getAuthenticatedUserId();
        log.info("REST request to delete aircraft id: {}", id);
        aircraftService.deleteAircraft(id, ownerId);
        return ResponseEntity.noContent().build();
    }

    // ── SecurityContext Helper ────────────────────────────────────────────────

    private Long getAuthenticatedUserId() {
//        return (Long) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getCredentials();          // userId stored by JwtAuthenticationFilter
        return null;
    }
}
