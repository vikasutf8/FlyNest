package com.flynest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightScheduleResponse {

    private Long id;
    private Long flightId;

    // ─── Flight Details ───
    private String flightNumber;

    // ─── Route ───
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    private LocalDate departureTime; // ISO 8601 format
    private LocalDate arrivalTime;   // ISO 8601 format

    private LocalDate startDate; // Schedule start date
    private LocalDate endDate;   // Schedule end date

    private List<DayOfWeek> operatingDays;

    private Boolean isActive;

}
