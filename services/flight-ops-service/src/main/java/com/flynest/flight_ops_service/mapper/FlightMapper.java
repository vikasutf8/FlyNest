package com.flynest.flight_ops_service.mapper;


import com.flynest.enums.FlightStatus;
import com.flynest.flight_ops_service.model.Flight;
import com.flynest.payload.request.FlightRequest;
import com.flynest.payload.response.AircraftResponse;
import com.flynest.payload.response.AirlineResponse;
import com.flynest.payload.response.AirportResponse;
import com.flynest.payload.response.FlightResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FlightMapper
 * --------------------------------------------------
 * Responsibilities:
 * - Entity ↔ DTO conversion
 * - Derived fields (price, seats)
 * - Safe enrichment (no lazy loading surprises)
 *
 * NOTE:
 * - External data (price, seat availability) should be passed in
 * - Mapper should NOT call repositories/services
 */
public class FlightMapper {

    private FlightMapper() {}

    // =========================================================
    // ENTITY → RESPONSE (FULL ENRICHMENT)
    // =========================================================

    public static FlightResponse toResponse(
            Flight flight,
            AirlineResponse airline,
            AircraftResponse aircraft,
            AirportResponse departureAirport,
            AirportResponse arrivalAirport,
            Double lowestPrice,
            Integer totalAvailableSeats
    ) {
        if (flight == null) return null;

        return FlightResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())

                .airline(airline)
                .aircraft(aircraft)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)

                // ─── Time Conversion ───
//                .departureTime(toLocalDateTime(flight.getDepartureTime()))
//                .arrivalTime(toLocalDateTime(flight.getArrivalTime()))

                .status(flight.getStatus())

                // ─── Derived / External ───
                .lowestPrice(lowestPrice)
                .totalAvailableSeats(totalAvailableSeats)

                // ─── Audit ───
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())

                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (LIGHT VERSION)
    // =========================================================
//have to change toResponse
    public static FlightResponse toBasicResponse(Flight flight) {
        if (flight == null) return null;

        return FlightResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
//                .departureTime(toLocalDateTime(flight.getDepartureTime()))
//                .arrivalTime(toLocalDateTime(flight.getArrivalTime()))
                .status(flight.getStatus())
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }

    // =========================================================
    // LIST MAPPING
    // =========================================================

    public static List<FlightResponse> toResponseList(List<Flight> flights) {
        if (flights == null) return List.of();

        return flights.stream()
                .filter(Objects::nonNull)
                .map(FlightMapper::toBasicResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // REQUEST → ENTITY (CREATE)
    // =========================================================
//TODO:later we know where its comes
    public static Flight toEntity(FlightRequest request) {
        if (request == null) return null;

        return Flight.builder()
                .flightNumber(request.getFlightNumber())
                .airlineId(request.getAirlineId())
                .aircraftId(request.getAircraftId())
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())

                // ─── Time Conversion ───
//                .departureTime(request.getDepartureTime())
//                .arrivalTime(request.getArrivalTime())

                // Default status
                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : FlightStatus.SCHEDULED
                )
                .build();
    }

    // =========================================================
    // UPDATE EXISTING ENTITY
    // =========================================================

    public static void updateEntity(Flight flight, FlightRequest request) {
        if (flight == null || request == null) return;

        flight.setFlightNumber(request.getFlightNumber());
        flight.setAircraftId(request.getAircraftId());
        flight.setDepartureAirportId(request.getDepartureAirportId());
        flight.setArrivalAirportId(request.getArrivalAirportId());

//        flight.setDepartureTime(request.getDepartureTime());
//        flight.setArrivalTime(request.getArrivalTime());

        // ⚠️ Status update should be controlled separately
        if (request.getStatus() != null) {
            flight.setStatus(request.getStatus());
        }
    }

    // =========================================================
    // STATUS CHANGE
    // =========================================================

    public static void updateStatus(Flight flight, FlightStatus newStatus) {
        if (flight == null || newStatus == null) return;

        // You SHOULD enforce transitions in service layer
        flight.setStatus(newStatus);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
