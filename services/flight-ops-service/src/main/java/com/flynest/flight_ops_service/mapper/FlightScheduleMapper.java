package com.flynest.flight_ops_service.mapper;

package com.flynest.flight_ops_service.mapper;

import com.flynest.flight_ops_service.model.FlightSchedule;
import com.flynest.payload.request.FlightScheduleRequest;
import com.flynest.payload.response.AirportResponse;
import com.flynest.payload.response.FlightScheduleResponse;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static FlightSchedule toEntity(FlightScheduleRequest request, Long airlineId) {
        if (request == null) return null;

        String operatingDaysStr = formatOperatingDaysToString(request.getOperatingDays());

        return FlightSchedule.builder()
                .flightId(request.getFlightId())
                .airlineId(airlineId)
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())
                .departureTime(toInstant(request.getDepartureTime()))
                .arrivalTime(toInstant(request.getArrivalTime()))
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .operatingDays(operatingDaysStr)
                .isActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE)
                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (FULL ENRICHMENT)
    // =========================================================

    public static FlightScheduleResponse toResponse(
            FlightSchedule schedule,
            String flightNumber,
            AirportResponse departureAirport,
            AirportResponse arrivalAirport
    ) {
        if (schedule == null) return null;

        List<DayOfWeek> operatingDays = parseOperatingDaysFromString(schedule.getOperatingDays());
        Duration duration = calculateDuration(schedule.getDepartureTime(), schedule.getArrivalTime());

        return FlightScheduleResponse.builder()
                .id(schedule.getId())
                .flightId(schedule.getFlightId())
                .flightNumber(flightNumber)
                .airlineId(schedule.getAirlineId())
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureTime(toLocalTime(schedule.getDepartureTime()))
                .arrivalTime(toLocalTime(schedule.getArrivalTime()))
                .formattedDuration(formatDuration(duration))
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .operatingDays(operatingDays)
                .formattedOperatingDays(schedule.getFormattedOperatingDays())
                .isActive(schedule.getIsActive())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    // =========================================================
    // ENTITY → RESPONSE (LIGHT VERSION)
    // =========================================================

    public static FlightScheduleResponse toBasicResponse(FlightSchedule schedule) {
        if (schedule == null) return null;

        List<DayOfWeek> operatingDays = parseOperatingDaysFromString(schedule.getOperatingDays());
        Duration duration = calculateDuration(schedule.getDepartureTime(), schedule.getArrivalTime());

        return FlightScheduleResponse.builder()
                .id(schedule.getId())
                .flightId(schedule.getFlightId())
                .airlineId(schedule.getAirlineId())
                .departureTime(toLocalTime(schedule.getDepartureTime()))
                .arrivalTime(toLocalTime(schedule.getArrivalTime()))
                .formattedDuration(formatDuration(duration))
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .operatingDays(operatingDays)
                .formattedOperatingDays(schedule.getFormattedOperatingDays())
                .isActive(schedule.getIsActive())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    // =========================================================
    // LIST MAPPING
    // =========================================================

    public static List<FlightScheduleResponse> toResponseList(List<FlightSchedule> schedules) {
        if (schedules == null) return List.of();

        return schedules.stream()
                .filter(Objects::nonNull)
                .map(FlightScheduleMapper::toBasicResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // UPDATE EXISTING ENTITY
    // =========================================================

    public static void updateEntity(FlightSchedule schedule, FlightScheduleRequest request) {
        if (schedule == null || request == null) return;

        // Do NOT update flightId or airlineId
        if (request.getDepartureAirportId() != null) {
            schedule.setDepartureAirportId(request.getDepartureAirportId());
        }
        if (request.getArrivalAirportId() != null) {
            schedule.setArrivalAirportId(request.getArrivalAirportId());
        }
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
            schedule.setOperatingDays(formatOperatingDaysToString(request.getOperatingDays()));
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

    private static List<DayOfWeek> formatOperatingDaysToString(List<DayOfWeek> days) {
        if (days == null || days.isEmpty()) return "";
        return days.stream()
                .map(d -> String.valueOf(d.getValue()))
                .collect(Collectors.joining(","));
    }

    private static List<DayOfWeek> parseOperatingDaysFromString(String daysStr) {
        if (daysStr == null || daysStr.isEmpty()) return List.of();
        return daysStr.split(",")
                .stream()
                .map(String::trim)
                .map(Integer::parseInt)
                .map(DayOfWeek::of)
                .collect(Collectors.toList());
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

