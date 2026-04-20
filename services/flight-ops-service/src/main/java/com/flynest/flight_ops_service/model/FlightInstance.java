package com.flynest.flight_ops_service.model;

import com.flynest.enums.FlightStatus;
import jakarta.persistence.*;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.Instant;

/**
 * FlightInstance Entity
 * -----------------------------------
 * Represents a specific occurrence of a Flight on a particular schedule/date.
 *
 * Microservice-friendly design:
 * - Keeps only local relationship to Flight.
 * - Stores other service references as IDs (airportId, scheduleId, etc.).
 */
@Entity
@Table(
		name = "flight_instances",
		uniqueConstraints = {
				// A flight cannot have two instances with same scheduled departure time
				@UniqueConstraint(name = "uk_flight_sched_departure", columnNames = {"flight_id", "departure_time"})
		},
		indexes = {
				@Index(name = "idx_fi_flight", columnList = "flight_id"),
				@Index(name = "idx_fi_airline", columnList = "airline_id"),
				@Index(name = "idx_fi_departure_airport", columnList = "departure_airport_id"),
				@Index(name = "idx_fi_arrival_airport", columnList = "arrival_airport_id"),
				@Index(name = "idx_fi_departure_time", columnList = "departure_time"),
				@Index(name = "idx_fi_status", columnList = "status")
		}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FlightInstance {

	// =========================================================
	// PRIMARY KEY
	// =========================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// =========================================================
	// RELATIONS
	// =========================================================
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight flight;

	// =========================================================
	// OWNERSHIP / LOOSE REFERENCES
	// =========================================================

	/** Airline owning this instance (denormalized for query performance) */

	@Column(name = "airline_id", nullable = false)
	private Long airlineId;


	@Column(name = "departure_airport_id", nullable = false)
	private Long departureAirportId;


	@Column(name = "arrival_airport_id", nullable = false)
	private Long arrivalAirportId;

	/** External scheduling entity reference (if any) */
	@Column(name = "schedule_id",nullable = false)
	private Long scheduleId;

	// =========================================================
	// TIMING (UTC)
	// =========================================================

	/** Scheduled departure time (UTC) */

	@Column(name = "departure_time", nullable = false)
	private Instant departureTime;

	/** Scheduled arrival time (UTC) */

	@Column(name = "arrival_time", nullable = false)
	private Instant arrivalTime;

	// =========================================================
	// CAPACITY
	// =========================================================

	@Column(name = "total_seats", nullable = false)
	private Integer totalSeats;

	@Column(name = "available_seats", nullable = false)
	private Integer availableSeats;

	// =========================================================
	// STATUS / RULES
	// =========================================================


	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private FlightStatus status;

	/**
	 * Minimum number of days before departure when booking opens.
	 * Example: 1 means you can book starting 1 day in advance.
	 */

	@Column(name = "min_advance_booking_days")
	private Integer minAdvanceBookingDays;

	/**
	 * Maximum number of days before departure allowed for booking.
	 * Example: 180 means you can book at most 180 days in advance.
	 */

	@Column(name = "max_advance_booking_days")
	private Integer maxAdvanceBookingDays;


	@Column(name = "is_active", nullable = false,columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean isActive ;

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
	// DERIVED HELPERS
	// =========================================================

	/**
	 * @return duration between scheduled departure and arrival formatted as "Xh Ym".
	 */
	@Transient
	public String getFormatedDuration() {
		if (departureTime == null || arrivalTime == null) return null;
		Duration d = Duration.between(departureTime, arrivalTime);
		if (d.isNegative()) d = d.negated();
		long hours = d.toHours();
		long minutes = d.toMinutesPart();
		return hours + "h " + minutes + "m";
	}

	@PrePersist
	@PreUpdate
	private void normalizeAndValidate() {
		// Keep available seats bounded.
		if (totalSeats != null && availableSeats != null) {
			if (availableSeats > totalSeats) {
				availableSeats = totalSeats;
			}
		}

		// Default values
		if (status == null) {
			status = FlightStatus.SCHEDULED;
		}
		if (isActive == null) {
			isActive = Boolean.TRUE;
		}

		// Basic route validation
		if (departureAirportId != null && departureAirportId.equals(arrivalAirportId)) {
			throw new IllegalArgumentException("Departure and arrival airports must be different");
		}
	}
}
