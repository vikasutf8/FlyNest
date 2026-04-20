package com.flynest.payload.response;

import com.flynest.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * FlightInstanceResponse
 * -----------------------------------
 * Represents a single scheduled occurrence of a flight with enriched data.
 *
 * Includes:
 * - Flight metadata (number, status)
 * - Airline information
 * - Aircraft details
 * - Route (departure/arrival airports)
 * - Scheduling & timing
 * - Capacity information
 * - Booking rules
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstanceResponse {

    // ─── Identity ───
    private Long id;
    private Long flightId;

    // ─── Flight Details ───
    private String flightNumber;

    // ─── Airline Info ───
    private Long airlineId;
    private String airlineName;
    private String airlineLogo;

    // ─── Aircraft Info ───
    private Long aircraftId;
    private String aircraftModel;
    private String aircraftCode;

    // ─── Route ───
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    // ─── Timing ───
    private Instant departureTime;
    private Instant arrivalTime;
    private String formattedDuration;

    // ─── Capacity ───
    private Integer totalSeats;
    private Integer availableSeats;

    // ─── Status ───
    private FlightStatus status;

    // ─── Booking Rules ───
    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;
    private Boolean isActive;

    // ─── Operational Info ───
    private String terminal;
    private String gate;
    private Long version;
}
