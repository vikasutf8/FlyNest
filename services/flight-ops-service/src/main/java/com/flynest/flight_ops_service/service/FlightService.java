package com.flynest.flight_ops_service.service;

import com.flynest.enums.FlightStatus;
import com.flynest.payload.request.FlightRequest;
import com.flynest.payload.response.FlightResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlightService {
// airline id is replaced via user id(from userid have to fetch airline id) because only airline can create flight and user is associated with airline

    FlightResponse createFlight(Long airlineId, FlightRequest flightRequest);
    FlightResponse updateFlight(Long id , FlightRequest flightRequest);

    List<FlightResponse> getAllFlights();
    List<FlightResponse> getFlightByAirline(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable);

    FlightResponse getFlightById(Long id);
    void deleteFlight(Long id);
    FlightResponse changeStatus(Long id, FlightStatus newStatus);


}
