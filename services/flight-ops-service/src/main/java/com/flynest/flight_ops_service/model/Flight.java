package com.flynest.flight_ops_service.model;


import com.flynest.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Flight Entity
 * -----------------------------------
 * Represents a scheduled or running flight.
 *
 * Design Notes:
 * - Uses loose references (IDs) for Airport & Aircraft → microservice friendly
 * - Indexed for fast lookups (flightNumber, airlineId)
 * - Audit fields handled via Spring Data JPA
 */
@Entity
@Table(
        name = "flights",
        indexes = {
                @Index(name = "idx_flight_number", columnList = "flight_number"),
                @Index(name = "idx_airline_id", columnList = "airline_id"),
                @Index(name = "idx_departure_airport", columnList = "departure_airport_id"),
                @Index(name = "idx_arrival_airport", columnList = "arrival_airport_id")
        }
)
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Flight {

    // =========================================================
    // PRIMARY KEY
    // =========================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // BASIC DETAILS
    // =========================================================
    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber; // e.g. "AI-202"

    @Column(name = "airline_id", nullable = false)
    private Long airlineId;

    @Column(name = "aircraft_id", nullable = false)
    private Long aircraftId;

    // =========================================================
    // ROUTING
    // =========================================================
    @Column(name = "departure_airport_id", nullable = false)
    private Long departureAirportId;

    @Column(name = "arrival_airport_id", nullable = false)
    private Long arrivalAirportId;

    // =========================================================
    // STATUS
    // =========================================================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlightStatus status;

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
     * Flight is active if it's not cancelled or completed
     */
    public boolean isActive() {
        return status != FlightStatus.CANCELLED &&
                status != FlightStatus.COMPLETED;
    }

    /**
     * Flight is in-progress (boarding → in air)
     */
    public boolean isInProgress() {
        return status == FlightStatus.BOARDING ||
                status == FlightStatus.DEPARTED ||
                status == FlightStatus.IN_AIR;
    }
}