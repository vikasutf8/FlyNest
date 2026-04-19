package com.flynest.payload.request;

import com.flynest.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstanceRequest {


    private Long flightId;


    private Long airlineId;

    private Long departureAirportId;

    private Long arrivalAirportId;


    private Long scheduleId;

    private LocalDateTime departureTime;


    private LocalDateTime arrivalTime;


    private Integer totalSeats;

    private Integer availableSeats;

    private FlightStatus status;


    private Integer minAdvanceBookingDays;

    private Integer maxAdvanceBookingDays;

    private Boolean isActive ;

}
