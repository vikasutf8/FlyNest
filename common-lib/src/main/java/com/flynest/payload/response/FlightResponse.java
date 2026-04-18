package com.flynest.payload.response;

;

import com.flynest.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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

    private Long airlineId;
    private Long aircraftId;

    private Long departureAirportId;
    private Long arrivalAirportId;

    // ───── Timing ─────
    private Instant departureTime;
    private Instant arrivalTime;

    private Instant estimatedDepartureTime;
    private Instant estimatedArrivalTime;

    // ───── Status ─────
    private FlightStatus status;

    // ───── Enrichment (optional) ─────
    private String departureAirportName;
    private String departureAirportCode;

    private String arrivalAirportName;
    private String arrivalAirportCode;

    // ───── Derived Fields ─────
    private Long durationMinutes;
    private Boolean isActive;
    private Boolean isDelayed;

    // ───── Audit ─────
    private Instant createdAt;
    private Instant updatedAt;
}
