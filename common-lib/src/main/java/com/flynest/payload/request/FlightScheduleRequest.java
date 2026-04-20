package com.flynest.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * FlightScheduleRequest
 * -----------------------------------
 * Used for CREATE / UPDATE operations on flight schedules.
 *
 * Validation Layers:
 * - Field-level (JSR-380/Jakarta Validation)
 * - Business-level (@AssertTrue)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightScheduleRequest {

    // ─── Flight Reference ───
    @NotNull(message = "Flight ID is required")
    @Positive(message = "Flight ID must be positive")
    private Long flightId;

//    // ─── Route ───
//    @NotNull(message = "Departure airport ID is required")
//    @Positive(message = "Departure airport ID must be positive")
//    private Long departureAirportId;
//
//    @NotNull(message = "Arrival airport ID is required")
//    @Positive(message = "Arrival airport ID must be positive")
//    private Long arrivalAirportId;

    // ─── Timing (Time of Day) ───
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    // ─── Validity Period ───
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    // ─── Recurrence Pattern ───
    @NotNull(message = "Operating days is required")
    @NotEmpty(message = "Operating days cannot be empty")
    @Size(min = 1, max = 7, message = "Operating days must contain 1-7 days")
    private List<DayOfWeek> operatingDays;

    @NotNull(message = "isActive is required")
    private Boolean isActive;

    // =========================================================
    // BUSINESS VALIDATIONS
    // =========================================================

    /**
     * Departure and arrival airports must be different
     */
//    @AssertTrue(message = "Departure and arrival airports must be different")
//    public boolean isAirportsValid() {
//        if (departureAirportId == null || arrivalAirportId == null) return true;
//        return !departureAirportId.equals(arrivalAirportId);
//    }

    /**
     * Departure time must be before arrival time
     */
    @AssertTrue(message = "Departure time must be before arrival time")
    public boolean isTimesValid() {
        if (departureTime == null || arrivalTime == null) return true;
        return departureTime.isBefore(arrivalTime);
    }

    /**
     * End date must be on or after start date
     */
    @AssertTrue(message = "End date must be on or after start date")
    public boolean isDatesValid() {
        if (startDate == null || endDate == null) return true;
        return !endDate.isBefore(startDate);
    }

    /**
     * Start date should not be in the past (optional business rule)
     * Uncomment if needed:
     */
    @AssertTrue(message = "Start date cannot be in the past")
    public boolean isStartDateNotInPast() {
        if (startDate == null) return true;
        return !startDate.isBefore(LocalDate.now());
    }

    /**
     * Operating days list must not contain duplicates
     */
    @AssertTrue(message = "Operating days must not contain duplicates")
    public boolean isOperatingDaysUnique() {
        if (operatingDays == null || operatingDays.isEmpty()) return true;
        return operatingDays.size() == operatingDays.stream().distinct().count();
    }
}
