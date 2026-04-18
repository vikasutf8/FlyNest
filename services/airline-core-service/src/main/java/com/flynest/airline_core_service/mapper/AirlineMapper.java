package com.flynest.airline_core_service.mapper;

// ── AirlineMapper ────────────────────────────────────────────────────────────

import com.flynest.airline_core_service.model.Airline;
import com.flynest.emabbedable.Support;
import com.flynest.enums.AirlineStatus;
import com.flynest.payload.request.AirlineRequest;
import com.flynest.payload.response.AirlineResponse;

public class AirlineMapper {

    // AirlineRequest ──► Airline entity
    public static Airline toEntity(AirlineRequest request, Long ownerId) {

//
//        if (request.getStatus() == null) {
//            request.setStatus(AirlineStatus.ACTIVE); // Default status
//        }

        Airline airline= Airline.builder()
                .iataCode(request.getIataCode())
                .icaoCode(request.getIcaoCode())
                .name(request.getName())
                .alias(request.getAlias())
                .logoUrl(request.getLogoUrl())
                .website(request.getWebsite())
                .status(request.getStatus())
                .alliances(request.getAlliances())
                .headquartersCityId(request.getHeadquartersCityId())
                .ownerId(ownerId)
                .build();

        if(request.getSupportEmail() != null || request.getSupportPhone() != null || request.getSupportHours() != null) {
             airline.setSupport(Support.builder()
                    .email(request.getSupportEmail())
                    .phone(request.getSupportPhone())
                    .hours(request.getSupportHours())
                    .build()
             );
        }
        return airline;
    }

    // Airline entity ──► AirlineResponse
    public static AirlineResponse toResponse(Airline airline) {
        return AirlineResponse.builder()
                .id(airline.getId())
                .iataCode(airline.getIataCode())
                .icaoCode(airline.getIcaoCode())
                .name(airline.getName())
                .alias(airline.getAlias())
                .logoUrl(airline.getLogoUrl())
                .website(airline.getWebsite())
                .status(airline.getStatus())
                .alliances(airline.getAlliances())
                .support(airline.getSupport())
                .ownerId(airline.getOwnerId())

//                .headquartersCity(.toResponse(airline.getHeadquartersCityId())) // Assuming a method to fetch city response by ID
                .updatedById(airline.getUpdatedById())
                .createdAt(airline.getCreatedAt())
                .updatedAt(airline.getUpdatedAt())
                .build();
    }

    // Partial update — only overwrite non-null fields
    public static void updateEntityFromRequest(AirlineRequest request, Airline airline) {
        if (request.getIataCode()           != null) airline.setIataCode(request.getIataCode());
        if (request.getIcaoCode()           != null) airline.setIcaoCode(request.getIcaoCode());
        if (request.getName()               != null) airline.setName(request.getName());
        if (request.getAlias()              != null) airline.setAlias(request.getAlias());
        if (request.getLogoUrl()            != null) airline.setLogoUrl(request.getLogoUrl());
        if (request.getWebsite()            != null) airline.setWebsite(request.getWebsite());
        if (request.getStatus()             != null) airline.setStatus(request.getStatus());
        if (request.getAlliances()           != null) airline.setAlliances(request.getAlliances());
        if (request.getHeadquartersCityId() != null) airline.setHeadquartersCityId(request.getHeadquartersCityId());
        if(airline.getSupport() ==null){
            airline.setSupport(new Support());
        }
        airline.getSupport().setEmail(request.getSupportEmail() != null ? request.getSupportEmail() : airline.getSupport().getEmail());
        airline.getSupport().setPhone(request.getSupportPhone() != null ? request.getSupportPhone() : airline.getSupport().getPhone());
        airline.getSupport().setHours(request.getSupportHours() != null ? request.getSupportHours() : airline.getSupport().getHours());

    }
}
