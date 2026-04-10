package com.flynest.location_service.service.impl;

import com.flynest.location_service.Repository.CityRepository;
import com.flynest.location_service.mapper.CityMapper;
import com.flynest.location_service.model.City;
import com.flynest.location_service.service.CityService;
import com.flynest.payload.request.CityRequest;
import com.flynest.payload.response.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    @Override
    public CityResponse createCity(CityRequest cityRequest) {
       if(cityRepository.existsByCityCodeIgnoreCase(cityRequest.getCityCode())){;
           throw new IllegalArgumentException("City code already exists: " + cityRequest.getCityCode());
       }
       City citycreated =cityRepository.save(CityMapper.toEntity(cityRequest));
        return CityMapper.toDto(citycreated);
    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + id));
//        if (cityRequest.getCityCode() != null &&
//                !cityRequest.getCityCode().equalsIgnoreCase(city.getCityCode()) &&
//                cityRepository.existsByCityCodeIgnoreCase(cityRequest.getCityCode())) {
//            throw new IllegalArgumentException("City code already exists: " + cityRequest.getCityCode());
//        }
        CityMapper.updateEntityFromRequest(cityRequest, city);
        City updatedCity = cityRepository.save(city);
        return CityMapper.toDto(updatedCity);
    }

    @Override
    public CityResponse getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + id));
        return CityMapper.toDto(city);

    }

    @Override
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new IllegalArgumentException("City not found with id: " + id);
        }
        cityRepository.deleteById(id);
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(CityMapper::toDto);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {
        String searchKeyword = keyword.toLowerCase();
        return cityRepository.searchByKeyword(searchKeyword, pageable).map(CityMapper::toDto);
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode, pageable).map(CityMapper::toDto);
    }

    @Override
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCodeIgnoreCase(cityCode);
    }


}
