package com.flynest.flight_ops_service.service;

import com.flynest.flight_ops_service.model.FlightSchedule;
import com.flynest.payload.request.FlightScheduleRequest;
import com.flynest.payload.response.FlightScheduleResponse;

import java.util.List;

public interface FlightScheduleService {

    FlightScheduleResponse createFlightSchedule(FlightScheduleRequest flightScheduleRequest, Long userId);
    FlightScheduleResponse getFlightScheduleById(Long id);
    List<FlightScheduleResponse> getAllFlightSchedulesByAirline(Long userId);
//    List<FlightScheduleResponse> getAllFlightSchedulesByFlightId(Long flightId);


    FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest flightScheduleRequest);
    void deleteFlightSchedule(Long id);
}
