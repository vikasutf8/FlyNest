package com.flynest.location_service.service.impl;

import com.flynest.location_service.Repository.AirportRepository;
import com.flynest.location_service.Repository.CityRepository;
import com.flynest.location_service.mapper.AirportMapper;
import com.flynest.location_service.model.Airport;
import com.flynest.location_service.model.City;
import com.flynest.location_service.service.AirportService;
import com.flynest.location_service.service.CityService;
import com.flynest.payload.request.AirportRequest;
import com.flynest.payload.response.AirportResponse;
import com.flynest.payload.response.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AirportServiceimpl implements AirportService {


    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final CityService cityService;

    @Override
    public AirportResponse createAirport(AirportRequest request) {

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + request.getCityId()));
        if (airportRepository.existsByIataIgnoreCase(request.getIata())) {
            throw new IllegalArgumentException("Airport with IATA code already exists: " + request.getIata());
        }
        Airport airport = AirportMapper.toEntity(request);
        airport.setCity(city);
//        return  null;
        return AirportMapper.toDto(airportRepository.save(airport));
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest request) {
        Airport existing = airportRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Airport not found with id: " + id));

        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + request.getCityId()));
            existing.setCity(city);
        }

        // same and already present IATA code we can;t update that ...meant findBYIataCode ...should be unique
        if (request.getIata() != null && !request.getIata().equals(existing.getIata())) {
            if (airportRepository.existsByIataIgnoreCase(request.getIata())) {
                throw new IllegalArgumentException("Airport with IATA code already exists: " + request.getIata());
            }
        }
        AirportMapper.updateEntityFromRequest(request, existing);
        Airport updated = airportRepository.save(existing);
        return AirportMapper.toDto(updated);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(AirportMapper::toDto)
                .toList();
    }

    @Override
    public AirportResponse getAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airport not found with id: " + id));
        return AirportMapper.toDto(airport);
    }

    @Override
    public void deleteAirport(Long id) {

        if (!airportRepository.existsById(id)) {
            throw new IllegalArgumentException("Airport not found with id: " + id);
        }
        airportRepository.deleteById(id);
    }

    @Override
    public List<AirportResponse> getAirportsByCityId(Long cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new IllegalArgumentException("City not found with id: " + cityId);
        }
        return airportRepository.findByCityId(cityId).stream()
                .map(AirportMapper::toDto)
                .toList();
    }
}
