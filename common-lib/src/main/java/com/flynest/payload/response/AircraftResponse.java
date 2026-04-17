package com.flynest.payload.response;

import com.flynest.enums.AircraftStatus;

import java.time.Instant;
import java.time.LocalDate;

public class AircraftResponse {
    private Long id;

    private String code;
    private String model;
    private String manufacturer;

    private Integer seatingCapacity;

    private Integer economySeats;
    private Integer premiumEconomySeats;
    private Integer businessSeats;
    private Integer firstClassSeats;

    private Double rangeKm;
    private Double cruisingSpeedKmh;
    private Integer maxAltitudeFt;
    private Integer yearOfManufacture;

    private LocalDate registrationDate;
    private LocalDate nextMaintenanceDate;

    private AircraftStatus status;
    private boolean isAvailable;

    private Long currentAirportId;

    private Long currentCityAirport;
    private String currentAirportName;
    private String currentAirportCode;

    private Integer totalSeats;
    private Boolean isOperational;
    private Boolean requiresMaintenance;

    private Instant createdAt;
    private Instant updatedAt;
}
