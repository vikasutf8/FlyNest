package com.flynest.payload.request;

import com.flynest.enums.FlightStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * FlightRequest
 * -----------------------------------
 * Used for CREATE / UPDATE operations
 *
 * Validation Layers:
 * - Field-level (JSR-380)
 * - Business-level (@AssertTrue)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {

    @NotBlank(message = "Flight number is required")
    @Size(max = 20, message = "Flight number must be <= 20 characters")
    private String flightNumber;

    @NotNull(message = "Airline ID is required")
    @Positive(message = "Airline ID must be positive")
    private Long airlineId;

    @NotNull(message = "Aircraft ID is required")
    @Positive(message = "Aircraft ID must be positive")
    private Long aircraftId;

    @NotNull(message = "Departure airport ID is required")
    @Positive(message = "Departure airport ID must be positive")
    private Long departureAirportId;

    @NotNull(message = "Arrival airport ID is required")
    @Positive(message = "Arrival airport ID must be positive")
    private Long arrivalAirportId;

    // ───── Timing (CRITICAL) ─────

    @NotNull(message = "Departure time is required")
    private Instant departureTime;

    @NotNull(message = "Arrival time is required")
    private Instant arrivalTime;

    private Instant estimatedDepartureTime;
    private Instant estimatedArrivalTime;

    // Optional for update scenarios
    private FlightStatus status;

    // =========================================================
    // BUSINESS VALIDATIONS
    // =========================================================

    /**
     * Departure & arrival airport cannot be same
     */
    @AssertTrue(message = "Departure and arrival airports must be different")
    public boolean isAirportsValid() {
        if (departureAirportId == null || arrivalAirportId == null) return true;
        return !departureAirportId.equals(arrivalAirportId);
    }

    /**
     * Arrival must be after departure
     */
    @AssertTrue(message = "Arrival time must be after departure time")
    public boolean isTimeValid() {
        if (departureTime == null || arrivalTime == null) return true;
        return arrivalTime.isAfter(departureTime);
    }

    /**
     * Estimated times must be logical if provided
     */
    @AssertTrue(message = "Estimated arrival must be after estimated departure")
    public boolean isEstimatedTimeValid() {
        if (estimatedDepartureTime == null || estimatedArrivalTime == null) return true;
        return estimatedArrivalTime.isAfter(estimatedDepartureTime);
    }
}
