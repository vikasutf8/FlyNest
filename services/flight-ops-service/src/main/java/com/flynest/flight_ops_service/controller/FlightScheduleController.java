package com.flynest.flight_ops_service.controller;


import com.flynest.flight_ops_service.service.FlightScheduleService;
import com.flynest.payload.request.FlightScheduleRequest;
import com.flynest.payload.response.FlightScheduleResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flight-schedules")
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    // =============================
    // CREATE
    // =============================

    @PostMapping
    public ResponseEntity<FlightScheduleResponse> createFlightSchedule(
            @Validated @RequestBody FlightScheduleRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        if (userId == null) {
            userId = 1L; // TODO: Extract from authentication context
        }
        FlightScheduleResponse created = flightScheduleService.createFlightSchedule(request, userId);
        URI location = URI.create(String.format("/api/v1/flight-schedules/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    // =============================
    // READ
    // =============================

    @GetMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> getFlightScheduleById(@PathVariable  Long id) {
        return ResponseEntity.ok(flightScheduleService.getFlightScheduleById(id));
    }

    @GetMapping
    public ResponseEntity<List<FlightScheduleResponse>> getAllFlightSchedulesByAirline(
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        if (userId == null) {
            userId = 1L; // TODO: Extract from authentication context
        }
        return ResponseEntity.ok(flightScheduleService.getAllFlightSchedulesByAirline(userId));
    }

    // =============================
    // UPDATE
    // =============================

    @PutMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> updateFlightSchedule(
            @PathVariable  Long id,
            @Validated @RequestBody FlightScheduleRequest request
    ) {
        return ResponseEntity.ok(flightScheduleService.updateFlightSchedule(id, request));
    }

    // =============================
    // DELETE
    // =============================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlightSchedule(@PathVariable  Long id) {
        flightScheduleService.deleteFlightSchedule(id);
        return ResponseEntity.noContent().build();
    }
}

