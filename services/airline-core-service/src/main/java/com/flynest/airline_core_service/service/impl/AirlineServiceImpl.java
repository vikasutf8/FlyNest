package com.flynest.airline_core_service.service.impl;

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
        return null;
    }

    @Override
    public AirlineResponse getAirlineByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public AirlineResponse getAirlineById(Long airlineId) {
        return null;
    }

    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
        return null;
    }

    @Override
    public AirlineResponse updateAirline(Long airlineId, AirlineRequest request, Long ownerId) {
        return null;
    }

    @Override
    public void deleteAirline(Long airlineId, Long ownerId) {

    }

    @Override
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) {
        return null;
    }

    @Override
    public List<AirlineDropdownItem> getAirlineDropdown() {
        return List.of();
    }
}
