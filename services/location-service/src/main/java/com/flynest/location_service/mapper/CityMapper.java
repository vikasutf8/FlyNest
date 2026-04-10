package com.flynest.location_service.mapper;

import com.flynest.location_service.model.City;
import com.flynest.payload.request.CityRequest;
import com.flynest.payload.response.CityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityMapper {

    public static City toEntity(CityRequest request) {
        return City.builder()
                .name(request.getName())
                .countryCode(request.getCountryCode())
                .country(request.getCountry())
                .cityCode(request.getCityCode())
                .regionCode(request.getRegionCode())
                .timeZoneId(request.getTimeZoneOffset())
                .build();
    }

    public static CityResponse toDto(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .countryCode(city.getCountryCode())
                .country(city.getCountry())
                .cityCode(city.getCityCode())
                .regionCode(city.getRegionCode())
                .timeZoneOffest(city.getTimeZoneId())
                .build();
    }

    public  static void updateEntityFromRequest(CityRequest request, City city) {
        if (request.getName()        != null) city.setName(request.getName());
        if (request.getCountryCode() != null) city.setCountryCode(request.getCountryCode());
        if (request.getCountry()     != null) city.setCountry(request.getCountry());
        if (request.getCityCode()    != null) city.setCityCode(request.getCityCode());
        if (request.getRegionCode()  != null) city.setRegionCode(request.getRegionCode());
        if (request.getTimeZoneOffset()  != null) city.setTimeZoneId(request.getTimeZoneOffset());
    }
}