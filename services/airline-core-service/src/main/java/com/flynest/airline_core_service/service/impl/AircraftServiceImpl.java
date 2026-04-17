package com.flynest.airline_core_service.service.impl;

import com.flynest.airline_core_service.repository.AircraftRepository;
import com.flynest.airline_core_service.service.AircraftService;
import com.flynest.payload.request.AircraftRequest;
import com.flynest.payload.response.AircraftResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;

    @Override
    public AircraftResponse getAircraftById(Long id) {
        return null;
    }

    @Override
    public AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        return null;
    }

    @Override
    public AircraftResponse updateAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        return null;
    }

    @Override
    public List<AircraftResponse> getAllAircrafts(Long ownerId) {
        return List.of();
    }

    @Override
    public void deleteAircraft(Long id, Long ownerId) {

    }
}
