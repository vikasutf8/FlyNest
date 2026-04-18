package com.flynest.airline_core_service.service.impl;

import com.flynest.airline_core_service.mapper.AircraftMapper;
import com.flynest.airline_core_service.model.Aircraft;
import com.flynest.airline_core_service.model.Airline;
import com.flynest.airline_core_service.repository.AircraftRepository;
import com.flynest.airline_core_service.repository.AirlineRepository;
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
    private final AirlineRepository airlineRepository;

    @Override
    public AircraftResponse getAircraftById(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id).orElseThrow(() -> new RuntimeException("Aircraft not found for id: " + id));
        return AircraftMapper.toResponse(aircraft);
    }

    @Override
    public AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Airline not found for ownerId: " + ownerId));
        Aircraft aircraft = AircraftMapper.toEntity(aircraftRequest, airline);
        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        return AircraftMapper.toResponse(savedAircraft);
    }

    @Override
    public AircraftResponse updateAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Airline not found for ownerId: " + ownerId));
        Aircraft aircraftToUpdate = aircraftRepository.findByCode(aircraftRequest.getCode()).orElseThrow(() -> new RuntimeException("Aircraft not found for id: " + aircraftRequest.getCode()));
        AircraftMapper.updateEntity(aircraftToUpdate, aircraftRequest);
        Aircraft updatedAircraft = aircraftRepository.save(aircraftToUpdate);
        return AircraftMapper.toResponse(updatedAircraft);
    }

    @Override
    public List<AircraftResponse> getAllAircrafts(Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Airline not found for ownerId: " + ownerId));
        List<Aircraft> aircrafts = aircraftRepository.findByAirline(airline);
        return aircrafts.stream().map(AircraftMapper::toResponse).toList();
    }

    @Override
    public void deleteAircraft(Long id, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Airline not found for ownerId: " + ownerId));
        Aircraft aircraftToDelete = aircraftRepository.findById(id).orElseThrow(() -> new RuntimeException("Aircraft not found for id: " + id));
        if (!aircraftToDelete.getAirline().equals(airline)) {
            throw new RuntimeException("Unauthorized to delete this aircraft");
        }
        aircraftRepository.delete(aircraftToDelete);
    }
}
