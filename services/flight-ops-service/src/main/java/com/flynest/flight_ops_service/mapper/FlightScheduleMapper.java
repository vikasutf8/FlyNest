package com.flynest.flight_ops_service.mapper;


import com.flynest.flight_ops_service.model.Flight;
import com.flynest.flight_ops_service.model.FlightSchedule;
import com.flynest.payload.request.FlightScheduleRequest;
import com.flynest.payload.response.AirportResponse;
import com.flynest.payload.response.FlightScheduleResponse;

import java.time.*;

/**
 * FlightScheduleMapper
 * --------------------------------------------------
 * Responsibilities:
 * - Entity ↔ DTO conversion (Request/Response)
 * - Duration & operating days formatting
 * - Safe enrichment with external data
 */
public class FlightScheduleMapper {

    private FlightScheduleMapper() {}

    // =========================================================
    // REQUEST → ENTITY (CREATE)
    // =========================================================

    public static FlightSchedule toEntity(FlightScheduleRequest request, Flight flight) {
        if (request == null) return null;

        return FlightSchedule.builder()
                .flight(flight)

                .departureAirportId(flight.getDepartureAirportId())
                .arrivalAirportId(flight.getArrivalAirportId())
                .departureTime(toInstant(request.getDepartureTime()))
                .arrivalTime(toInstant(request.getArrivalTime()))
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .operatingDays(request.getOperatingDays())
                .isActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE)
                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (FULL ENRICHMENT)
    // =========================================================

    public static FlightScheduleResponse toResponse(
            FlightSchedule schedule,
            AirportResponse departureAirport,
            AirportResponse arrivalAirport
    ) {
        if (schedule == null) return null;


        return FlightScheduleResponse.builder()
                .id(schedule.getId())
                .flightId(schedule.getFlight().getId())
                .flightNumber(schedule.getFlight().getFlightNumber())

                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureTime(schedule.getDepartureTime() != null ? LocalDate.from(schedule.getDepartureTime()) : null)
                .arrivalTime(schedule.getArrivalTime() != null ? LocalDate.from(schedule.getArrivalTime()) : null)

                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .operatingDays(schedule.getOperatingDays())
                .isActive(schedule.getIsActive())
                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (LIGHT VERSION)
    // =========================================================


    // =========================================================
    // LIST MAPPING
    // =========================================================



    // =========================================================
    // UPDATE EXISTING ENTITY
    // =========================================================

    public static void updateEntity(FlightSchedule schedule, FlightScheduleRequest request) {
        if (schedule == null || request == null) return;

        // Do NOT update flightId or airlineId

        if (request.getDepartureTime() != null) {
            schedule.setDepartureTime(toInstant(request.getDepartureTime()));
        }
        if (request.getArrivalTime() != null) {
            schedule.setArrivalTime(toInstant(request.getArrivalTime()));
        }
        if (request.getStartDate() != null) {
            schedule.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            schedule.setEndDate(request.getEndDate());
        }
        if (request.getOperatingDays() != null && !request.getOperatingDays().isEmpty()) {
            schedule.setOperatingDays(request.getOperatingDays());
        }
        if (request.getIsActive() != null) {
            schedule.setIsActive(request.getIsActive());
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private static java.time.Instant toInstant(LocalTime lt) {
        if (lt == null) return null;
        return lt.atDate(java.time.LocalDate.of(1970, 1, 1)).toInstant(ZoneOffset.UTC);
    }

    private static LocalTime toLocalTime(java.time.Instant instant) {
        if (instant == null) return null;
        return LocalTime.ofInstant(instant, ZoneOffset.UTC);
    }





    private static Duration calculateDuration(java.time.Instant departure, java.time.Instant arrival) {
        if (departure == null || arrival == null) return null;
        return Duration.between(departure, arrival);
    }

    private static String formatDuration(Duration d) {
        if (d == null) return null;
        if (d.isNegative()) d = d.negated();
        long hours = d.toHours();
        long minutes = d.toMinutesPart();
        return hours + "h " + minutes + "m";
    }
}

