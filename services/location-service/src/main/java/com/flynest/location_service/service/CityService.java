package com.flynest.location_service.service;

import com.flynest.payload.request.CityRequest;
import com.flynest.payload.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {

    CityResponse createCity(CityRequest cityRequest);
    CityResponse updateCity(Long id,CityRequest cityRequest);
    CityResponse getCityById(Long id);
    void deleteCity(Long id);
    Page<CityResponse> getAllCities(Pageable pageable);
    Page<CityResponse> searchCities(String keyword,Pageable pageable);
    Page<CityResponse> getCitiesByCountryCode(String countryCode,Pageable pageable);

    boolean cityExists(String cityCode);

}

