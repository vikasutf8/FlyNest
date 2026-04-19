package com.flynest.flight_ops_service.model;



import jakarta.persistence.*;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FlightSchedule Entity
 * -----------------------------------
 * Represents recurring schedule for a flight (weekly/monthly patterns).
 *
 * Design Notes:
 * - Defines when a flight operates (start/end dates, days of week)
 * - Time slots are defined at schedule level for consistency
 * - Keeps ManyToOne relationship to Flight
 * - Stores airport references as loose IDs (microservice-friendly)
 */
@Entity
@Table(
        name = "flight_schedules",
        indexes = {
                @Index(name = "idx_fs_flight", columnList = "flight_id"),
                @Index(name = "idx_fs_airline", columnList = "airline_id"),
                @Index(name = "idx_fs_start_date", columnList = "start_date"),
                @Index(name = "idx_fs_end_date", columnList = "end_date"),
                @Index(name = "idx_fs_is_active", columnList = "is_active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FlightSchedule {

    // =========================================================
    // PRIMARY KEY
    // =========================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // RELATIONSHIPS
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    // =========================================================
    // OWNERSHIP / LOOSE REFERENCES
    // =========================================================

    /**
     * Airline ID (denormalized for query performance)
     */

    @Column(name = "airline_id", nullable = false)
    private Long airlineId;

    /**
     * Departure airport ID (external service reference)
     */

    @Column(name = "departure_airport_id", nullable = false)
    private Long departureAirportId;

    /**
     * Arrival airport ID (external service reference)
     */

    @Column(name = "arrival_airport_id", nullable = false)
    private Long arrivalAirportId;

    // =========================================================
    // TIMING
    // =========================================================

    /**
     * Scheduled departure time (UTC) - time of day only
     */

    @Column(name = "departure_time", nullable = false)
    private Instant departureTime;

    /**
     * Scheduled arrival time (UTC) - time of day only
     */
    @NotNull
    @Column(name = "arrival_time", nullable = false)
    private Instant arrivalTime;

    // =========================================================
    // VALIDITY PERIOD
    // =========================================================

    /**
     * Schedule becomes effective on this date (inclusive)
     */

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Schedule expires on this date (inclusive)
     */

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // =========================================================
    // RECURRING PATTERN
    // =========================================================
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> operatingDays;
    // =========================================================
    // STATUS
    // =========================================================

    /**
     * Whether this schedule is currently active/in-use
     */

    @Column(name = "is_active", nullable = false)
    private Boolean isActive=true;

    // =========================================================
    // AUDIT
    // =========================================================

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    // =========================================================
    // BUSINESS METHODS
    // =========================================================

    /**
     * Check if this schedule is currently valid (within date range and active)
     */
    public boolean isCurrentlyValid() {
        if (!isActive) return false;
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    /**
     * Check if schedule operates on given day of week
     * @param dow DayOfWeek (MONDAY=1, SUNDAY=7)
     * @return true if flight operates on this day
     */
    public boolean operatesOn(DayOfWeek dow) {
        if (operatingDays == null || dow == null) return false;
        int dayValue = dow.getValue(); // 1=MON, 7=SUN
        return operatingDays.contains(String.valueOf(dayValue));
    }

    /**
     * Get operating days as formatted string for display
     * @return e.g. "MON, WED, FRI"
     */
    @Transient
    public String getFormattedOperatingDays() {
        if (operatingDays == null || operatingDays.isEmpty()) return "";
        String[] days = {"", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        StringBuilder result = new StringBuilder();
        for (String dayStr : operatingDays.split(",")) {
            try {
                int dayNum = Integer.parseInt(dayStr.trim());
                if (dayNum >= 1 && dayNum <= 7) {
                    if (!result.isEmpty()) result.append(", ");
                    result.append(days[dayNum]);
                }
            } catch (NumberFormatException e) {
                // Skip invalid values
            }
        }
        return result.toString();
    }

    // =========================================================
    // LIFECYCLE CALLBACKS
    // =========================================================

    @PrePersist
    @PreUpdate
    private void validateAndNormalize() {
        // Ensure departure and arrival are different
        if (departureAirportId != null && departureAirportId.equals(arrivalAirportId)) {
            throw new IllegalArgumentException("Departure and arrival airports must be different");
        }

        // Ensure end date is not before start date
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Ensure departure time is before arrival time
        if (departureTime != null && arrivalTime != null &&
                departureTime.isAfter(arrivalTime)) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }

        // Default values
        if (isActive == null) {
            isActive = Boolean.TRUE;
        }
    }
}

