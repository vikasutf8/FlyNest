package com.flynest.flight_ops_service.controller;


import com.flynest.enums.FlightStatus;
import com.flynest.flight_ops_service.service.FlightService;
import com.flynest.payload.request.FlightRequest;
import com.flynest.payload.response.ApiResponse;
import com.flynest.payload.response.FlightResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FlightController {

    private final FlightService flightService;

    // ── ROLE_AIRLINE_ADMIN: create flight ────────────────────────────────────

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> createFlight(
            @Validated @RequestBody FlightRequest request) {
        Long airlineId = getAirlineIdFromContext();
        log.info("REST request to create flight by airline: {}", airlineId);
        FlightResponse response = flightService.createFlight(airlineId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Flight created successfully", response));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> updateFlight(
            @PathVariable Long id,
            @Validated @RequestBody FlightRequest request) {
        log.info("REST request to update flight id: {}", id);
        FlightResponse response = flightService.updateFlight(id, request);
        return ResponseEntity.ok(ApiResponse.success("Flight updated successfully", response));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        log.info("REST request to delete flight id: {}", id);
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    // ── Status change: SYSTEM_ADMIN only ─────────────────────────────────────

    @PatchMapping("/{id}/status")
//    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> changeStatus(
            @PathVariable Long id,
            @RequestParam FlightStatus newStatus) {
        log.info("REST request to change status of flight id: {} to {}", id, newStatus);
        FlightResponse response = flightService.changeStatus(id, newStatus);
        return ResponseEntity.ok(ApiResponse.success("Flight status updated", response));
    }

    // ── Read: Authenticated ──────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlightById(
            @PathVariable Long id) {
        log.info("REST request to get flight by id: {}", id);
        FlightResponse response = flightService.getFlightById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getAllFlights() {
        log.info("REST request to get all flights");
        List<FlightResponse> flights = flightService.getAllFlights();
        return ResponseEntity.ok(ApiResponse.success(flights));
    }

    // ── Search: filter by airline + airports with pagination ─────────────────

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getFlightByAirline(
            @RequestParam Long airlineId,
            @RequestParam(required = false) Long departureAirportId,
            @RequestParam(required = false) Long arrivalAirportId,
            @PageableDefault(size = 10, sort = "departureTime", direction = Sort.Direction.ASC)
            Pageable pageable) {
        log.info("REST request to search flights for airline: {}, from: {}, to: {}",
                airlineId, departureAirportId, arrivalAirportId);
        List<FlightResponse> flights = flightService
                .getFlightByAirline(airlineId, departureAirportId, arrivalAirportId, pageable);
        return ResponseEntity.ok(ApiResponse.success(flights));
    }

    // ── SecurityContext Helpers ───────────────────────────────────────────────

    private Long getAuthenticatedUserId() {
//        return (Long) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getCredentials();
// userId stored by JwtAuthenticationFilter

        return null;
    }

    private Long getAirlineIdFromContext() {
        // userId from JWT → fetch airlineId in service layer
        // passed down so service can resolve airline ownership
        return getAuthenticatedUserId();
    }
}