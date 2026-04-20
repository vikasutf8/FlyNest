package com.flynest.payload.response;

;

import com.flynest.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * FlightResponse
 * -----------------------------------
 * Used for API output
 *
 * Includes:
 * - Core data
 * - Derived fields
 * - Enriched info (optional)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {

    private Long id;

    private String flightNumber;

    private AirlineResponse airline;
    private AircraftResponse aircraft;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
//----------schudles
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private FlightStatus status;
    private Double lowestPrice;
    private Integer totalAvailableSeats;

    // ───── Audit ─────
    private Instant createdAt;
    private Instant updatedAt;
}
