package com.flynest.airline_core_service.model;

import com.flynest.enums.AircraftStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aircrafts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;                        // unique aircraft code e.g. "VT-ALX"

    @Column(nullable = false)
    private String model;                       // e.g. "Boeing 737-800"

    @Column(nullable = false)
    private String manufacturer;                // e.g. "Boeing", "Airbus"

    private Integer seatingCapacity;

    // ── Seating Configuration ─────────────────────────────────────────────────

    @Column(nullable = false)
    private Integer economySeats;

    @Column(nullable = false)
    private Integer premiumEconomySeats;

    @Column(nullable = false)
    private Integer businessSeats;

    @Column(nullable = false)
    private Integer firstClassSeats;

    // ── Aircraft Specs ────────────────────────────────────────────────────────
    private Double  rangeKm;                    // e.g. 5430.0
    private Double  cruisingSpeedKmh;           // e.g. 842.0
    private Integer maxAltiudeFt;
    private Integer yearOfManufacture;          // e.g. 2015

    private LocalDate registrationDate;

    private LocalDate nextMaintenanceDate;

    // ── Status & Availability ─────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AircraftStatus status;

    @Column(nullable = false)
    private boolean isAvailable;

    // ── Relations ─────────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;                    // many aircrafts → one airline

    @Column(name = "current_airport_id")
    private Long currentAirportId;              // loose ref — no hard FK to airport

    // ── Audit ─────────────────────────────────────────────────────────────────

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ── Derived / Business Logic Methods ─────────────────────────────────────

    /**
     * Returns total seat count across all cabin classes.
     * seatingCapacity is always computed — never stored — to stay consistent.
     */
    public int getTotalSeats() {
        return nullSafe(economySeats)
                + nullSafe(premiumEconomySeats)
                + nullSafe(businessSeats)
                + nullSafe(firstClassSeats);
    }

    /**
     * Aircraft is operational only when ACTIVE status AND marked available.
     */
    public boolean isOperational() {
        return AircraftStatus.ACTIVE.equals(this.status) && this.isAvailable;
    }

    /**
     * Returns true if next maintenance is due within the next 14 days.
     * Also returns true if maintenance date is already past (overdue).
     */
    public boolean requiresMaintenance() {
        if (this.nextMaintenanceDate == null) return false;
        LocalDate twoWeeksFromNow = LocalDate.now().plusWeeks(2);
        return !this.nextMaintenanceDate.isAfter(twoWeeksFromNow);
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    private int nullSafe(Integer value) {
        return value != null ? value : 0;
    }
}
