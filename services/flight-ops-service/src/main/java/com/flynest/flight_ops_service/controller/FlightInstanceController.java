package com.flynest.flight_ops_service.controller;

import com.flynest.flight_ops_service.service.FlightInstanceService;
import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.response.ApiResponse;
import com.flynest.payload.response.FlightInstanceResponse;
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

@RestController
@RequestMapping("/api/v1/flight-instances")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FlightInstanceController {

    private final FlightInstanceService flightInstanceService;

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> createFlightInstance(
            @Validated @RequestBody FlightInstanceRequest request) {
        Long userId = getAuthenticatedUserId();
        log.info("REST request to create flight instance by user: {}", userId);
        FlightInstanceResponse response = flightInstanceService.createFlightInstance(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Flight instance created successfully", response));
    }

    @PutMapping("/{id}")
//    @PreAuthorizeAuthorize("hasAuthorityuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> updateFlightInstance(
            @PathVariable Long id,
            @Validated @RequestBody FlightInstanceRequest request) {
        log.info("REST request to update flight instance id: {}", id);
        FlightInstanceResponse response = flightInstanceService.updateFlightInstance(id, request);
        return ResponseEntity.ok(ApiResponse.success("Flight instance updated successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> getFlightInstanceById(
            @PathVariable Long id) {
        log.info("REST request to get flight instance by id: {}", id);
        FlightInstanceResponse response = flightInstanceService.getFlightInstanceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<FlightInstanceResponse>>> getFlightInstancesByAirline(

            @RequestParam(required = false) Long airlineId,
            @RequestParam(required = false) Long departureAirportId,
            @RequestParam(required = false) Long arrivalAirportId,
            @RequestParam(required = false) Long onDate,           // epoch ms — e.g. 1712947200000
            @RequestParam(required = false) Long flightId,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("REST request to search flight instances — airline: {}, dep: {}, arr: {}, date: {}, flight: {}",
                airlineId, departureAirportId, arrivalAirportId, onDate, flightId);

        Page<FlightInstanceResponse> response = flightInstanceService.getFlightInstancesByAirlineId(
                airlineId, departureAirportId, arrivalAirportId, onDate, flightId, pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_AIRLINE_ADMIN')")
    public ResponseEntity<Void> deleteFlightInstance(@PathVariable Long id) {
        log.info("REST request to delete flight instance id: {}", id);
        flightInstanceService.deleteFlightInstance(id);
        return ResponseEntity.noContent().build();
    }

    // ── SecurityContext Helper ────────────────────────────────────────────────

    private Long getAuthenticatedUserId() {
//        return (Long) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getCredentials();

        return null;
    }
}
