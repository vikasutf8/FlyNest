package com.flynest.location_service.service;

import com.flynest.location_service.model.Airport;
import com.flynest.payload.request.AirportRequest;
import com.flynest.payload.response.AirportResponse;

import java.util.List;

public interface AirportService {

    AirportResponse createAirport(AirportRequest request);
    AirportResponse updateAirport(Long id, AirportRequest request);
    List<AirportResponse> getAllAirports();
    AirportResponse getAirportById(Long id);
    void deleteAirport(Long id);

    List<AirportResponse> getAirportsByCityId(Long cityId);
}
