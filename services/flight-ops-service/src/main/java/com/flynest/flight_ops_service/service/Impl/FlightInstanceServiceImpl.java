package com.flynest.flight_ops_service.service.Impl;

import com.flynest.flight_ops_service.mapper.FlightInstanceMapper;
import com.flynest.flight_ops_service.model.FlightInstance;
import com.flynest.flight_ops_service.repository.FlightInstanceRepository;
import com.flynest.flight_ops_service.service.FlightInstanceService;
import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.response.FlightInstanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightInstanceRepository flightInstanceRepository;

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be a positive number");
        }
        FlightInstance instance = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FlightInstance not found with id: " + id));
        return FlightInstanceMapper.toBasicResponse(instance);
    }

    @Override
    public FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest flightInstanceRequest) {


        // Validate required fields
        if (flightInstanceRequest.getFlightId() == null || flightInstanceRequest.getFlightId() <= 0) {
            throw new IllegalArgumentException("flightId must be a positive number");
        }
        if (flightInstanceRequest.getAirlineId() == null || flightInstanceRequest.getAirlineId() <= 0) {
            throw new IllegalArgumentException("airlineId must be a positive number");
        }

        // Validate airports are different
        if (Objects.equals(flightInstanceRequest.getDepartureAirportId(), flightInstanceRequest.getArrivalAirportId())) {
            throw new IllegalArgumentException("Departure and arrival airports must be different");
        }

        // Map request to entity
        FlightInstance instance = FlightInstanceMapper.toEntity(flightInstanceRequest);
        if (instance == null) {
            throw new IllegalStateException("Unable to map flight instance request to entity");
        }

        // Save
        FlightInstance saved = flightInstanceRepository.save(instance);
//        log.info("Created flight instance id={} flightId={} airlineId={}", saved.getId(), savedz.getFlightId(), saved.getAirlineId());
        return FlightInstanceMapper.toBasicResponse(saved);
    }

    @Override
    public FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest flightInstanceRequest) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be a positive number");
        }
        if (flightInstanceRequest == null) {
            throw new IllegalArgumentException("flightInstanceRequest must not be null");
        }

        // Fetch existing instance
        FlightInstance existing = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FlightInstance not found with id: " + id));

        // Validate airports are different if provided
        if (Objects.equals(flightInstanceRequest.getDepartureAirportId(), flightInstanceRequest.getArrivalAirportId())) {
            throw new IllegalArgumentException("Departure and arrival airports must be different");
        }

        // Update entity
        FlightInstanceMapper.updateEntity(existing, flightInstanceRequest);

        // Save
        FlightInstance saved = flightInstanceRepository.save(existing);
//        log.info("Updated flight instance id={} flightId={}", saved.getId(), saved.getFlightId());
        return FlightInstanceMapper.toBasicResponse(saved);
    }

    @Override
    public void deleteFlightInstance(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be a positive number");
        }
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
            Long onDate,
            Long flightId,
            Pageable pageable
    ) {
        if (airlineId == null || airlineId <= 0) {
            throw new IllegalArgumentException("airlineId must be a positive number");
        }

        // Query all instances for this airline
        var instances = flightInstanceRepository.findAll();

        // Filter by airlineId
        var filtered = instances.stream()
                .filter(fi -> Objects.equals(fi.getAirlineId(), airlineId))
                .toList();

        // Apply optional filters
        if (departureAirportId != null) {
            filtered = filtered.stream()
                    .filter(fi -> Objects.equals(fi.getDepartureAirportId(), departureAirportId))
                    .toList();
        }
        if (arrivalAirportId != null) {
            filtered = filtered.stream()
                    .filter(fi -> Objects.equals(fi.getArrivalAirportId(), arrivalAirportId))
                    .toList();
        }
        if (flightId != null) {
            filtered = filtered.stream()
                    .filter(fi -> Objects.equals(fi.getFlight().getId(), flightId))
                    .toList();
        }

        // Convert to responses
        var responses = filtered.stream()
                .map(FlightInstanceMapper::toBasicResponse)
                .toList();

        // Manual pagination
        int start = Math.min((int) pageable.getOffset(), responses.size());
        int end = Math.min(start + pageable.getPageSize(), responses.size());
        var pageContent = responses.subList(start, end);

        return new PageImpl<>(pageContent, pageable, responses.size());
    }
}
