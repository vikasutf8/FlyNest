package com.flynest.airline_core_service.service;

import com.flynest.payload.request.AircraftRequest;
import com.flynest.payload.response.AircraftResponse;

import java.util.List;

public interface AircraftService {

    AircraftResponse getAircraftById(Long id);
    AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId);
    AircraftResponse updateAircraft(AircraftRequest aircraftRequest, Long ownerId);
    List<AircraftResponse> getAllAircrafts(Long ownerId);

    void deleteAircraft(Long id, Long ownerId);
}
