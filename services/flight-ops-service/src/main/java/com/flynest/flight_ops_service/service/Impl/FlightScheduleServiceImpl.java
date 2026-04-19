package com.flynest.flight_ops_service.service.Impl;

import com.flynest.flight_ops_service.repository.FlightScheduleRepository;
import com.flynest.flight_ops_service.service.FlightScheduleService;
import com.flynest.payload.request.FlightScheduleRequest;
import com.flynest.payload.response.FlightScheduleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;

    @Override
    public FlightScheduleResponse createFlightSchedule(FlightScheduleRequest flightScheduleRequest, Long userId) {
        return null;
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) {
        return null;
    }

    @Override
    public List<FlightScheduleResponse> getAllFlightSchedulesByAirline(Long userId) {
        return List.of();
    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest flightScheduleRequest) {
        return null;
    }

    @Override
    public void deleteFlightSchedule(Long id) {

    }
}
