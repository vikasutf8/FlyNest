package com.flynest.airline_core_service.mapper;



import com.flynest.airline_core_service.model.Aircraft;
import com.flynest.airline_core_service.model.Airline;
import com.flynest.payload.request.AircraftRequest;
import com.flynest.payload.response.AircraftResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AircraftMapper
 * -------------------------
 * Responsibility:
 * - Convert Entity ↔ DTO
 *
 * Best Practices:
 * - Stateless → use static methods
 * - Null-safe mapping
 * - Never trigger lazy loading unintentionally
 * - Derived fields handled here (NOT in controller)
 */
public class AircraftMapper {

    private AircraftMapper() {
        // prevent instantiation
    }

    // =========================================================
    // ENTITY → RESPONSE
    // =========================================================



    // =========================================================
    // ENTITY → RESPONSE (without airport enrichment)
    // =========================================================

    public static AircraftResponse toResponse(Aircraft entity) {
        if (entity == null) return null;

        return AircraftResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .model(entity.getModel())
                .manufacturer(entity.getManufacturer())

                .seatingCapacity(entity.getSeatingCapacity())

                .economySeats(entity.getEconomySeats())
                .premiumEconomySeats(entity.getPremiumEconomySeats())
                .businessSeats(entity.getBusinessSeats())
                .firstClassSeats(entity.getFirstClassSeats())

                .rangeKm(entity.getRangeKm())
                .cruisingSpeedKmh(entity.getCruisingSpeedKmh())
                .maxAltitudeFt(entity.getMaxAltiudeFt())
                .yearOfManufacture(entity.getYearOfManufacture())

                .registrationDate(entity.getRegistrationDate())
                .nextMaintenanceDate(entity.getNextMaintenanceDate())

                .status(entity.getStatus())
                .isAvailable(entity.isAvailable())

                .currentAirportId(entity.getCurrentAirportId())
                .currentCityAirport(entity.getAirline().getHeadquartersCityId())
                .currentAirportName(entity.getAirline() != null ? entity.getAirline().getName() : null)
                .currentAirportCode(entity.getAirline() != null ? entity.getAirline().getIataCode() : null)

                // ───── Airport enrichment (optional) ─────

                // ───── Derived fields ─────
                .totalSeats(entity.getTotalSeats())
                .isOperational(entity.isOperational())
                .requiresMaintenance(entity.requiresMaintenance())

                // ───── Audit ─────
                .createdAt(toInstant(entity.getCreatedAt()))
                .updatedAt(toInstant(entity.getUpdatedAt()))

                .build();
    }

    // =========================================================
    // LIST MAPPING
    // =========================================================

    public static List<AircraftResponse> toResponseList(List<Aircraft> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(AircraftMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // REQUEST → ENTITY (CREATE)
    // =========================================================

    public static Aircraft toEntity(AircraftRequest request, Airline airline) {
        if (request == null) return null;

        return Aircraft.builder()
                .code(request.getCode())
                .model(request.getModel())
                .manufacturer(request.getManufacturer())

                .seatingCapacity(request.getSeatingCapacity())

                .economySeats(request.getEconomySeats())
                .premiumEconomySeats(request.getPremiumEconomySeats())
                .businessSeats(request.getBusinessSeats())
                .firstClassSeats(request.getFirstClassSeats())

                .rangeKm(request.getRangeKm())
                .cruisingSpeedKmh(request.getCruisingSpeedKmh())
                .maxAltiudeFt(request.getMaxAltitudeFt())
                .yearOfManufacture(request.getYearOfManufacture())

                .registrationDate(request.getRegistrationDate())
                .nextMaintenanceDate(request.getNextMaintenanceDate())

                .status(request.getStatus())
                .currentAirportId(request.getCurrentAirportId())
                .airline(airline)

                // Defaults (important)
                .isAvailable(true)
                // status should be set in service (business logic)
                .build();
    }

    // =========================================================
    // UPDATE EXISTING ENTITY
    // =========================================================

    public static void updateEntity(Aircraft entity, AircraftRequest request) {
        if (entity == null || request == null) return;

        entity.setCode(request.getCode());
        entity.setModel(request.getModel());
        entity.setManufacturer(request.getManufacturer());

        entity.setSeatingCapacity(request.getSeatingCapacity());

        entity.setEconomySeats(request.getEconomySeats());
        entity.setPremiumEconomySeats(request.getPremiumEconomySeats());
        entity.setBusinessSeats(request.getBusinessSeats());
        entity.setFirstClassSeats(request.getFirstClassSeats());

        entity.setRangeKm(request.getRangeKm());
        entity.setCruisingSpeedKmh(request.getCruisingSpeedKmh());
        entity.setMaxAltiudeFt(request.getMaxAltitudeFt());
        entity.setYearOfManufacture(request.getYearOfManufacture());

        entity.setRegistrationDate(request.getRegistrationDate());
        entity.setNextMaintenanceDate(request.getNextMaintenanceDate());

        entity.setCurrentAirportId(request.getCurrentAirportId());

        // NOTE:
        // - Do NOT update status blindly
        // - Do NOT update availability blindly
        // These should be controlled via business rules in service layer
    }

    // =========================================================
    // HELPER
    // =========================================================

    private static Instant toInstant(java.time.LocalDateTime time) {
        return time != null ? time.toInstant(ZoneOffset.UTC) : null;
    }
}