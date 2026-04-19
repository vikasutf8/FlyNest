package com.flynest.flight_ops_service.service.Impl;

import com.flynest.enums.FlightStatus;
import com.flynest.flight_ops_service.mapper.FlightMapper;
import com.flynest.flight_ops_service.model.Flight;
import com.flynest.flight_ops_service.repository.FlightRepository;
import com.flynest.flight_ops_service.service.FlightService;
import com.flynest.payload.request.FlightRequest;
import com.flynest.payload.response.FlightResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    @Override
    public FlightResponse createFlight(Long airlineId, FlightRequest flightRequest) {


        // Source of truth: airlineId from method param (typically resolved from user context)
        // Request currently has airlineId as well (TODO in DTO); we ignore it to avoid spoofing.
        if (flightRepository.existByFlightNumber(flightRequest.getFlightNumber())) {
            throw new IllegalArgumentException("Flight number already exists: " + flightRequest.getFlightNumber());
        }



        Flight flight = FlightMapper.toEntity(flightRequest);

        flight.setAirlineId(airlineId);
        if (flight.getStatus() == null) {
            flight.setStatus(FlightStatus.SCHEDULED);
        }

        Flight saved = flightRepository.save(flight);
        log.info("Created flight id={} flightNumber={} airlineId={}", saved.getId(), saved.getFlightNumber(), saved.getAirlineId());
        return FlightMapper.toBasicResponse(saved);
    }

    @Override
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest) {
              Flight existing = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));

        // If flight number is being changed, ensure uniqueness.
        String incomingNumber = flightRequest.getFlightNumber();
        if (incomingNumber != null && !incomingNumber.equals(existing.getFlightNumber())) {
            if (flightRepository.existByFlightNumber(incomingNumber)) {
                throw new IllegalArgumentException("Flight number already exists: " + incomingNumber);
            }
        }

        if (Objects.equals(flightRequest.getDepartureAirportId(), flightRequest.getArrivalAirportId())) {
            throw new IllegalArgumentException("Departure and arrival airports must be different");
        }

        // airlineId should not be updated from request.
        FlightMapper.updateEntity(existing, flightRequest);

        Flight saved = flightRepository.save(existing);
        log.info("Updated flight id={} flightNumber={} airlineId={}", saved.getId(), saved.getFlightNumber(), saved.getAirlineId());
        return FlightMapper.toBasicResponse(saved);
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        return FlightMapper.toResponseList(flightRepository.findAll());
    }

    @Override
    public List<FlightResponse> getFlightByAirline(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {

        // Repository only supports filtering by airlineId with pagination.
        // Apply optional airport filters in-memory until dedicated query methods are added.
        List<Flight> flights = flightRepository.findByAirlineId(airlineId, pageable).getContent();

        if (departureAirportId != null) {
            flights = flights.stream()
                    .filter(f -> Objects.equals(f.getDepartureAirportId(), departureAirportId))
                    .toList();
        }
        if (arrivalAirportId != null) {
            flights = flights.stream()
                    .filter(f -> Objects.equals(f.getArrivalAirportId(), arrivalAirportId))
                    .toList();
        }

        return FlightMapper.toResponseList(flights);
    }

    @Override
    public FlightResponse getFlightById(Long id) {

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));
        return FlightMapper.toBasicResponse(flight);
    }

    @Override
    public void deleteFlight(Long id) {

        if (!flightRepository.existsById(id)) {
            throw new IllegalArgumentException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
        log.info("Deleted flight id={}", id);
    }

    @Override
    public FlightResponse changeStatus(Long id, FlightStatus newStatus) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));

        FlightStatus oldStatus = flight.getStatus();
        FlightMapper.updateStatus(flight, newStatus);

        Flight saved = flightRepository.save(flight);
        log.info("Changed flight status id={} from {} to {}", saved.getId(), oldStatus, newStatus);
        return FlightMapper.toBasicResponse(saved);
    }
}
