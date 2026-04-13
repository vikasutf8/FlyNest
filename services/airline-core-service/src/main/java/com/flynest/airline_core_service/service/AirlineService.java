package com.flynest.airline_core_service.service;

import com.flynest.enums.AirlineStatus;
import com.flynest.payload.request.AirlineRequest;
import com.flynest.payload.response.AirlineDropdownItem;
import com.flynest.payload.response.AirlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AirlineService {

    AirlineResponse createAirline(AirlineRequest request, Long ownerId);
    AirlineResponse getAirlineByOwnerId(Long ownerId);
    AirlineResponse getAirlineById(Long airlineId);
    Page<AirlineResponse> getAllAirlines(Pageable pageable);
    AirlineResponse updateAirline(Long airlineId, AirlineRequest request, Long ownerId);
    void deleteAirline(Long airlineId, Long ownerId);


    // only system admin can change status
    AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status);


    List<AirlineDropdownItem> getAirlineDropdown();
}
