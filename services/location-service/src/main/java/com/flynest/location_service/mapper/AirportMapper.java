package com.flynest.location_service.mapper;

import com.flynest.location_service.model.Airport;
import com.flynest.payload.request.AirportRequest;
import com.flynest.payload.response.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class AirportMapper {

    private final CityMapper cityMapper;

    public AirportMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    // AirportRequest ──► Airport entity
    public static Airport toEntity(AirportRequest request) {
        return Airport.builder()
                .iata(request.getIata())
                .name(request.getName())
                .timeZoneId(request.getTimeZoneId())
                .address(request.getAddress())
                .geoCode(request.getGeoCode())
                // city is NOT set here — always set manually in service
                .build();
    }

    // Airport entity ──► AirportResponse
    public static AirportResponse toDto(Airport airport) {
        return AirportResponse.builder()
                .id(airport.getId())
                .iata(airport.getIata())
                .name(airport.getName())
                .timeZoneId(airport.getTimeZoneId())
                .address(airport.getAddress())
                .geoCode(airport.getGeoCode())
                .city(CityMapper.toDto(airport.getCity()))  // City → CityResponse
                .build();
    }

    // Partial update — only overwrite non-null fields onto existing entity
    public static void updateEntityFromRequest(AirportRequest request, Airport airport) {
        if (request.getIata()       != null) airport.setIata(request.getIata());
        if (request.getName()       != null) airport.setName(request.getName());
        if (request.getTimeZoneId() != null) airport.setTimeZoneId(request.getTimeZoneId());
        if (request.getAddress()    != null) airport.setAddress(request.getAddress());
        if (request.getGeoCode()    != null) airport.setGeoCode(request.getGeoCode());
        // city is NOT touched here — handled separately in service
    }
}
