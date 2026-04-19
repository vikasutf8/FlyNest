package com.flynest.flight_ops_service.mapper;


import com.flynest.enums.FlightStatus;
import com.flynest.flight_ops_service.model.FlightInstance;
import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.response.AircraftResponse;
import com.flynest.payload.response.AirlineResponse;
import com.flynest.payload.response.AirportResponse;
import com.flynest.payload.response.FlightInstanceResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FlightInstanceMapper
 * --------------------------------------------------
 * Responsibilities:
 * - Entity ↔ DTO conversion (Request/Response)
 * - Duration formatting
 * - Safe enrichment with external service data
 *
 * NOTE:
 * - External data (airline, aircraft, airport) should be passed in
 * - Mapper should NOT call repositories/services
 */
public class FlightInstanceMapper {

    private FlightInstanceMapper() {}

    // =========================================================
    // REQUEST → ENTITY (CREATE)
    // =========================================================

    public static FlightInstance toEntity(FlightInstanceRequest request) {
        if (request == null) return null;

        return FlightInstance.builder()
//                .flightId(request.getFlightId())
                .airlineId(request.getAirlineId())
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())
                .scheduleId(request.getScheduleId())
                .departureTime(toInstant(request.getDepartureTime()))
                .arrivalTime(toInstant(request.getArrivalTime()))
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getAvailableSeats())
                .status(request.getStatus() != null ? request.getStatus() : FlightStatus.SCHEDULED)
                .minAdvanceBookingDays(request.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(request.getMaxAdvanceBookingDays())
                .isActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE)
                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (FULL ENRICHMENT)
    // =========================================================

    public static FlightInstanceResponse toResponse(
            FlightInstance instance,
            String flightNumber,
            AirlineResponse airline,
            AircraftResponse aircraft,
            AirportResponse departureAirport,
            AirportResponse arrivalAirport
    ) {
        if (instance == null) return null;

        return FlightInstanceResponse.builder()
                .id(instance.getId())
//                .flightId(instance.getFlightId())
                .flightNumber(flightNumber)
                .airlineId(instance.getAirlineId())
                .airlineName(airline != null ? airline.getName() : null)
                .airlineLogo(airline != null ? airline.getLogoUrl() : null)
                .aircraftId(aircraft != null ? aircraft.getId() : null)
                .aircraftModel(aircraft != null ? aircraft.getModel() : null)
                .aircraftCode(aircraft != null ? aircraft.getCode() : null)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureTime(instance.getDepartureTime())
                .arrivalTime(instance.getArrivalTime())
                .formattedDuration(instance.getFormatedDuration())
                .totalSeats(instance.getTotalSeats())
                .availableSeats(instance.getAvailableSeats())
                .status(instance.getStatus())
                .minAdvanceBookingDays(instance.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(instance.getMaxAdvanceBookingDays())
                .isActive(instance.getIsActive())
//                .terminal(instance.get)
//                .gate(instance.getGate())
//                .version(instance.getVersion())
                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (LIGHT VERSION)
    // =========================================================

    public static FlightInstanceResponse toBasicResponse(FlightInstance instance) {
        if (instance == null) return null;

        return FlightInstanceResponse.builder()
                .id(instance.getId())
//                .flightId(instance.getFlightId())
                .airlineId(instance.getAirlineId())
                .departureTime(instance.getDepartureTime())
                .arrivalTime(instance.getArrivalTime())
                .formattedDuration(instance.getFormatedDuration())
                .totalSeats(instance.getTotalSeats())
                .availableSeats(instance.getAvailableSeats())
                .status(instance.getStatus())
                .minAdvanceBookingDays(instance.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(instance.getMaxAdvanceBookingDays())
                .isActive(instance.getIsActive())
//                .terminal(instance.getTerminal())
//                .gate(instance.getGate())
//                .version(instance.getVersion())
                .build();
    }

    // =========================================================
    // LIST MAPPING
    // =========================================================

    public static List<FlightInstanceResponse> toResponseList(List<FlightInstance> instances) {
        if (instances == null) return List.of();

        return instances.stream()
                .filter(Objects::nonNull)
                .map(FlightInstanceMapper::toBasicResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // UPDATE EXISTING ENTITY
    // =========================================================

    public static void updateEntity(FlightInstance instance, FlightInstanceRequest request) {
        if (instance == null || request == null) return;

        if (request.getDepartureAirportId() != null) {
            instance.setDepartureAirportId(request.getDepartureAirportId());
        }
        if (request.getArrivalAirportId() != null) {
            instance.setArrivalAirportId(request.getArrivalAirportId());
        }
        if (request.getScheduleId() != null) {
            instance.setScheduleId(request.getScheduleId());
        }
        if (request.getDepartureTime() != null) {
            instance.setDepartureTime(toInstant(request.getDepartureTime()));
        }
        if (request.getArrivalTime() != null) {
            instance.setArrivalTime(toInstant(request.getArrivalTime()));
        }
        if (request.getTotalSeats() != null) {
            instance.setTotalSeats(request.getTotalSeats());
        }
        if (request.getAvailableSeats() != null) {
            instance.setAvailableSeats(request.getAvailableSeats());
        }
        if (request.getStatus() != null) {
            instance.setStatus(request.getStatus());
        }
        if (request.getMinAdvanceBookingDays() != null) {
            instance.setMinAdvanceBookingDays(request.getMinAdvanceBookingDays());
        }
        if (request.getMaxAdvanceBookingDays() != null) {
            instance.setMaxAdvanceBookingDays(request.getMaxAdvanceBookingDays());
        }
        if (request.getIsActive() != null) {
            instance.setIsActive(request.getIsActive());
        }
    }

    // =========================================================
    // STATUS CHANGE
    // =========================================================

    public static void updateStatus(FlightInstance instance, FlightStatus newStatus) {
        if (instance == null || newStatus == null) return;
        instance.setStatus(newStatus);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private static Instant toInstant(LocalDateTime ldt) {
        if (ldt == null) return null;
        return ldt.toInstant(ZoneOffset.UTC);
    }
}

