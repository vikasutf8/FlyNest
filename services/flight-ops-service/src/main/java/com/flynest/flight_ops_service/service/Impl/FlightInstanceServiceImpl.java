package com.flynest.flight_ops_service.service.Impl;

import com.flynest.flight_ops_service.mapper.FlightInstanceMapper;
import com.flynest.flight_ops_service.model.Flight;
import com.flynest.flight_ops_service.model.FlightInstance;
import com.flynest.flight_ops_service.repository.FlightInstanceRepository;
import com.flynest.flight_ops_service.repository.FlightRepository;
import com.flynest.flight_ops_service.service.FlightInstanceService;
import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.response.AircraftResponse;
import com.flynest.payload.response.FlightInstanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) {


        FlightInstance instance = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FlightInstance not found with id: " + id));
        return FlightInstanceMapper.toBasicResponse(instance);
    }

    @Override
    public FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest flightInstanceRequest) {

        Flight flight =flightRepository.findById(flightInstanceRequest.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + flightInstanceRequest.getFlightId()));

        AircraftResponse aircraftResponse = AircraftResponse.builder().id(flight.getAircraftId()).totalSeats(150).
                build();


        // Map request to entity
        FlightInstance instance = FlightInstanceMapper.toEntity(flightInstanceRequest, flight);
        instance.setAvailableSeats(aircraftResponse.getTotalSeats());
        instance.setTotalSeats(aircraftResponse.getTotalSeats());

        // Save
        FlightInstance saved = flightInstanceRepository.save(instance);

        //TODO: create seat instance
//        log.info("Created flight instance id={} flightId={} airlineId={}", saved.getId(), savedz.getFlightId(), saved.getAirlineId());
        return FlightInstanceMapper.toBasicResponse(saved);
    }

    @Override
    public FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest flightInstanceRequest) {

        FlightInstance existing = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FlightInstance not found with id: " + id));
        // Update entity
        FlightInstanceMapper.updateEntity(existing, flightInstanceRequest);

        // Save
        FlightInstance saved = flightInstanceRepository.save(existing);
//        log.info("Updated flight instance id={} flightId={}", saved.getId(), saved.getFlightId());
        return FlightInstanceMapper.toBasicResponse(saved);
    }

    @Override
    public void deleteFlightInstance(Long id) {

        if (!flightInstanceRepository.existsById(id)) {
            throw new IllegalArgumentException("FlightInstance not found with id: " + id);
        }
        flightInstanceRepository.deleteById(id);
        log.info("Deleted flight instance id={}", id);
    }

    @Override
    public Page<FlightInstanceResponse> getFlightInstancesByAirlineId(
            Long airlineId,
            Long departureAirportId,
            Long arrivalAirportId,
            LocalDate onDate,
            Long flightId,
            Pageable pageable
    ) {

//        todo: check airlineId
        LocalDateTime start = onDate != null? onDate.atStartOfDay() : null;
        LocalDateTime end = onDate != null? onDate.plusDays(1).atStartOfDay() : null;

        return flightInstanceRepository.findByAirlineId(airlineId, departureAirportId, arrivalAirportId, flightId, start, end, pageable)
                .map(FlightInstanceMapper::toBasicResponse);
    }
}
