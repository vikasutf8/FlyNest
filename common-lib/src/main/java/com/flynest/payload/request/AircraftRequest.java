package com.flynest.payload.request;

import com.flynest.enums.AircraftStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftRequest {
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must be <= 20 characters")
    private String code;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    // Total seating capacity
    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Seating capacity must be at least 1")
    private Integer seatingCapacity;

    // Seat distribution
    @Min(value = 0, message = "Economy seats cannot be negative")
    private Integer economySeats;

    @Min(value = 0, message = "Premium economy seats cannot be negative")
    private Integer premiumEconomySeats;

    @Min(value = 0, message = "Business seats cannot be negative")
    private Integer businessSeats;

    @Min(value = 0, message = "First class seats cannot be negative")
    private Integer firstClassSeats;

    // Aircraft specifications
    @Positive(message = "Range must be positive")
    private Double rangeKm;

    @Positive(message = "Cruising speed must be positive")
    private Double cruisingSpeedKmh;

    @Min(value = 1000, message = "Altitude must be realistic")
    private Integer maxAltitudeFt;

    @Min(value = 1900, message = "Year must be valid")
    @Max(value = 2100, message = "Year must be realistic")
    private Integer yearOfManufacture;

    // Dates
    @PastOrPresent(message = "Registration date cannot be in future")
    private LocalDate registrationDate;

    @Future(message = "Next maintenance date must be in future")
    private LocalDate nextMaintenanceDate;


    private AircraftStatus status;

    // Foreign key reference
    private Long currentAirportId;
}
