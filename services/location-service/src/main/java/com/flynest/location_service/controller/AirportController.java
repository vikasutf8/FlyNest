package com.flynest.location_service.controller;

// ── AirportController ────────────────────────────────────────────────────────

import com.flynest.location_service.service.AirportService;
import com.flynest.payload.request.AirportRequest;
import com.flynest.payload.response.AirportResponse;
import com.flynest.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    public ResponseEntity<ApiResponse<AirportResponse>> createAirport(
            @Valid @RequestBody AirportRequest request) {
        log.info("REST request to create airport with IATA: {}", request.getIata());
        AirportResponse response = airportService.createAirport(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Airport created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> updateAirport(
            @PathVariable Long id,
            @Valid @RequestBody AirportRequest request) {
        log.info("REST request to update airport with id: {}", id);
        AirportResponse response = airportService.updateAirport(id, request);
        return ResponseEntity.ok(ApiResponse.success("Airport updated successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportById(
            @PathVariable Long id) {
        log.info("REST request to get airport by id: {}", id);
        AirportResponse response = airportService.getAirportById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AirportResponse>>> getAllAirports() {
        log.info("REST request to get all airports");
        List<AirportResponse> airports = airportService.getAllAirports();
        return ResponseEntity.ok(ApiResponse.success(airports));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        log.info("REST request to delete airport with id: {}", id);
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<List<AirportResponse>>> getAirportsByCityId(
            @PathVariable Long cityId) {
        log.info("REST request to get airports for city id: {}", cityId);
        List<AirportResponse> airports = airportService.getAirportsByCityId(cityId);
        return ResponseEntity.ok(ApiResponse.success(airports));
    }
}
