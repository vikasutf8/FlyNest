package com.flynest.airline_core_service.service.impl;

import com.flynest.airline_core_service.mapper.AirlineMapper;
import com.flynest.airline_core_service.model.Airline;
import com.flynest.airline_core_service.repository.AirlineRepository;
import com.flynest.airline_core_service.service.AirlineService;
import com.flynest.enums.AirlineStatus;
import com.flynest.payload.request.AirlineRequest;
import com.flynest.payload.response.AirlineDropdownItem;
import com.flynest.payload.response.AirlineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional()
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;


    @Override
    public AirlineResponse createAirline(AirlineRequest request, Long ownerId) {
        Airline savedAirline =airlineRepository.save(AirlineMapper.toEntity(request, ownerId));
        return AirlineMapper.toResponse(savedAirline);
    }

    @Override
    public AirlineResponse getAirlineByOwnerId(Long ownerId) {
        Airline airline =airlineRepository.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Airline not found for ownerId: " + ownerId));
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public AirlineResponse getAirlineById(Long airlineId) {
        Airline airline =airlineRepository.findById(airlineId).orElseThrow(() -> new RuntimeException("Airline not found for id: " + airlineId));
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
        return airlineRepository.findAll(pageable).map(AirlineMapper::toResponse);
    }

    @Override
    public AirlineResponse updateAirline(Long airlineId, AirlineRequest request, Long ownerId) {
        Airline airlineToUpdate;
//        if(airlineRepository.existsById(airlineId)) {
//           airlineToUpdate = airlineRepository.findById(airlineId).orElseThrow(() -> new RuntimeException("Airline not found for id: " + airlineId));
//        } else {
//            throw new RuntimeException("Airline not found for id: " + airlineId);
//        }

        if(airlineRepository.existByOwnerId(ownerId)){
            airlineToUpdate =airlineRepository.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Airline not found for ownerId: " + ownerId));
        }else{
            throw new RuntimeException("Airline not found for id: " + ownerId);
        }
        AirlineMapper.updateEntityFromRequest(request,airlineToUpdate);
        airlineToUpdate =airlineRepository.save(airlineToUpdate);

        return AirlineMapper.toResponse(airlineToUpdate);
    }

    @Override
    public void deleteAirline(Long airlineId, Long ownerId) {
        if(airlineRepository.existsById(airlineId) && airlineRepository.existByOwnerId(ownerId)) {
            airlineRepository.deleteById(airlineId);
        } else {
            throw new RuntimeException("Airline not found for id: " + airlineId);
        }
    }

    @Override
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) {
        Airline airlineToUpdate = airlineRepository.findById(airlineId).
                orElseThrow(() -> new RuntimeException("Airline not found for id: " + airlineId));
        airlineToUpdate.setStatus(status);
        airlineToUpdate = airlineRepository.save(airlineToUpdate);
        return AirlineMapper.toResponse(airlineToUpdate);
    }

    @Override
    public List<AirlineDropdownItem> getAirlineDropdown() {

        return airlineRepository.findByStatus(AirlineStatus.ACTIVE.name()).stream().map(
                airline -> AirlineDropdownItem.builder()
                .id(airline.getId())
                .name(airline.getName()).iataCode(airline.getIataCode()).icaoCode(airline.getIcaoCode()).logoUrl(airline.getLogoUrl())
                .build()).toList();
//        return List.of();
    }
}
