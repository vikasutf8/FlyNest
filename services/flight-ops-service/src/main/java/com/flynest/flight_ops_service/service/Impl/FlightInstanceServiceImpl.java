package com.flynest.flight_ops_service.service.Impl;

import com.flynest.flight_ops_service.repository.FlightInstanceRepository;
import com.flynest.flight_ops_service.service.FlightInstanceService;
import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.response.FlightInstanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightInstanceRepository flightInstanceRepository;


    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) {
        return null;
    }

    @Override
    public FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest flightInstanceRequest) {
        return null;
    }

    @Override
    public FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest flightInstanceRequest) {
        return null;
    }

    @Override
    public void deleteFlightInstance(Long id) {

    }

    @Override
    public Page<FlightInstanceResponse> getFlightInstancesByAirlineId(Long airlineId, Long departureAirportId, Long arrivalAirportId, Long onDate, Long flightId, Pageable pageable) {
        return null;
    }
}
