package com.flynest.flight_ops_service.service;

import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.response.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FlightInstanceService {

    FlightInstanceResponse getFlightInstanceById(Long id);
    FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest flightInstanceRequest);
    FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest flightInstanceRequest);
    void deleteFlightInstance(Long id);

    Page<FlightInstanceResponse> getFlightInstancesByAirlineId(
            Long airlineId,
            Long departureAirportId,
            Long arrivalAirportId,
            Long onDate,

            Long flightId, Pageable pageable);
}
